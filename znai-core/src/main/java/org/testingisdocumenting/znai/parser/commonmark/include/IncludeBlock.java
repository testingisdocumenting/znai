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

package org.testingisdocumenting.znai.parser.commonmark.include;

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.commonmark.node.CustomBlock;

public class IncludeBlock extends CustomBlock {
    private PluginParams params;

    IncludeBlock() {
    }

    public void setParams(PluginParams params) {
        this.params = params;
    }

    public String getId() {
        return params.getPluginId();
    }

    public PluginParams getParams() {
        return params;
    }
}
