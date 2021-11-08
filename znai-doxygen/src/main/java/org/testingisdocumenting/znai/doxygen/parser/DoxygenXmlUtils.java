/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.doxygen.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class DoxygenXmlUtils {
    private DoxygenXmlUtils() {
    }

    public static String extractNameNodeText(Node parentNode) {
        NodeList nodes = parentNode.getChildNodes();
        for (int idx = 0; idx < nodes.getLength(); idx++) {
            Node node = nodes.item(idx);
            if (node.getNodeName().equals("name")) {
                return node.getTextContent();
            }
        }

        throw new IllegalArgumentException("no nested name node found in: " + parentNode);
    }
}
