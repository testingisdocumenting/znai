package com.twosigma.znai.client;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

import java.nio.file.Path;

public class ZipTask extends Zip {
    public ZipTask(Path dirToZip, Path zipDestination) {
        setProject(new Project());
        getProject().init();

        setBasedir(dirToZip.toFile());
        setDestFile(zipDestination.toFile());
    }
}
