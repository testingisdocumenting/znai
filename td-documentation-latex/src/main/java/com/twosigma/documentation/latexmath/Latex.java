package com.twosigma.documentation.latexmath;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.scilab.forge.jlatexmath.DefaultTeXFont;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.scilab.forge.jlatexmath.greek.GreekRegistration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class Latex {
    static {
        init();
    }

    public static String toSvg(String latex) throws IOException {
        DOMImplementation dom = GenericDOMImplementation.getDOMImplementation();
        Document document = dom.createDocument("http://www.w3.org/2000/svg", "svg", null);

        SVGGraphics2D svg2d = new SVGGraphics2D(SVGGeneratorContext.createDefault(document), false);

        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

        svg2d.setSVGCanvasSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
        svg2d.setColor(Color.white);
        svg2d.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());

        JLabel label = new JLabel();
        label.setForeground(new Color(0, 0, 0));
        icon.paintIcon(label, svg2d, 0, 0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(outputStream, "UTF-8");
        svg2d.stream(out, false);

        return outputStream.toString("UTF-8");
    }

    private static void init() {
        DefaultTeXFont.registerAlphabet(new GreekRegistration());
    }

    public static void main(String[] args) throws IOException {
        String latex = "\\begin{eqnarray}\n" +
                "i_w\\cdot\\psi_v\\cdot N_w&=&\\psi_w\\cdot i_w\\cdot N_w\\\\\n" +
                "&=&\\psi_w\\cdot i_w\\cdot\\sum_{\\sigma\\in G_w}\\sigma\\\\\n" +
                "&=&\\psi_w\\cdot\\sum_{\\sigma\\in G_w}\\sigma\\\\\n" +
                "&=& i_w\\cdot N_w\\cdot\\psi_w.\n" +
                "\\end{eqnarray}";

        String svg = toSvg(latex);
        Files.write(Paths.get("test.svg"), svg.getBytes());
    }
}
