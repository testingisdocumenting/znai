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

package org.testingisdocumenting.znai.jupyter;

import java.util.List;

public class JupyterCell {
    public static final String CODE_TYPE = "code";
    public static final String MARKDOWN_TYPE = "markdown";

    private final String type;
    private final String input;
    private final List<JupyterOutput> outputs;

    public JupyterCell(String type, String input, List<JupyterOutput> outputs) {
        this.type = type;
        this.input = input;
        this.outputs = outputs;
    }

    public String getType() {
        return type;
    }

    public String getInput() {
        return input;
    }

    public List<JupyterOutput> getOutputs() {
        return outputs;
    }

    public boolean hasTextOutput() {
        return outputs.stream().anyMatch((output) -> output.format().equals(JupyterOutput.TEXT_FORMAT));
    }
}
