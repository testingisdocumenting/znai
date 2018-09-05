package com.twosigma.documentation.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

@Mojo(name = "build", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
class MDocMavenBuildRunner extends AbstractMojo {
    @Parameter
    private String docId

    @Parameter
    private String sourceRoot

    @Parameter
    private String deployRoot

    @Override
    void execute() throws MojoExecutionException, MojoFailureException {
        MDocCliRunner.run(new MavenPluginConsoleOuput(getLog()), [
                'doc-id': docId,
                source  : sourceRoot,
                deploy  : deployRoot,
        ])
    }
}
