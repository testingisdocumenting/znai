package com.twosigma.testing.webtau.cfg;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.console.ansi.FontStyle;
import com.twosigma.utils.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.twosigma.testing.webtau.cfg.ConfigValue.declare;

/**
 * @author mykola
 */
public class WebUiTestConfig {
    public static final WebUiTestConfig INSTANCE = new WebUiTestConfig();

    private ConfigValue config = declare("config", "config path", "test.cfg");
    private ConfigValue url = declare("url", "base url for application under test", null);
    private ConfigValue waitTimeout = declare("waitTimeout", "wait timeout in milliseconds", 5000);
    private ConfigValue docPath = declare("docPath", "path for screenshots and other generated " +
            "artifacts for documentation", "");
    private ConfigValue windowWidth = declare("windowWidth", "browser window width", 1000);
    private ConfigValue windowHeight = declare("windowHeight", "browser window height", 800);
    private ConfigValue workingDir = declare("workingDir", "logical working dir", Paths.get(""));

    private List<ConfigValue> cfgValues = Arrays.asList(
            config,
            url,
            workingDir,
            waitTimeout,
            docPath,
            windowWidth,
            windowHeight);

    public Stream<ConfigValue> getCfgValuesStream() {
        return cfgValues.stream();
    }

    public void acceptConfigValues(String source, Map values) {
        cfgValues.forEach(v -> v.accept(source, values));
    }

    public String getBaseUrl() {
        return url.getAsString();
    }

    public int waitTimeout() {
        return waitTimeout.getAsInt();
    }

    public Path getDocArtifactsPath() {
        return Paths.get(docPath.getAsString());
    }

    public int getWindowWidth() {
        return windowWidth.getAsInt();
    }

    public int getWindowHeight() {
        return windowHeight.getAsInt();
    }

    public Path getWorkingDir() {
        return workingDir.getAsPath();
    }

    public String getWorkingDirConfigName() {
        return workingDir.getKey();
    }

    @Override
    public String toString() {
        return cfgValues.stream().map(ConfigValue::toString).collect(Collectors.joining("\n"));
    }

    public void print() {
        int maxKeyLength = cfgValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getKey().length()).max(Integer::compareTo).orElse(0);

        int maxValueLength = cfgValues.stream()
                .filter(ConfigValue::nonDefault)
                .map(v -> v.getAsString().length()).max(Integer::compareTo).orElse(0);

        cfgValues.stream().filter(ConfigValue::nonDefault).forEach(v -> {
            String valueAsText = v.getAsString();
            int valuePadding = maxValueLength - valueAsText.length();

            ConsoleOutputs.out(Color.BLUE, String.format("%" + maxKeyLength + "s", v.getKey()), ": ",
                            Color.YELLOW, valueAsText,
                            StringUtils.createIndentation(valuePadding),
                            FontStyle.NORMAL, " // from ", v.getSource());
                }
        );
    }
}
