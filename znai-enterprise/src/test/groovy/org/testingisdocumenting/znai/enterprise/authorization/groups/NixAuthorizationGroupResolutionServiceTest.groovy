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

package org.testingisdocumenting.znai.enterprise.authorization.groups

import org.junit.Test

import java.util.stream.Collectors

class NixAuthorizationGroupResolutionServiceTest {
    static def userId = System.getProperty("user.name")
    @Test
    void "group names should be trimmed"() {
        def list = NixAuthorizationGroupResolutionService.groupNamesStream(userId)
                .collect(Collectors.toList())

        list.size().shouldBe > 0
        list.each { g -> g.trim().should == g }
    }

    @Test
    void "group should be matched"() {
        def list = NixAuthorizationGroupResolutionService.groupNamesStream(userId)
                .collect(Collectors.toList())

        def service = new NixAuthorizationGroupResolutionService()
        service.groupContainsUser(list[0], userId).should == true
        service.groupContainsUser(list[list.size() - 1], userId).should == true
    }
}
