/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.api

import org.junit.Test

class ApiParametersTest {
    @Test
    void "should collect all anchorIds"() {
        def params = new ApiParameters('')
        populateParams(params)

        params.collectAllAnchors().should == ['firstName', 'lastName', 'address', 'address_street']
    }

    @Test
    void "should use root prefix when collecting all anchorIds"() {
        def params = new ApiParameters('myRoot')
        populateParams(params)

        params.collectAllAnchors().should == ['myRoot_firstName', 'myRoot_lastName', 'myRoot_address',
                                              'myRoot_address_street']
    }

    private static void populateParams(ApiParameters params) {
        params.add('firstName', new ApiLinkedText('String'), [], '')
        params.add('lastName', new ApiLinkedText('String'), [], '')

        def address = params.add('address', new ApiLinkedText('Object'), [], '')
        address.add('street', new ApiLinkedText('String'), [], '')
    }
}
