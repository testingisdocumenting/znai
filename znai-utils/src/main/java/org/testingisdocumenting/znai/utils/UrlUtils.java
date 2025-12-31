/*
 * Copyright 2022 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.utils;

import java.util.regex.Pattern;

public class UrlUtils {
    private static final Pattern EXTERNAL_URL_PATTERN = Pattern.compile("^\\S+://");

    public static String concat(String left, String right) {
        if (left == null) {
            throw new IllegalArgumentException("passed url on the left is NULL");
        }

        if (right == null) {
            throw new IllegalArgumentException("passed url on the right is NULL");
        }

        if (left.endsWith("/") && !right.startsWith("/")) {
            return left + right;
        }

        if (! left.endsWith("/") && right.startsWith("/")) {
            return left + right;
        }

        if (left.endsWith("/") && right.startsWith("/")) {
            return left + right.substring(1);
        }

        return left + "/" + right;
    }

    public static boolean isExternal(String url) {
        return EXTERNAL_URL_PATTERN.matcher(url).find();
    }

    public static String removeAnchor(String url) {
        var idx = url.indexOf('#');
        if (idx == -1) {
            return url;
        }

        return url.substring(0, idx);
    }

    public static String extractAnchor(String url) {
        var idx = url.lastIndexOf('#');
        if (idx == -1) {
            return "";
        }

        return url.substring(idx + 1);
    }

    public static String attachIndexHtmlIfEndsWithSlash(String url) {
        if (!url.endsWith("/")) {
            return url;
        }

        return url + "index.html";
    }
}
