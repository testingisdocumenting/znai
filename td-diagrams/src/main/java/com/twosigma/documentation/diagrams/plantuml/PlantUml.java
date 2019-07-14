package com.twosigma.documentation.diagrams.plantuml;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class PlantUml {
    public static String generateSvg(String plantUml) {
        SourceStringReader reader = new SourceStringReader(plantUml);

        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();

            return os.toString(String.valueOf(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
