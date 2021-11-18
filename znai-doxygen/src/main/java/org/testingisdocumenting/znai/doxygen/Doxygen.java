/*
 * Copyright 2021 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.doxygen;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.doxygen.parser.*;
import org.testingisdocumenting.znai.utils.FileUtils;
import org.testingisdocumenting.znai.utils.JsonUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Doxygen {
    public static Doxygen INSTANCE = new Doxygen();

    private static final String DOXYGEN_JSON_CFG_NAME = "doxygen.json";
    private static final String INDEX_PATH_KEY = "indexPath";

    private DoxygenIndex doxygenIndexCached;
    private Path indexPath;
    private FileTime indexLastModifiedTime;

    private final Map<String, DoxygenMember> memberByName;
    private final Map<String, DoxygenCompound> compoundByName;

    private Doxygen() {
        memberByName = new ConcurrentHashMap<>();
        compoundByName = new ConcurrentHashMap<>();
    }

    public Path getIndexPath() {
        return indexPath;
    }

    public DoxygenMember getCachedOrFindAndParseMember(ComponentsRegistry componentsRegistry, String fullName) {
        DoxygenMember cachedMember = memberByName.get(fullName);
        if (cachedMember != null) {
            return cachedMember;
        }

        DoxygenIndex doxygenIndex = buildIndexOrGetCached(componentsRegistry);
        DoxygenIndexMember indexMember = doxygenIndex.findMemberByName(fullName);
        if (indexMember == null) {
            return null;
        }

        DoxygenCompound compound = getCachedOrFindAndParseCompound(componentsRegistry,
                indexMember.getCompound().getName());


        return compound.findByFullName(fullName);
    }

    public DoxygenMembersList findAndParseAllMembers(ComponentsRegistry componentsRegistry, String fullName) {
        DoxygenIndex doxygenIndex = buildIndexOrGetCached(componentsRegistry);
        List<DoxygenIndexMember> indexMembersList = doxygenIndex.findAllMembersByName(fullName);
        if (indexMembersList.isEmpty()) {
            return new DoxygenMembersList();
        }

        return new DoxygenMembersList(indexMembersList.stream()
                .map(indexMember -> {
                    DoxygenCompound compound = getCachedOrFindAndParseCompound(componentsRegistry,
                            indexMember.getCompound().getName());

                    return compound.findById(indexMember.getId());
                }));
    }

    public DoxygenCompound getCachedOrFindAndParseCompound(ComponentsRegistry componentsRegistry, String fullName) {
        DoxygenCompound cachedCompound = compoundByName.get(fullName);
        if (cachedCompound != null) {
            return cachedCompound;
        }

        DoxygenIndex doxygenIndex = buildIndexOrGetCached(componentsRegistry);
        DoxygenIndexCompound indexCompound = doxygenIndex.findCompoundByName(fullName);
        if (indexCompound == null) {
            return null;
        }

        DoxygenCompound compound = findAndParseCompound(componentsRegistry, indexCompound);

        this.compoundByName.put(fullName, compound);
        return compound;
    }

    public DoxygenIndex buildIndexOrGetCached(ComponentsRegistry componentsRegistry) {
        if (indexPath != null) {
            FileTime indexModifiedTime = FileUtils.getLastModifiedTime(indexPath);
            if (indexModifiedTime.equals(indexLastModifiedTime)) {
                return doxygenIndexCached;
            }
        }

        buildIndex(componentsRegistry);
        memberByName.clear();
        compoundByName.clear();

        return doxygenIndexCached;
    }

    private void buildIndex(ComponentsRegistry componentsRegistry) {
        if (indexPath == null) {
            indexPath = extractDoxygenIndexPath(componentsRegistry);
        }

        String indexXml = FileUtils.fileTextContent(indexPath);
        doxygenIndexCached = DoxygenIndexParser.parse(indexXml);
        indexLastModifiedTime = FileUtils.getLastModifiedTime(indexPath);
    }

    private DoxygenCompound findAndParseCompound(ComponentsRegistry componentsRegistry, DoxygenIndexCompound indexCompound) {
        String xml = FileUtils.fileTextContent(indexPath.getParent().resolve(indexCompound.getId() + ".xml"));
        return DoxygenCompoundParser.parse(componentsRegistry, xml, indexCompound.getId());
    }

    private Path extractDoxygenIndexPath(ComponentsRegistry componentsRegistry) {
        Path docRoot = componentsRegistry.docConfig().getDocRoot();
        Path cfgPath = docRoot.resolve(DOXYGEN_JSON_CFG_NAME).toAbsolutePath();
        if (!Files.exists(cfgPath)) {
            throw new IllegalArgumentException("can't find " + DOXYGEN_JSON_CFG_NAME + " config file");
        }

        Map<String, ?> cfg = JsonUtils.deserializeAsMap(FileUtils.fileTextContent(cfgPath));
        Object indexPath = cfg.get(INDEX_PATH_KEY);
        if (indexPath == null) {
            throw new IllegalArgumentException("can't find " + INDEX_PATH_KEY + " key that points to doxygen index.xml");
        }

        return docRoot.resolve(indexPath.toString());
    }
}
