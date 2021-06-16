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

    public static final PythonBasedPythonParser INSTANCE = new PythonBasedPythonParser();
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private PythonBasedPythonParser() {
        Process process = createProcess();
        inputStream = process.getInputStream();
        outputStream = process.getOutputStream();
    }

    @SuppressWarnings("unchecked")
    public PythonCode parse(Path path) {
        write(path);
        String json = read();

        try {
            return new PythonCode((List<Map<String, Object>>) JsonUtils.deserializeAsList(json));
        } catch (Exception e) {
            throw new RuntimeException("can't parse python parser output: " + json, e);
        }
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
