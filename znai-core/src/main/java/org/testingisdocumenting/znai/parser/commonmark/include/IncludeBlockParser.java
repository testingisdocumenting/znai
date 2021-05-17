/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.parser.commonmark.include;

import org.testingisdocumenting.znai.extensions.PluginParams;
import org.commonmark.internal.DocumentBlockParser;
import org.commonmark.internal.IndentedCodeBlockParser;
import org.commonmark.node.Block;
import org.commonmark.parser.InlineParser;
import org.commonmark.parser.block.*;
import org.testingisdocumenting.znai.extensions.PluginsRegexp;

import java.util.regex.Pattern;

public class IncludeBlockParser extends AbstractBlockParser {
    private static final Pattern SPACES_REGEXP = Pattern.compile("^\\s{4,}.*$");

    private final StringBuilder value;
    private final String pluginId;
    private final IncludeBlock block;

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
                PluginsRegexp.INCLUDE_PLUGIN_PATTERN.matcher(line).matches()) {
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

            PluginsRegexp.IdAndParams idAndParams = PluginsRegexp.parseIncludePlugin(line);
            if (idAndParams != null) {
                return BlockStart.of(new IncludeBlockParser(idAndParams.getId(), idAndParams.getParams())).atIndex(state.getIndex());
            }

            return BlockStart.none();
        }
    }
}
