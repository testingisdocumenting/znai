package com.twosigma.documentation.html;

import com.twosigma.documentation.structure.TocItem;
import com.twosigma.documentation.utils.NameUtils;

/**
 * @author mykola
 */
public class HtmlAnchors {
    private HtmlAnchors() {
    }

    public static String tocAnchor(TocItem tocItem) {
        return tocItem.getDirName() + "--" + tocItem.getFileNameWithoutExtension();
    }

    public static String headingAnchor(String heading) {
        return NameUtils.camelCaseWithSpacesToDashes(heading);
    }
}
