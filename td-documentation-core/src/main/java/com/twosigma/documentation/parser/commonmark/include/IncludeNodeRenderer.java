package com.twosigma.documentation.parser.commonmark.include;///*
// * Copyright (c) 2016 Two Sigma Investments, LP.
// * All Rights Reserved
// *
// * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
// * Two Sigma Investments, LP.
// *
// * The copyright notice above does not evidence any
// * actual or intended publication of such source code.
// */
//
//package com.twosigma.documentation.parser.commonmark.include;
//
//import java.util.Collections;
//import java.util.Set;
//
//import org.commonmark.html.HtmlWriter;
//import org.commonmark.html.renderer.NodeRenderer;
//import org.commonmark.html.renderer.NodeRendererContext;
//import org.commonmark.node.Node;
//
//import com.twosigma.documentation.extensions.include.IncludeParams;
//import com.twosigma.documentation.extensions.include.IncludePlugin;
//import com.twosigma.documentation.extensions.include.IncludePlugins;
//import com.twosigma.documentation.extensions.include.IncludeResult;
//import com.twosigma.documentation.extensions.PluginsListener;
//
//
///**
// * @author mykola
// */
//public class IncludeNodeRenderer implements NodeRenderer {
//    private final HtmlWriter htmlWriter;
//    private PluginsListener pluginsListener;
//
//    public IncludeNodeRenderer(NodeRendererContext context, final PluginsListener pluginsListener) {
//        htmlWriter = context.getHtmlWriter();
//        this.pluginsListener = pluginsListener;
//    }
//
//    @Override
//    public Set<Class<? extends Node>> getNodeTypes() {
//        return Collections.singleton(IncludeNode.class);
//    }
//
//    @Override
//    public void render(final Node node) {
//        renderInclude((IncludeNode) node);
//    }
//
//    private void renderInclude(final IncludeNode node) {
//        final IncludePlugin includePlugin = IncludePlugins.byId(node.getId());
//        final IncludeResult includeResult = includePlugin.process(new IncludeParams(node.getValue(), Collections.emptyMap()));
//
//        htmlWriter.line();
//        htmlWriter.raw(includeResult.getHtml());
//
//        if (pluginsListener != null) {
//            pluginsListener.onInclude(includePlugin, includeResult);
//        }
//    }
//}
