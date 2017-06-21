package com.twosigma.testing.webtau.data.converters;

import com.twosigma.testing.data.converters.ToNumberConverter;
import com.twosigma.testing.webtau.page.ElementValue;
import com.twosigma.testing.webtau.page.PageElement;
import com.twosigma.utils.NumberUtils;

/**
 * @author mykola
 */
public class ElementValueToNumberConverter implements ToNumberConverter {
    @Override
    public Number convert(Object v) {
        if (! (v instanceof ElementValue)) {
            return null;
        }

        ElementValue elementValue = (ElementValue) v;
        return NumberUtils.convertStringToNumber(elementValue.get().toString());
    }
}
