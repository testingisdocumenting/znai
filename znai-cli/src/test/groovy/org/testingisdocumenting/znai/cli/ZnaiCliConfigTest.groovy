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

package org.testingisdocumenting.znai.cli

import org.apache.commons.cli.Options
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.znai.console.ConsoleOutputs
import org.testingisdocumenting.znai.console.ansi.AnsiConsoleOutput

class ZnaiCliConfigTest {
    static AnsiConsoleOutput consoleOutput = new AnsiConsoleOutput()

    @BeforeClass
    static void init() {
        ConsoleOutputs.add(consoleOutput)
    }

    @BeforeClass
    static void tearDown() {
        ConsoleOutputs.remove(consoleOutput)
    }

    @Test
    void "legacy mode as string"() {
        mode('--deploy=location').should == 'build'
        mode('--doc-id=my-doc').should == 'build'
        mode('--preview').should == 'preview'
        mode('--export=my-dir').should == 'export'
    }

    @Test
    void "new command mode as string"() {
        mode('build', '--deploy=location').should == 'build'
//        mode('build', '--doc-id=my-doc').should == 'build'
//        mode('preview').should == 'preview'
//        mode('preview', '--port=4000').should == 'preview'
//        mode('export', 'my-dir').should == 'export'
//        mode('new').should == 'scaffold new'
//        mode('serve').should == 'serve'
    }

    @Test
    void "defaults to build when no command specified"() {
        mode('--source=docs').should == 'build'
        mode('some-path').should == 'build'
    }

    @Test
    void "preview command includes server and SSL options"() {
        def config = createConfig('preview', '--source=test')
        def options = config.createOptionsForCommand(ZnaiCliConfig.Command.PREVIEW)

        // Server options should be available for preview
        options.hasOption('host').should == true
        options.hasOption('port').should == true
        options.hasOption('deploy').should == true

        // SSL options should be available for preview
        options.hasOption('jks-path').should == true
        options.hasOption('jks-password').should == true
        options.hasOption('pem-cert-path').should == true
        options.hasOption('pem-key-path').should == true

        // Build-specific options should NOT be available
        options.hasOption('doc-id').should == false

        // Common options should be available
        options.hasOption('source').should == true
        options.hasOption('markup-type').should == true
    }

    @Test
    void "build command includes doc-id but not server options"() {
        def config = createConfig('build', '--source=test')
        def options = createOptionsForCommand(config, ZnaiCliConfig.Command.BUILD)

        // Build-specific options should be available
        options.hasOption('doc-id').should == true

        // Deploy option should be available for build
        options.hasOption('deploy').should == true

        // Server options should NOT be available for build (except deploy)
        options.hasOption('host').should == false
        options.hasOption('port').should == false

        // SSL options should NOT be available for build
        options.hasOption('jks-path').should == false
        options.hasOption('pem-cert-path').should == false

        // Common options should be available
        options.hasOption('source').should == true
        options.hasOption('markup-type').should == true
    }

    @Test
    void "export command includes export option but not server options"() {
        def config = createConfig('export', '--source=test')
        def options = createOptionsForCommand(config, ZnaiCliConfig.Command.EXPORT)

        // Export-specific options should be available
        options.hasOption('export').should == true

        // Server options should NOT be available for export
        options.hasOption('host').should == false
        options.hasOption('port').should == false
        options.hasOption('jks-path').should == false

        // Build-specific options should NOT be available
        options.hasOption('doc-id').should == false

        // Common options should be available
        options.hasOption('source').should == true
        options.hasOption('markup-type').should == true
    }

    @Test
    void "new command shows only common options"() {
        def config = createConfig('new', '--source=test')
        def options = createOptionsForCommand(config, ZnaiCliConfig.Command.NEW)

        // Command-specific options should NOT be available
        options.hasOption('host').should == false
        options.hasOption('port').should == false
        options.hasOption('deploy').should == false
        options.hasOption('jks-path').should == false
        options.hasOption('doc-id').should == false
        options.hasOption('export').should == false

        // Common options should be available
        options.hasOption('source').should == true
        options.hasOption('markup-type').should == true
        options.hasOption('help').should == true
        options.hasOption('version').should == true
    }

    private static createConfig(String... args) {
        return new ZnaiCliConfig((exitCode) -> { println "exit code: ${exitCode}" }, args)

    }

    private static String mode(String... args) {
        return createConfig(args).modeAsString
    }

    private static Options createOptionsForCommand(ZnaiCliConfig config, ZnaiCliConfig.Command command) {
        return config.createOptionsForCommand(command)
    }
}