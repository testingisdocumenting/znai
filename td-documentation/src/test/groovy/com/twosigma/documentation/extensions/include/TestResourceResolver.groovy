package com.twosigma.documentation.extensions.include

import com.twosigma.utils.ResourceUtils

/**
 * @author mykola
 */
class TestResourceResolver implements IncludeResourcesResolver {
    @Override
    String textContent(String path) {
        return ResourceUtils.textContent(path)
    }
}
