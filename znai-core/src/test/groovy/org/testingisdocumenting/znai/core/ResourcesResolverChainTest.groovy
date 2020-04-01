/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.core

import org.junit.Before
import org.junit.Test

import java.nio.file.Path
import java.util.stream.Stream

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static java.util.stream.Collectors.toList

class ResourcesResolverChainTest {
    private ResourcesResolver resolverA
    private ResourcesResolver resolverB
    private ResourcesResolverChain chain

    @Before
    void init() {
        resolverA = new Resolver('a')
        resolverB = new Resolver('b')

        def lookupPaths = ['a:1', 'b:1', 'a:2', 'b:2', 'b:3']

        chain = new ResourcesResolverChain()
        chain.addResolver(resolverA)
        chain.addResolver(resolverB)
        chain.initialize(lookupPaths.stream())
    }

    @Test
    void "tries resolvers in order and stops when finds resolver that canResolve"() {
        chain.canResolve('_b_').should == true
        chain.canResolve('_a_').should == true
        chain.canResolve('_c_').shouldNot == true
    }

    @Test
    void "enumerates all paths checked when no resolvers can resolve a path"() {
        def expectedPaths = ['a:1/path', 'a:2/path', 'b:1/path', 'b:2/path', 'b:3/path']
        chain.listOfTriedLocations('path').should == expectedPaths

        code {
            chain.textContent('path')
        } should throwException("Can't find <path>\n\n" +
                "Tried following ResourceResolvers:\n" +
                "org.testingisdocumenting.znai.core.ResourcesResolverChainTest.Resolver resources not found:\n" +
                "  a:1/path\n" +
                "  a:2/path\n" +
                "org.testingisdocumenting.znai.core.ResourcesResolverChainTest.Resolver resources not found:\n" +
                "  b:1/path\n" +
                "  b:2/path\n" +
                "  b:3/path")
    }

    private static class Resolver implements ResourcesResolver {
        private final String prefix
        private List<String> lookupPaths

        Resolver(String prefix) {
            this.prefix = prefix
        }

        @Override
        void initialize(Stream<String> filteredLookupPaths) {
            lookupPaths = filteredLookupPaths.collect(toList())
        }

        @Override
        boolean supportsLookupPath(String lookupPath) {
            return lookupPath.startsWith(prefix + ':')
        }

        @Override
        boolean canResolve(String path) {
            return path.contains('_' + prefix + '_')
        }

        @Override
        List<String> listOfTriedLocations(String path) {
            return lookupPaths.collect { it + '/' + path}
        }

        @Override
        Path fullPath(String path) {
            return lookupPaths[0] + '/' + path
        }

        @Override
        Path docRootRelativePath(Path path) {
            return null
        }

        @Override
        boolean isInsideDoc(Path path) {
            return false
        }

        @Override
        boolean isLocalFile(String path) {
            return true
        }
    }
}
