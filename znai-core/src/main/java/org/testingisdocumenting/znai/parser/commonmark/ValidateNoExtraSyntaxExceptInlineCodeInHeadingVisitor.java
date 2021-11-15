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

class ValidateNoExtraSyntaxExceptInlineCodeInHeadingVisitor extends AbstractVisitor {
    static final ValidateNoExtraSyntaxExceptInlineCodeInHeadingVisitor INSTANCE = new ValidateNoExtraSyntaxExceptInlineCodeInHeadingVisitor();

    @Override
    public void visit(BlockQuote blockQuote) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(BulletList bulletList) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(Code code) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(Document document) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(Emphasis emphasis) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(Image image) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(Link link) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(ListItem listItem) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(OrderedList orderedList) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(Paragraph paragraph) {
        super.visit(paragraph);
    }

    @Override
    public void visit(SoftLineBreak softLineBreak) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(CustomBlock customBlock) {
        onlyRegularTextAllowed();
    }

    @Override
    public void visit(CustomNode customNode) {
        onlyRegularTextAllowed();
    }

    private static void onlyRegularTextAllowed() {
        throw new RuntimeException("only regular text is supported in headings");
    }
}
