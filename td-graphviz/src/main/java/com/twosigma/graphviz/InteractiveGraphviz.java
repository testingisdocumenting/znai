package com.twosigma.graphviz;

import java.io.*;
import java.util.Scanner;

/**
 * @author mykola
 */
public class InteractiveGraphviz {
    private final Process dot;
    private final OutputStream outputStream;
    private final InputStream inputStream;

    public InteractiveGraphviz() {
        dot = createProcess();

        inputStream = dot.getInputStream();
        outputStream = dot.getOutputStream();
    }

    public String svgFromDot(String dot) {
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
            System.out.println(line);
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

    public static void main(String[] args) throws IOException {
        InteractiveGraphviz interactiveGraphviz = new InteractiveGraphviz();
        String svg = interactiveGraphviz.svgFromDot("digraph Simple {" +
                "node [shape=record]\n" +
                "    main -> parse;\n" +
                "}");

        System.out.println("--- svg ----");
        System.out.println(svg.replace('\n', ' '));
    }
}
