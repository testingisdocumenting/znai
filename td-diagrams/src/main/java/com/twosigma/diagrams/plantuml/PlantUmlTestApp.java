package com.twosigma.diagrams.plantuml;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class PlantUmlTestApp {
    public static void main(String[] args) throws IOException {
        String source = "@startuml\n";
        source += "Bob -> Alice : hello\n";
        source += "@enduml\n";

        SourceStringReader reader = new SourceStringReader(source);

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();

        Files.write(Paths.get("plantuml.svg"), os.toByteArray());
    }
}
