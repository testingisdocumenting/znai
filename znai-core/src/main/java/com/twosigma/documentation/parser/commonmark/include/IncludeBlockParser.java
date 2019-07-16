package com.twosigma.documentation.parser.commonmark.include;

import com.twosigma.documentation.extensions.PluginParams;
import org.commonmark.internal.DocumentBlockParser;
import org.commonmark.internal.IndentedCodeBlockParser;
import org.commonmark.node.Block;
import org.commonmark.parser.InlineParser;
import org.commonmark.parser.block.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncludeBlockParser extends AbstractBlockParser {
    private static final Pattern INCLUDE_PLUGIN_REGEXP = Pattern.compile("^\\s*:include-(\\S+)+:\\s*(.*)$");
    private static final Pattern SPACES_REGEXP = Pattern.compile("^\\s{4,}.*$");

    private final StringBuilder value;
    private final String pluginId;
    private IncludeBlock block;

    IncludeBlockParser(String pluginId, String value) {
        this.pluginId = pluginId;
        this.value = new StringBuilder();
        this.block = new IncludeBlock();

        this.value.append(value);
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public void closeBlock() {
        block.setParams(new PluginParams(pluginId, value.toString()));
        super.closeBlock();
    }

    @Override
    public void addLine(CharSequence line) {
    }

    @Override
    public void parseInlines(InlineParser inlineParser) {
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
        CharSequence line = parserState.getLine();

        if (line.toString().trim().isEmpty() ||
                INCLUDE_PLUGIN_REGEXP.matcher(line).matches()) {
            return BlockContinue.none();
        }

        value.append(line);

        return BlockContinue.atIndex(parserState.getIndex());
    }

    public static class Factory extends AbstractBlockParserFactory {
        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            CharSequence line = state.getLine();

            BlockParser parentParser = matchedBlockParser.getMatchedBlockParser();

            if (parentParser instanceof DocumentBlockParser && SPACES_REGEXP.matcher(line).matches()) {
                return BlockStart.none();
            }

            if (parentParser instanceof IndentedCodeBlockParser) {
                return BlockStart.none();
            }

            Matcher matcher = INCLUDE_PLUGIN_REGEXP.matcher(line);
            if (matcher.matches()) {
                return BlockStart.of(new IncludeBlockParser(matcher.group(1), matcher.group(2))).atIndex(state.getIndex());
            }

            return BlockStart.none();
        }
    }
}
