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

package org.testingisdocumenting.znai.diagrams.svg;

import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SvgParser {
    public static void main(String[] args) {
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(
                XMLResourceDescriptor.getXMLParserClassName());

        File file = new File("C:/resources/chessboard.svg");
        InputStream is = new FileInputStream(file);

        Document document = factory.createDocument(
                file.toURI().toURL().toString(), is);
        UserAgent agent = new UserAgentAdapter();
        DocumentLoader loader= new DocumentLoader(agent);
        BridgeContext context = new BridgeContext(agent, loader);
        context.setDynamic(true);
        GVTBuilder builder= new GVTBuilder();
        GraphicsNode root= builder.build(context, document);

        System.out.println(root.getPrimitiveBounds().getWidth());
        System.out.println(root.getPrimitiveBounds().getHeight());
    }
}
