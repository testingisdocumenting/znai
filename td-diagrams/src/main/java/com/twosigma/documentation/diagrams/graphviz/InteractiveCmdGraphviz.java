package com.twosigma.documentation.diagrams.graphviz;

import java.io.*;
import java.util.Scanner;

/**
 * @author mykola
 */
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
        write(gv);
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

    private void write(String gv) {
        try {
            outputStream.write((gv + "\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Process createProcess(String layoutType) {
        try {
            String binPath = getBinPath(layoutType);
            return new ProcessBuilder().command(binPath, "-Tsvg").redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBinPath(String layoutType) {
        String bin = System.getProperty(layoutType + ".bin");
        return bin != null ? bin : layoutType;
    }
}
