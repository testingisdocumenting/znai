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

package org.testingisdocumenting.znai.cpp.parser;

public class EntryDef {
    private String name;
    private String full;
    private String bodyOnly;

    public EntryDef(String name, String full, String bodyOnly) {
        this.name = name;
        this.full = full;
        this.bodyOnly = bodyOnly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull() {
        return full;
    }

    public void setBodyWithDecl(String bodyWithDecl) {
        this.full = bodyWithDecl;
    }

    public String getBodyOnly() {
        return bodyOnly;
    }

    public void setBodyOnly(String bodyOnly) {
        this.bodyOnly = bodyOnly;
    }

    @Override
    public String toString() {
        return "EntryDef{" +
                "name='" + name + '\'' +
                ", full='" + full + '\'' +
                ", bodyOnly='" + bodyOnly + '\'' +
                '}';
    }
}
