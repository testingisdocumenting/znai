/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.server.auth;

import org.apache.commons.codec.binary.Base64;

/**
 * This covers the case when remote user is injected by NGINX proxy. E.g. Kerberos
 */
public class BasicInjectedAuthentication {
    public static String extractUserId(String authorizationHeader) {
        if (authorizationHeader == null) {
            return "";
        }

        String[] parts = authorizationHeader.split(" ");

        Base64 base64 = new Base64();
        String userNameAndFakePassword = new String(base64.decode(parts[1].getBytes()));
        String[] userNamePasswordParts = userNameAndFakePassword.split(" ");

        return userNamePasswordParts[0];
    }
}
