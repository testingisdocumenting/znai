package com.twosigma.testing.data.converters;

import com.twosigma.utils.JavaBeanUtils;

import java.util.Map;

/**
 * @author mykola
 */
public class BeanToMapConverter implements ToMapConverter {
    @Override
    public Map<String, ?> convert(Object v) {
        return JavaBeanUtils.convertBeanToMap(v);
    }
}
