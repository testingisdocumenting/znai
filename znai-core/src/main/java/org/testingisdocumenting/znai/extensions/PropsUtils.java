/*
 * Copyright 2022 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.extensions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PropsUtils {
    /**
     * values inside a map could be lazy evaluated Suppliers and we need to exercise them before rendering the page
     * @param content content with potential suppliers
     * @return content with exercised suppliers
     */
    @SuppressWarnings("unchecked")
    public static Object exerciseSuppliers(Object content) {
        if (content instanceof Supplier) {
            return ((Supplier<?>) content).get();
        }

        if (content instanceof Map) {
            return exerciseMapSuppliers((Map<String, ?>) content);
        }

        if (content instanceof List) {
            return exerciseListSuppliers((List<?>) content);
        }

        return content;
    }

    public static Map<String, ?> exerciseMapSuppliers(Map<String, ?> content) {
        return content.entrySet().stream()
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> exerciseSuppliers(e.getValue()),
                        (a, b) -> b,
                        LinkedHashMap::new));
    }

    public static List<?> exerciseListSuppliers(List<?> content) {
        return content.stream().map(PropsUtils::exerciseSuppliers).collect(Collectors.toList());
    }
}
