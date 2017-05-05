package com.twosigma.documentation.parser.commonmark.include;

import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePluginParser;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.PostProcessor;

/**
 * @author mykola
 */
public class IncludePostProcessor implements PostProcessor {
    @Override
    public Node process(final Node node) {
        final IncludeVisitor visitor = new IncludeVisitor();
        node.accept(visitor);

        return node;
    }

    private static class IncludeVisitor extends AbstractVisitor {
        @Override
        public void visit(final Text text) {
            String includeStatement = text.getLiteral().trim();
            if (includeStatement.startsWith(":include-")) {
                insertIncludeNode(text, includeStatement);
            } else {
                super.visit(text);
            }
        }

        private void insertIncludeNode(final Text text, final String includeStatement) {
            Node parent = text.getParent();

            Node previous = text.getPrevious();
            Node previousParent = previous != null ? previous.getParent() : null;
            IncludeNode includeNode = createNode(includeStatement);

            text.unlink();

            (previousParent != null ? previousParent : parent).insertAfter(includeNode);
        }

        private IncludeNode createNode(final String includeStatement) {
            PluginParams params = IncludePluginParser.parse(includeStatement);
            return new IncludeNode(params);
        }
    }
}
