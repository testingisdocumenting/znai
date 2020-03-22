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

package com.twosigma.znai.java.parser;

public class JavaType {
    private String name;
    private String fullBody;
    private String bodyOnly;

    public JavaType(String name, String fullBody, String bodyOnly) {
        this.name = name;
        this.fullBody = fullBody;
        this.bodyOnly = bodyOnly;
    }

    public String getName() {
        return name;
    }

    public String getFullBody() {
        return fullBody;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }
}
