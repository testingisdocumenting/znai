package com.twosigma.znai.server.upload;

import org.apache.ant.compress.taskdefs.Unzip;
import org.apache.tools.ant.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnzipTask extends Unzip {
    public UnzipTask(Path src, Path dest) {
        setProject(new Project());
        getProject().init();

        try {
            Files.createDirectories(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setSrc(src.toAbsolutePath().toFile());
        setDest(dest.toAbsolutePath().toFile());
        setOverwrite(true);
    }
}
