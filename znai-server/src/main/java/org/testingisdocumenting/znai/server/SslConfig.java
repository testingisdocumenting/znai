/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.server;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;

public record SslConfig(String jksPath, String jksPassword, String pemCertPath, String pemKeyPath) {
    public boolean isSpecified() {
        return (jksPath != null && jksPassword != null) || (pemCertPath != null && pemKeyPath != null);
    }

    public void updateServerOptions(HttpServerOptions serverOptions) {
        if (!isSpecified()) {
            return;
        }

        serverOptions.setSsl(true);

        if (jksPath != null) {
            serverOptions.setKeyStoreOptions(new JksOptions().setPath(jksPath()).setPassword(jksPassword()));
        } else {
            serverOptions.setPemKeyCertOptions(new PemKeyCertOptions().setCertPath(pemCertPath).setKeyPath(pemKeyPath));
        }
    }
}
