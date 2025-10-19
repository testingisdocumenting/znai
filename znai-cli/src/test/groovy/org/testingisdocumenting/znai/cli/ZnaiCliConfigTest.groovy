/*
 * Copyright 2025 znai maintainers
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

import static org.testingisdocumenting.znai.cli.ZnaiCliConfig.Command
import static org.testingisdocumenting.znai.cli.ZnaiCliConfig.OptionKey

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
        mode('build', '--doc-id=my-doc').should == 'build'
        mode('preview').should == 'preview'
        mode('preview', '--port=4000').should == 'preview'
        mode('export', 'my-dir').should == 'export'
        mode('new').should == 'scaffold new'
        mode('serve').should == 'serve'
    }

    @Test
    void "defaults to build when no command specified"() {
        mode('--source=docs').should == 'build'
        mode('some-path').should == 'build'
    }

    @Test
    void "preview command options"() {
        def options = createOptionsForCommand(Command.PREVIEW, '--source=test')

        assertOnlyTheseOptions(options,
                OptionKey.HOST,
                OptionKey.PORT,
                OptionKey.DEPLOY,
                OptionKey.SSL_JKS_PATH,
                OptionKey.SSL_JKS_PASSWORD,
                OptionKey.SSL_PEM_CERT_PATH,
                OptionKey.SSL_PEM_KEY_PATH,
                OptionKey.SOURCE,
                OptionKey.MARKUP_TYPE,
                OptionKey.HELP,
                OptionKey.VERSION,
                OptionKey.VALIDATE_EXTERNAL_LINKS,
                OptionKey.ACTOR,
                OptionKey.MODIFIED_TIME,
                OptionKey.LOOKUP_PATHS
        )
    }

    @Test
    void "build command options"() {
        def options = createOptionsForCommand(Command.BUILD, '--source=test')

        assertOnlyTheseOptions(options,
                OptionKey.DOC_ID,
                OptionKey.DEPLOY,
                OptionKey.SOURCE,
                OptionKey.MARKUP_TYPE,
                OptionKey.HELP,
                OptionKey.VERSION,
                OptionKey.VALIDATE_EXTERNAL_LINKS,
                OptionKey.ACTOR,
                OptionKey.MODIFIED_TIME,
                OptionKey.LOOKUP_PATHS,
                OptionKey.SSL_JKS_PATH,
                OptionKey.SSL_JKS_PASSWORD,
                OptionKey.SSL_PEM_CERT_PATH,
                OptionKey.SSL_PEM_KEY_PATH,
        )
    }

    @Test
    void "export command options"() {
        def options = createOptionsForCommand(Command.EXPORT, '--source=test')

        assertOnlyTheseOptions(options,
                OptionKey.EXPORT,
                OptionKey.SOURCE,
                OptionKey.MARKUP_TYPE,
                OptionKey.HELP,
                OptionKey.VERSION,
                OptionKey.VALIDATE_EXTERNAL_LINKS,
                OptionKey.ACTOR,
                OptionKey.MODIFIED_TIME,
                OptionKey.LOOKUP_PATHS,
                OptionKey.SSL_JKS_PATH,
                OptionKey.SSL_JKS_PASSWORD,
                OptionKey.SSL_PEM_CERT_PATH,
                OptionKey.SSL_PEM_KEY_PATH,
        )
    }

    @Test
    void "new command options"() {
        def options = createOptionsForCommand(Command.NEW, '--source=test')

        assertOnlyTheseOptions(options,
                OptionKey.SOURCE,
                OptionKey.MARKUP_TYPE,
                OptionKey.HELP,
                OptionKey.VERSION,
                OptionKey.VALIDATE_EXTERNAL_LINKS,
                OptionKey.ACTOR,
                OptionKey.MODIFIED_TIME,
                OptionKey.LOOKUP_PATHS,
                OptionKey.SSL_JKS_PATH,
                OptionKey.SSL_JKS_PASSWORD,
                OptionKey.SSL_PEM_CERT_PATH,
                OptionKey.SSL_PEM_KEY_PATH,

        )
    }

    private static createConfig(String... args) {
        return new ZnaiCliConfig((exitCode) -> { println "exit code: ${exitCode}" }, args)

    }

    private static String mode(String... args) {
        return createConfig(args).modeAsString
    }

    private static Options createOptionsForCommand(Command command, String... args) {
        def commandName = command.getName()
        def allArgs = [commandName] + args.toList()
        def config = createConfig(allArgs as String[])
        return config.createOptionsForCommand(command)
    }

    private static void assertOnlyTheseOptions(Options options, OptionKey... expectedKeys) {
        def allKeys = OptionKey.values()

        expectedKeys.each { key ->
            def optionName = key.getKey()
            assert options.hasOption(optionName), "Expected option ${optionName} (${key}) to be present"
        }

        def expectedSet = expectedKeys as Set
        allKeys.each { key ->
            if (!expectedSet.contains(key)) {
                def optionName = key.getKey()
                assert !options.hasOption(optionName), "Unexpected option ${optionName} (${key}) should not be present"
            }
        }
    }
}