package com.twosigma.znai.maven

import com.twosigma.console.ConsoleOutput
import com.twosigma.console.ConsoleOutputs
import com.twosigma.znai.cli.DocumentationCliApp
import com.twosigma.znai.cli.DocumentationCliConfig

class MDocCliRunner {
    static void run(ConsoleOutput consoleOutput, Map<String, String> argsMap) {
        ConsoleOutputs.add(consoleOutput)

        String[] args = constructArgs(argsMap)
        def config = new DocumentationCliConfig(args)

        DocumentationCliApp.start(config)
    }

    static String[] constructArgs(Map<String, String> args) {
        return args.entrySet().stream()
        .map { entry -> argToString(entry) }
        .toArray { size -> new String[size] }
    }

    private static String argToString(Map.Entry<String, String> entry) {
        String str = "--" + entry.key
        if (entry.value != null) {
            str += "=" + entry.value
        }

        return str
    }
}
