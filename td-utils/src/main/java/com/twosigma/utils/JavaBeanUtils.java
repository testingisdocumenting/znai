package com.twosigma.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class JavaBeanUtils {
    private JavaBeanUtils() {
    }

    public static Map<String, ?> convertBeanToMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }

        try {
            return extractMap(bean);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, ?> extractMap(Object bean) throws IntrospectionException,
            InvocationTargetException,
            IllegalAccessException {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : properties) {
            if (property.getName().equals("class")) {
                continue;
            }

            result.put(property.getName(), property.getReadMethod().invoke(bean));
        }

        return result;
    }
}
