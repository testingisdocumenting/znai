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
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Doxygen {
    public static Doxygen INSTANCE = new Doxygen();

    private static final String DOXYGEN_JSON_CFG_NAME = "doxygen.json";
    private static final String INDEX_PATH_KEY = "indexPath";

    private DoxygenIndex doxygenIndexCached;
    private final Path indexPath;
    private FileTime indexLastModifiedTime;

    private final Map<String, DoxygenMember> memberByName;

    private Doxygen() {
        memberByName = new ConcurrentHashMap<>();
        indexPath = extractDoxygenIndexPath();
        buildIndex();
    }

    public Path getIndexPath() {
        return indexPath;
    }

    public DoxygenMember getCachedOrFindAndParseMember(ComponentsRegistry componentsRegistry, String nameOrFullName) {
        DoxygenMember cachedMember = memberByName.get(nameOrFullName);
        if (cachedMember != null) {
            return cachedMember;
        }

        DoxygenIndex doxygenIndex = buildIndexOrGetCached();
        DoxygenIndexMember byName = doxygenIndex.findByName(nameOrFullName);
        DoxygenMember member = findAndParseMember(componentsRegistry, byName);

        memberByName.put(nameOrFullName, member);
        return member;
    }

    private DoxygenIndex buildIndexOrGetCached() {
        FileTime indexModifiedTime = FileUtils.getLastModifiedTime(indexPath);
        if (indexModifiedTime.equals(indexLastModifiedTime)) {
            return doxygenIndexCached;
        }

        buildIndex();
        memberByName.clear();

        return doxygenIndexCached;
    }

    private void buildIndex() {
        String indexXml = FileUtils.fileTextContent(indexPath);
        doxygenIndexCached = DoxygenIndexParser.parse(indexXml);
        indexLastModifiedTime = FileUtils.getLastModifiedTime(indexPath);
    }

    private DoxygenMember findAndParseMember(ComponentsRegistry componentsRegistry, DoxygenIndexMember indexMember) {
        String xml = FileUtils.fileTextContent(indexPath.getParent().resolve(indexMember.getCompound().getId() + ".xml"));
        return DoxygenMemberParser.parse(componentsRegistry,
                xml, indexMember.getCompound().getId(), indexMember.getId());
    }

    private Path extractDoxygenIndexPath() {
        Path cfgPath = Paths.get(DOXYGEN_JSON_CFG_NAME);
        if (!Files.exists(cfgPath)) {
            throw new IllegalArgumentException("can't find " + DOXYGEN_JSON_CFG_NAME + " config file");
        }


        Map<String, ?> cfg = JsonUtils.deserializeAsMap(FileUtils.fileTextContent(cfgPath));
        Object indexPath = cfg.get(INDEX_PATH_KEY);
        if (indexPath == null) {
            throw new IllegalArgumentException("can't find " + INDEX_PATH_KEY + " key that points to doxygen index.xml");
        }

        return Paths.get(indexPath.toString());
    }
}
