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

package org.testingisdocumenting.znai.enterprise.authorization.groups;

import org.apache.commons.io.IOUtils;
import org.testingisdocumenting.znai.console.ConsoleOutputs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

public class NixAuthorizationGroupResolutionService implements AuthorizationGroupResolutionService {
    @Override
    public boolean groupContainsUser(String group, String userId) {
        return groupNamesStream(userId).anyMatch(group::equals);
    }

    protected static Stream<String> groupNamesStream(String userId) {
        String groups = readGroups(userId);
        String[] groupsList = groups.split(" ");
        return Arrays.stream(groupsList)
                .map(String::trim);
    }

    private static String readGroups(String userId) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/sh", "-c", "groups " + userId);
            Process process = processBuilder.start();

            String groups = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
            String error = IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8);

            if (!error.isEmpty()) {
                ConsoleOutputs.err(error);
            }

            process.waitFor();
            if (process.exitValue() != 0) {
                return "";
            }

            return groups;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
