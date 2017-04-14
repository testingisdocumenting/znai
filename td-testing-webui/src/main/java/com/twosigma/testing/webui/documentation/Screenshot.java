package com.twosigma.testing.webui.documentation;

import org.eclipse.jetty.io.RuntimeIOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author mykola
 */
public class Screenshot {
    private TakesScreenshot screenshotTaker;
    private BufferedImage bufferedImage;

    public Screenshot(TakesScreenshot screenshotTaker) {
        this.screenshotTaker = screenshotTaker;
        take();
    }

    public void save(String fileName) {
        saveImage(bufferedImage, fileName);
    }

    private BufferedImage takeBufferedImage() {
        byte[] screenshotBytes = screenshotTaker.getScreenshotAs(OutputType.BYTES);
        try {
            return ImageIO.read(new ByteArrayInputStream(screenshotBytes));
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    private void saveImage(BufferedImage bufferedImage, String fileName)  {
        try {
            ImageIO.write(bufferedImage, "png", new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void take() {
        bufferedImage = takeBufferedImage();
        saveImage(bufferedImage, "test.png");
    }
}
