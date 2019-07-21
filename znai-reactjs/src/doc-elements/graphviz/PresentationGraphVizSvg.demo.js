/*
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

import React from 'react'
import {createPresentationDemo} from '../demo-utils/PresentationDemo'

export function graphVizSvgPresentationDemo(registry) {
    registry
        .add('one at a time', createPresentationDemo([simpleDiagram()]))
        .add('all at once', createPresentationDemo([simpleDiagram({allAtOnce: true})]))
}

function simpleDiagram(meta) {
    return {
        meta,
        "diagram": {
            "shapeSvgByStyleId": {},
            "isInvertedTextColorByStyleId": {},
            "svg": "\u003c?xml version\u003d\"1.0\" encoding\u003d\"UTF-8\" standalone\u003d\"no\"?\u003e\n\u003c!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"\u003e\n\u003c!-- Generated by graphviz version 2.40.1 (20161225.0304)\n --\u003e\n\u003c!-- Title: Generated Pages: 1 --\u003e\n\u003csvg width\u003d\"275pt\" height\u003d\"197pt\"\n viewBox\u003d\"0.00 0.00 275.32 197.00\" xmlns\u003d\"http://www.w3.org/2000/svg\" xmlns:xlink\u003d\"http://www.w3.org/1999/xlink\"\u003e\n\u003cg id\u003d\"page0,1_graph0\" class\u003d\"graph\" transform\u003d\"scale(1 1) rotate(0) translate(4 193)\"\u003e\n\u003ctitle\u003eGenerated\u003c/title\u003e\n\u003cpolygon fill\u003d\"#ffffff\" stroke\u003d\"transparent\" points\u003d\"-4,4 -4,-193 271.3198,-193 271.3198,4 -4,4\"/\u003e\n\u003c!-- n1 --\u003e\n\u003cg id\u003d\"node1\" class\u003d\"node\"\u003e\n\u003ctitle\u003en1\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"#000000\" points\u003d\"53.0591,-150.5 53.0591,-188.5 122.2065,-188.5 122.2065,-150.5 53.0591,-150.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"87.6328\" y\u003d\"-166.5\" font-family\u003d\"Helvetica,sans-Serif\" font-size\u003d\"10.00\" fill\u003d\"#000000\"\u003einput one\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- n3 --\u003e\n\u003cg id\u003d\"node3\" class\u003d\"node\"\u003e\n\u003ctitle\u003en3\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"#000000\" points\u003d\"94.6797,-75.5 94.6797,-113.5 166.5859,-113.5 166.5859,-75.5 94.6797,-75.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"130.6328\" y\u003d\"-91.5\" font-family\u003d\"Helvetica,sans-Serif\" font-size\u003d\"10.00\" fill\u003d\"#000000\"\u003eprocessor\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- n1\u0026#45;\u0026gt;n3 --\u003e\n\u003cg id\u003d\"edge1\" class\u003d\"edge\"\u003e\n\u003ctitle\u003en1\u0026#45;\u0026gt;n3\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"#000000\" d\u003d\"M98.7064,-150.1856C103.5214,-141.7873 109.2571,-131.7833 114.5297,-122.5868\"/\u003e\n\u003cpolygon fill\u003d\"#000000\" stroke\u003d\"#000000\" points\u003d\"117.6592,-124.1651 119.5968,-113.7489 111.5865,-120.6834 117.6592,-124.1651\"/\u003e\n\u003c/g\u003e\n\u003c!-- n2 --\u003e\n\u003cg id\u003d\"node2\" class\u003d\"node\"\u003e\n\u003ctitle\u003en2\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"#000000\" points\u003d\"140.6206,-150.5 140.6206,-188.5 208.645,-188.5 208.645,-150.5 140.6206,-150.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"174.6328\" y\u003d\"-166.5\" font-family\u003d\"Helvetica,sans-Serif\" font-size\u003d\"10.00\" fill\u003d\"#000000\"\u003einput two\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- n2\u0026#45;\u0026gt;n3 --\u003e\n\u003cg id\u003d\"edge2\" class\u003d\"edge\"\u003e\n\u003ctitle\u003en2\u0026#45;\u0026gt;n3\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"#000000\" d\u003d\"M163.3017,-150.1856C158.3747,-141.7873 152.5057,-131.7833 147.1104,-122.5868\"/\u003e\n\u003cpolygon fill\u003d\"#000000\" stroke\u003d\"#000000\" points\u003d\"150.0045,-120.6031 141.9255,-113.7489 143.9669,-124.1452 150.0045,-120.6031\"/\u003e\n\u003c/g\u003e\n\u003c!-- n4 --\u003e\n\u003cg id\u003d\"node4\" class\u003d\"node\"\u003e\n\u003ctitle\u003en4\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"#000000\" points\u003d\"0,-.5 0,-38.5 75.2656,-38.5 75.2656,-.5 0,-.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"37.6328\" y\u003d\"-16.5\" font-family\u003d\"Helvetica,sans-Serif\" font-size\u003d\"10.00\" fill\u003d\"#000000\"\u003eoutput one\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- n3\u0026#45;\u0026gt;n4 --\u003e\n\u003cg id\u003d\"edge3\" class\u003d\"edge\"\u003e\n\u003ctitle\u003en3\u0026#45;\u0026gt;n4\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"#000000\" d\u003d\"M106.683,-75.1856C95.4012,-66.0874 81.7828,-55.1048 69.6322,-45.3059\"/\u003e\n\u003cpolygon fill\u003d\"#000000\" stroke\u003d\"#000000\" points\u003d\"71.4828,-42.302 61.5015,-38.7489 67.0885,-47.7509 71.4828,-42.302\"/\u003e\n\u003c/g\u003e\n\u003c!-- n5 --\u003e\n\u003cg id\u003d\"node5\" class\u003d\"node\"\u003e\n\u003ctitle\u003en5\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"#000000\" points\u003d\"93.5615,-.5 93.5615,-38.5 167.7041,-38.5 167.7041,-.5 93.5615,-.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"130.6328\" y\u003d\"-16.5\" font-family\u003d\"Helvetica,sans-Serif\" font-size\u003d\"10.00\" fill\u003d\"#000000\"\u003eoutput two\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- n3\u0026#45;\u0026gt;n5 --\u003e\n\u003cg id\u003d\"edge4\" class\u003d\"edge\"\u003e\n\u003ctitle\u003en3\u0026#45;\u0026gt;n5\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"#000000\" d\u003d\"M130.6328,-75.1856C130.6328,-67.2247 130.6328,-57.821 130.6328,-49.03\"/\u003e\n\u003cpolygon fill\u003d\"#000000\" stroke\u003d\"#000000\" points\u003d\"134.1329,-48.7489 130.6328,-38.7489 127.1329,-48.7489 134.1329,-48.7489\"/\u003e\n\u003c/g\u003e\n\u003c!-- n6 --\u003e\n\u003cg id\u003d\"node6\" class\u003d\"node\"\u003e\n\u003ctitle\u003en6\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"#000000\" points\u003d\"185.9458,-.5 185.9458,-38.5 267.3198,-38.5 267.3198,-.5 185.9458,-.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"226.6328\" y\u003d\"-16.5\" font-family\u003d\"Helvetica,sans-Serif\" font-size\u003d\"10.00\" fill\u003d\"#000000\"\u003eoutput three\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- n3\u0026#45;\u0026gt;n6 --\u003e\n\u003cg id\u003d\"edge5\" class\u003d\"edge\"\u003e\n\u003ctitle\u003en3\u0026#45;\u0026gt;n6\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"#000000\" d\u003d\"M155.1061,-75.3803C166.9542,-66.1239 181.343,-54.8826 194.0999,-44.9163\"/\u003e\n\u003cpolygon fill\u003d\"#000000\" stroke\u003d\"#000000\" points\u003d\"196.4788,-47.4994 202.2043,-38.5848 192.1692,-41.9832 196.4788,-47.4994\"/\u003e\n\u003c/g\u003e\n\u003c/g\u003e\n\u003c/svg\u003e\n",
            "stylesByNodeId": {},
            "id": "dag17"
        },
        "colors": {
            "a": {
                "line": "#123752",
                "fill": "#708EA4",
                "text": "#eeeeee",
                "textInverse": "#123752"
            },
            "b": {
                "line": "#AA8E39",
                "fill": "#FFEAAA",
                "text": "#806515",
                "textInverse": "#AA8E39"
            },
            "c": {
                "line": "#306E12",
                "fill": "#519331",
                "text": "#ABDD93",
                "textInverse": "#306E12"
            },
            "h": {
                "line": "#888",
                "fill": "#fff",
                "text": "#888",
                "textInverse": "#888"
            }
        },
        "idsToHighlight": ['n2', 'n3'],
        "wide": false,
        "urls": {},
        "type": "GraphVizDiagram"
    }
}

