package com.twosigma.diagrams.graphviz;

import java.io.*;
import java.util.Scanner;

/**
 * @author mykola
 */
public class InteractiveCmdGraphviz implements GraphvizRuntime {
    private final Process dot;
    private final OutputStream outputStream;
    private final InputStream inputStream;

    public InteractiveCmdGraphviz() {
        dot = createProcess();

        inputStream = dot.getInputStream();
        outputStream = dot.getOutputStream();
    }

    public String svgFromGv(String dot) {
        write(dot);
        return readTill("</svg>");
    }

    private String readTill(String endMarker) {
        StringBuilder result = new StringBuilder();

        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("Error: <stdin>:")) {
                throw new RuntimeException(line);
            }

            result.append(line).append("\n");
            if (line.contains(endMarker)) {
                break;
            }
        }

        return result.toString();
    }

    private void write(String dot) {
        try {
            outputStream.write((dot + "\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Process createProcess() {
        try {
            return new ProcessBuilder().command("dot", "-Tsvg").redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
