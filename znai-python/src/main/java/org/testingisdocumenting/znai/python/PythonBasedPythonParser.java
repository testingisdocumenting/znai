/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.testingisdocumenting.znai.utils.ResourceUtils.tempCopyOfResource;

public class PythonBasedPythonParser {
    private static final String PARSE_COMPLETED = "---parse_completed---";

    private final InputStream inputStream;
    private final OutputStream outputStream;

    public PythonBasedPythonParser() {
        Process process = createProcess();
        inputStream = process.getInputStream();
        outputStream = process.getOutputStream();
    }

    @SuppressWarnings("unchecked")
    public PythonParsedFile parse(Path path, PythonContext context) {
        write(path);
        String json = read();

        Map<String, Object> parserResponse;
        try {
            parserResponse = (Map<String, Object>) JsonUtils.deserializeAsMap(json);
        } catch (Exception e) {
            throw new RuntimeException("can't parse python parser output: " + json, e);
        }

        boolean success = (boolean) parserResponse.get("success");
        if (!success) {
            String error = (String) parserResponse.get("error");
            String version = (String) parserResponse.get("version");
            throw new RuntimeException("Error from python (" + version + "): " + error);
        }

        List<String> warnings = (List<String>) parserResponse.get("warnings");
        if (warnings != null && warnings.size() > 0) {
            System.out.println("Warnings from python parsing:");
            warnings.forEach(warning -> System.out.println("\t" + warning));
        }

        return new PythonParsedFile((List<Map<String, Object>>) parserResponse.get("result"), context);
    }

    private void write(Path path) {
        try {
            outputStream.write((path.toString() + "\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String read() {
        StringBuilder result = new StringBuilder();

        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(PARSE_COMPLETED)) {
                break;
            }
            result.append(line);
        }

        return result.toString();
    }

    private Process createProcess() {
        try {
            String pythonPath = getPythonPath();
            Path parserPath = tempCopyOfResource("python_parser.py").toAbsolutePath();

            return new ProcessBuilder().command(pythonPath,
                    parserPath.toString()).redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPythonPath() {
        String nodeBin = System.getProperty("python.bin");
        return nodeBin != null ? nodeBin : "python3";
    }
}
