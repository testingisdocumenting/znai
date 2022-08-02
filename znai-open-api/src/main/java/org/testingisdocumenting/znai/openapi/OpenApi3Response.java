/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.openapi;

public class OpenApi3Response {
    private final String description;
    private final OpenApi3Content content;

    public OpenApi3Response(String description, OpenApi3Content content) {
        this.description = description;
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public OpenApi3Content getContent() {
        return content;
    }
}
