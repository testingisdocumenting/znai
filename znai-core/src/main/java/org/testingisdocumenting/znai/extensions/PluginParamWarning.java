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

import java.util.Objects;

public class PluginParamWarning {
    private final String pluginId;
    private final String parameterName;
    private final String message;

    public PluginParamWarning(String pluginId, String parameterName, String message) {
        this.pluginId = pluginId;
        this.parameterName = parameterName;
        this.message = message;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PluginParamWarning that = (PluginParamWarning) o;
        return pluginId.equals(that.pluginId) &&
                parameterName.equals(that.parameterName) &&
                message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginId, parameterName, message);
    }
}
