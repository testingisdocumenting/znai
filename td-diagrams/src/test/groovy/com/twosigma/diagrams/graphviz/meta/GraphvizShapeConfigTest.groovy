package com.twosigma.diagrams.graphviz.meta

import com.twosigma.utils.ResourceUtils
import org.junit.Test

/**
 * @author mykola
 */
class GraphvizShapeConfigTest {
    @Test
    void "should parse shape configs from json"() {
        def shapeConfig = new GraphvizShapeConfig(ResourceUtils.textContent("graphviz-meta-conf.json"))
        def shape = shapeConfig.nodeShape("man").orElseThrow { new AssertionError("no shape found") }
        assert shape.shape == "octagon"
    }
}
