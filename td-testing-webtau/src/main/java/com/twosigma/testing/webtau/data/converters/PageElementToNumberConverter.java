package com.twosigma.testing.webtau.data.converters;

import com.twosigma.testing.data.converters.ToNumberConverter;
import com.twosigma.testing.webtau.page.PageElement;
import com.twosigma.utils.NumberUtils;

/**
 * @author mykola
 */
public class PageElementToNumberConverter implements ToNumberConverter {
    @Override
    public Number convert(Object v) {
        if (! (v instanceof PageElement)) {
            return null;
        }

        PageElement pageElement = (PageElement) v;
        return NumberUtils.convertStringToNumber(pageElement.elementValue().get().toString());
    }
}
