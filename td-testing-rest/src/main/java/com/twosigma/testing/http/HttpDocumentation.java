package com.twosigma.testing.http;

import com.twosigma.documentation.DocumentationArtifactsLocation;
import com.twosigma.utils.FileUtils;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class HttpDocumentation {
    public void capture(String artifactName) {
        Path path = DocumentationArtifactsLocation.resolve(artifactName + ".json");
        HttpDocumentationArtifact documentationArtifact =
                new HttpDocumentationArtifact(Http.http.getLastValidationResult());

        FileUtils.writeTextContent(path, documentationArtifact.toJson());
    }
}
