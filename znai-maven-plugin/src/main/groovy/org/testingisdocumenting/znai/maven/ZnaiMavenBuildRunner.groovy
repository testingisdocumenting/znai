/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.maven

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

@Mojo(name = "build", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
class ZnaiMavenBuildRunner extends AbstractMojo {
    /**
     * documentation id becomes part of url of the generated website:
     * <code>myhosting.org/&lt;doc-id&gt;</code>
     */
    @Parameter(required = true)
    private String docId

    @Parameter(defaultValue = '${project.basedir}/znai')
    private String sourceRoot

    @Parameter(defaultValue = '${project.build.directory}')
    private String deployRoot

    @Override
    void execute() throws MojoExecutionException, MojoFailureException {
        ZnaiMavenRunner.run(new MavenPluginConsoleOuput(getLog()), [
                'doc-id': docId,
                source  : sourceRoot,
                deploy  : deployRoot,
        ])
    }
}
