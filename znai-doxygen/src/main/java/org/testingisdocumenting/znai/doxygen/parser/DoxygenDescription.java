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

package org.testingisdocumenting.znai.doxygen.parser;

import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.List;

public class DoxygenDescription {
    private final List<DocElement> docElements;
    private final ApiParameters apiParameters;
    private final String searchText;

    public DoxygenDescription(List<DocElement> docElements, ApiParameters apiParameters, String withoutParametersSearchText) {
        this.docElements = docElements;
        this.apiParameters = apiParameters;
        this.searchText = withoutParametersSearchText;
    }

    public List<DocElement> getDocElements() {
        return docElements;
    }

    public String getSearchTextWithoutParameters() {
        return searchText;
    }

    public ApiParameters getApiParameters() {
        return apiParameters;
    }
}
