package com.twosigma.documentation.parser.commonmark.include;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        private static final Pattern INCLUDE_PATTERN = Pattern.compile(":include-(\\S+)+:\\S*([^\\{]*)(\\{[^\\}]+\\})?");

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
            final Node previous = text.getParent().getPrevious();
            IncludeNode includeNode = createNode(includeStatement);
            text.unlink();
            previous.insertAfter(includeNode);
        }

        private IncludeNode createNode(final String includeStatement) {
            final Matcher matcher = INCLUDE_PATTERN.matcher(includeStatement);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("To define include pattern use\n:" +
                    "include-plugin-id: free form value {optional: keyValues}\n" +
                    "Got: " + includeStatement);
            }

            return new IncludeNode(matcher.group(1).trim(), matcher.group(2).trim());
        }
    }
}
