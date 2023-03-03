/*
 * Copyright 2023 znai maintainers
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PluginParamValidationResult {
    public final static PluginParamValidationResult EMPTY = new PluginParamValidationResult(Stream.empty(), "");
    private final List<PluginParamWarning> warnings;

    private final String validationError;

    public PluginParamValidationResult(Stream<PluginParamWarning> warningsStream, String validationError) {
        this.warnings = warningsStream.collect(Collectors.toList());
        this.validationError = validationError;
    }

    public boolean isValid() {
        return validationError.isEmpty();
    }

    public List<PluginParamWarning> getWarnings() {
        return warnings;
    }

    public String getValidationError() {
        return validationError;
    }
}
