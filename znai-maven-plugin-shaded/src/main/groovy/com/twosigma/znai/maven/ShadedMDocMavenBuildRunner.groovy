package com.twosigma.znai.maven

import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo

@Mojo(name = "build", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
class ShadedMDocMavenBuildRunner extends MDocMavenBuildRunner {}
