package com.twosigma.znai.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

@Mojo(name = "preview")
class MDocMavenPreviewRunner extends AbstractMojo {
    @Parameter
    private String sourceRoot

    @Parameter(defaultValue = "3333")
    private Integer port

    @Override
    void execute() throws MojoExecutionException, MojoFailureException {
        MDocCliRunner.run(new MavenPluginConsoleOuput(getLog()), [
                preview: null,
                port   : port.toString(),
                source : sourceRoot,
        ])
    }
}
