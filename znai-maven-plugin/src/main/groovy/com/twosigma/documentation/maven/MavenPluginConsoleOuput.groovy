package com.twosigma.documentation.maven

import com.twosigma.console.ConsoleOutput
import com.twosigma.console.ansi.Color
import groovy.transform.Canonical
import org.apache.maven.plugin.logging.Log

import java.util.stream.Collectors

@Canonical
class MavenPluginConsoleOuput implements ConsoleOutput {
    Log log

    @Override
    void out(Object... styleOrValues) {
        log.info(concatIgnoringColours(styleOrValues))
    }

    @Override
    void err(Object... styleOrValues) {
        log.error(concatIgnoringColours(styleOrValues))
    }

    static CharSequence concatIgnoringColours(Object... styleOrValues) {
        return styleOrValues
                .findAll { styleOrValue -> !(styleOrValue instanceof Color) }
                .collect(Objects.&toString)
                .join('')
    }
}
