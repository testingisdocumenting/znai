/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.parser.commonmark;

import org.commonmark.node.*;

class ValidateOnlyAllowedSyntaxInHeadingVisitor extends AbstractVisitor {
    static final ValidateOnlyAllowedSyntaxInHeadingVisitor INSTANCE = new ValidateOnlyAllowedSyntaxInHeadingVisitor();

    @Override
    public void visit(Code code) {
    }

    @Override
    public void visit(Emphasis emphasis) {
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
    }

    @Override
    public void visit(Link link) {
    }

    @Override
    public void visit(BlockQuote blockQuote) {
        thisIsNotAllowed(blockQuote);
    }

    @Override
    public void visit(BulletList bulletList) {
        thisIsNotAllowed(bulletList);
    }

    @Override
    public void visit(Document document) {
        thisIsNotAllowed(document);
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        thisIsNotAllowed(fencedCodeBlock);
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        thisIsNotAllowed(hardLineBreak);
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        thisIsNotAllowed(thematicBreak);
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        thisIsNotAllowed(htmlInline);
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        thisIsNotAllowed(htmlBlock);
    }

    @Override
    public void visit(Image image) {
        thisIsNotAllowed(image);
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        thisIsNotAllowed(indentedCodeBlock);
    }

    @Override
    public void visit(ListItem listItem) {
        thisIsNotAllowed(listItem);
    }

    @Override
    public void visit(OrderedList orderedList) {
        thisIsNotAllowed(orderedList);
    }

    @Override
    public void visit(Paragraph paragraph) {
        super.visit(paragraph);
    }

    @Override
    public void visit(SoftLineBreak softLineBreak) {
        thisIsNotAllowed(softLineBreak);
    }

    @Override
    public void visit(CustomBlock customBlock) {
        thisIsNotAllowed(customBlock);
    }

    @Override
    public void visit(CustomNode customNode) {
        thisIsNotAllowed(customNode);
    }

    private static void thisIsNotAllowed(Node node) {
        throw new RuntimeException("this is not allowed in the header at this point: " + node);
    }
}
