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

package org.testingisdocumenting.znai.diagrams.graphviz;

import org.testingisdocumenting.znai.debug.ZnaiDebug;

import java.io.*;
import java.util.Scanner;

public class InteractiveCmdGraphviz implements GraphvizRuntime {
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final String layoutType;

    public InteractiveCmdGraphviz(String layoutType) {
        this.layoutType = layoutType;

        Process process = createProcess(layoutType);

        inputStream = process.getInputStream();
        outputStream = process.getOutputStream();
    }

    public String getLayoutType() {
        return layoutType;
    }

    public String svgFromGv(String gv) {
        ZnaiDebug.debug("generating svg from graphviz:\n" + gv);
        write(gv);

        String svg = readTill("</svg>");
        ZnaiDebug.debug("generated svg:\n" + svg);

        return svg;
    }

    private String readTill(String endMarker) {
        StringBuilder result = new StringBuilder();

        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("Error: <stdin>:")) {
                throw new RuntimeException(line);
            }

            if (line.startsWith("Warning:")) {
                continue;
            }

            result.append(line).append("\n");
            if (line.contains(endMarker)) {
                break;
            }
        }

        return result.toString();
    }

    private void write(String gv) {
        try {
            outputStream.write((gv + "\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("error processing graphviz:\n" + gv, e);
        }
    }

    private Process createProcess(String layoutType) {
        try {
            String binPath = getBinPath(layoutType);
            return new ProcessBuilder().command(binPath, "-Tsvg").redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new RuntimeException("failed to create process for layoutType: " + layoutType, e);
        }
    }

    private String getBinPath(String layoutType) {
        String bin = System.getProperty(layoutType + ".bin");
        return bin != null ? bin : layoutType;
    }
}
