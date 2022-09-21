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

package org.testingisdocumenting.znai.extensions;

import org.testingisdocumenting.znai.extensions.paramtypes.*;

public interface PluginParamType {
    PluginParamType STRING = new PluginParamTypeString();
    PluginParamType NUMBER = new PluginParamTypeNumber();
    PluginParamType BOOLEAN = new PluginParamTypeBoolean();
    PluginParamType OBJECT = new PluginParamTypeObject();
    PluginParamType LIST_OR_SINGLE_NUMBER = new PluginParamTypeListOrSingleNumber();
    PluginParamType LIST_OR_SINGLE_STRING_OR_NUMBER = new PluginParamTypeListOrSingleStringOrNumber();
    PluginParamType LIST_OR_SINGLE_STRING = new PluginParamTypeListOrSingleString();
    PluginParamType LIST_OR_SINGLE_STRING_WITH_NULLS = new PluginParamTypeListOrSingleStringWithNulls();
    PluginParamType LIST_OF_ANY = new PluginParamTypeListOfAny();

    String description();
    String example();

    boolean isValid(Object param);

    default String descriptionWithExample() {
        return "<" + description() + "> (e.g. " + example() + ")";
    }
}