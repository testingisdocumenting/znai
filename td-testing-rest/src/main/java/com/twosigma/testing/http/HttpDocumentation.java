package com.twosigma.testing.http;

import com.twosigma.documentation.DocumentationArtifactsLocation;
import com.twosigma.utils.FileUtils;
import com.twosigma.utils.JsonUtils;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class HttpDocumentation {
    public void capture(String artifactName) {
        Path path = DocumentationArtifactsLocation.resolve(artifactName + ".json");

        HttpValidationResult lastValidationResult = Http.http.getLastValidationResult();
        if (lastValidationResult == null) {
            throw new IllegalStateException("no http calls were made yet");
        }

        FileUtils.writeTextContent(path, JsonUtils.serializePrettyPrint(lastValidationResult.toMap()));
    }
}
