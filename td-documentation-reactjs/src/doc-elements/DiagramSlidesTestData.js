const data = {
  "slides": [
    {
      "ids": [
        "nginx-\u003eaccess_log"
      ],
      "content": [
        {
          "type": "Paragraph",
          "content": [
            {
              "text": "NGINX stores every access to access.log. It also kerberizes the request.",
              "type": "SimpleText"
            }
          ]
        }
      ]
    },
    {
      "ids": [
        "access_log-\u003elog_stash_log"
      ],
      "content": [
        {
          "type": "Paragraph",
          "content": [
            {
              "text": "Log Stash runs on the same machine and consumes access.log lines.",
              "type": "SimpleText"
            },
            {
              "type": "SoftLineBreak"
            },
            {
              "text": "As long as lines contain required information it gets send to Kafka",
              "type": "SimpleText"
            }
          ]
        }
      ]
    }
  ],
  "diagram": {
    "id": "demo",
    "svg": "\u003c?xml version\u003d\"1.0\" encoding\u003d\"UTF-8\" standalone\u003d\"no\"?\u003e\n\u003c!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"\u003e\n\u003c!-- Generated by graphviz version 2.38.0 (20140413.2041)\n --\u003e\n\u003c!-- Title: Simple Pages: 1 --\u003e\n\u003csvg width\u003d\"477pt\" height\u003d\"295pt\"\n viewBox\u003d\"0.00 0.00 477.00 295.00\" xmlns\u003d\"http://www.w3.org/2000/svg\" xmlns:xlink\u003d\"http://www.w3.org/1999/xlink\"\u003e\n\u003cg id\u003d\"graph0\" class\u003d\"graph\" transform\u003d\"scale(1 1) rotate(0) translate(4 291)\"\u003e\n\u003ctitle\u003eSimple\u003c/title\u003e\n\u003cpolygon fill\u003d\"white\" stroke\u003d\"none\" points\u003d\"-4,4 -4,-291 473,-291 473,4 -4,4\"/\u003e\n\u003c!-- nginx --\u003e\n\u003cg id\u003d\"node1\" class\u003d\"node\"\u003e\u003ctitle\u003enginx\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"3.44824,-235 3.44824,-271 64.5518,-271 64.5518,-235 3.44824,-235\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"34\" y\u003d\"-248.8\" font-family\u003d\"Times,serif\" font-size\u003d\"14.00\"\u003eNGINX\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- access_log --\u003e\n\u003cg id\u003d\"node4\" class\u003d\"node\"\u003e\u003ctitle\u003eaccess_log\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"68,-183 0,-183 0,-115 68,-115 68,-183\"/\u003e\n\u003c/g\u003e\n\u003c!-- nginx\u0026#45;\u0026gt;access_log --\u003e\n\u003cg id\u003d\"edge1\" class\u003d\"edge\"\u003e\u003ctitle\u003enginx\u0026#45;\u0026gt;access_log\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"black\" d\u003d\"M34,-234.697C34,-223.354 34,-207.986 34,-193.454\"/\u003e\n\u003cpolygon fill\u003d\"black\" stroke\u003d\"black\" points\u003d\"37.5001,-193.09 34,-183.09 30.5001,-193.09 37.5001,-193.09\"/\u003e\n\u003c/g\u003e\n\u003c!-- log_stash_log --\u003e\n\u003cg id\u003d\"node2\" class\u003d\"node\"\u003e\u003ctitle\u003elog_stash_log\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"7,-21.5 7,-57.5 61,-57.5 61,-21.5 7,-21.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"33.8638\" y\u003d\"-35.3\" font-family\u003d\"Times,serif\" font-size\u003d\"14.00\"\u003eLS file\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- kafka --\u003e\n\u003cg id\u003d\"node8\" class\u003d\"node\"\u003e\u003ctitle\u003ekafka\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"133,-21.5 133,-57.5 187,-57.5 187,-21.5 133,-21.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"159.545\" y\u003d\"-35.3\" font-family\u003d\"Times,serif\" font-size\u003d\"14.00\"\u003ekafka\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- log_stash_log\u0026#45;\u0026gt;kafka --\u003e\n\u003cg id\u003d\"edge3\" class\u003d\"edge\"\u003e\u003ctitle\u003elog_stash_log\u0026#45;\u0026gt;kafka\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"black\" d\u003d\"M61.0703,-39.5C81.6605,-39.5 102.251,-39.5 122.841,-39.5\"/\u003e\n\u003cpolygon fill\u003d\"black\" stroke\u003d\"black\" points\u003d\"122.949,-43.0001 132.949,-39.5 122.949,-36.0001 122.949,-43.0001\"/\u003e\n\u003c/g\u003e\n\u003c!-- log_stash_elastic --\u003e\n\u003cg id\u003d\"node3\" class\u003d\"node\"\u003e\u003ctitle\u003elog_stash_elastic\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"258.536,-21.5 258.536,-57.5 325.464,-57.5 325.464,-21.5 258.536,-21.5\"/\u003e\n\u003ctext text-anchor\u003d\"middle\" x\u003d\"292\" y\u003d\"-35.3\" font-family\u003d\"Times,serif\" font-size\u003d\"14.00\"\u003eLS kafka\u003c/text\u003e\n\u003c/g\u003e\n\u003c!-- elastic --\u003e\n\u003cg id\u003d\"node5\" class\u003d\"node\"\u003e\u003ctitle\u003eelastic\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"469,-79 397,-79 397,-0 469,-0 469,-79\"/\u003e\n\u003c/g\u003e\n\u003c!-- log_stash_elastic\u0026#45;\u0026gt;elastic --\u003e\n\u003cg id\u003d\"edge5\" class\u003d\"edge\"\u003e\u003ctitle\u003elog_stash_elastic\u0026#45;\u0026gt;elastic\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"black\" d\u003d\"M325.598,-39.5C346.028,-39.5 366.459,-39.5 386.89,-39.5\"/\u003e\n\u003cpolygon fill\u003d\"black\" stroke\u003d\"black\" points\u003d\"386.92,-43.0001 396.92,-39.5 386.919,-36.0001 386.92,-43.0001\"/\u003e\n\u003c/g\u003e\n\u003c!-- access_log\u0026#45;\u0026gt;log_stash_log --\u003e\n\u003cg id\u003d\"edge2\" class\u003d\"edge\"\u003e\u003ctitle\u003eaccess_log\u0026#45;\u0026gt;log_stash_log\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"black\" d\u003d\"M34,-114.869C34,-99.8262 34,-82.2434 34,-67.952\"/\u003e\n\u003cpolygon fill\u003d\"black\" stroke\u003d\"black\" points\u003d\"37.5001,-67.7614 34,-57.7615 30.5001,-67.7615 37.5001,-67.7614\"/\u003e\n\u003c/g\u003e\n\u003c!-- web_dashboard --\u003e\n\u003cg id\u003d\"node6\" class\u003d\"node\"\u003e\u003ctitle\u003eweb_dashboard\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"462,-178 404,-178 404,-120 462,-120 462,-178\"/\u003e\n\u003c/g\u003e\n\u003c!-- web_dashboard\u0026#45;\u0026gt;elastic --\u003e\n\u003cg id\u003d\"edge7\" class\u003d\"edge\"\u003e\u003ctitle\u003eweb_dashboard\u0026#45;\u0026gt;elastic\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"black\" d\u003d\"M433,-119.756C433,-110.424 433,-99.7582 433,-89.3401\"/\u003e\n\u003cpolygon fill\u003d\"black\" stroke\u003d\"black\" points\u003d\"436.5,-89.0632 433,-79.0632 429.5,-89.0633 436.5,-89.0632\"/\u003e\n\u003c/g\u003e\n\u003c!-- client --\u003e\n\u003cg id\u003d\"node7\" class\u003d\"node\"\u003e\u003ctitle\u003eclient\u003c/title\u003e\n\u003cpolygon fill\u003d\"none\" stroke\u003d\"black\" points\u003d\"467,-287 399,-287 399,-219 467,-219 467,-287\"/\u003e\n\u003c/g\u003e\n\u003c!-- client\u0026#45;\u0026gt;web_dashboard --\u003e\n\u003cg id\u003d\"edge6\" class\u003d\"edge\"\u003e\u003ctitle\u003eclient\u0026#45;\u0026gt;web_dashboard\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"black\" d\u003d\"M433,-218.884C433,-209.126 433,-198.369 433,-188.314\"/\u003e\n\u003cpolygon fill\u003d\"black\" stroke\u003d\"black\" points\u003d\"436.5,-188.162 433,-178.162 429.5,-188.162 436.5,-188.162\"/\u003e\n\u003c/g\u003e\n\u003c!-- kafka\u0026#45;\u0026gt;log_stash_elastic --\u003e\n\u003cg id\u003d\"edge4\" class\u003d\"edge\"\u003e\u003ctitle\u003ekafka\u0026#45;\u0026gt;log_stash_elastic\u003c/title\u003e\n\u003cpath fill\u003d\"none\" stroke\u003d\"black\" d\u003d\"M187.07,-39.5C207.5,-39.5 227.93,-39.5 248.36,-39.5\"/\u003e\n\u003cpolygon fill\u003d\"black\" stroke\u003d\"black\" points\u003d\"248.39,-43.0001 258.39,-39.5 248.39,-36.0001 248.39,-43.0001\"/\u003e\n\u003c/g\u003e\n\u003c/g\u003e\n\u003c/svg\u003e\n",
    "stylesByNodeId": {
      "nginx": [
        "b"
      ],
      "log_stash_log": [
        "b"
      ],
      "log_stash_elastic": [
        "b"
      ],
      "access_log": [
        "documents",
        "b"
      ],
      "elastic": [
        "database",
        "a"
      ],
      "web_dashboard": [
        "world",
        "a"
      ],
      "client": [
        "client",
        "a"
      ]
    },
    "shapeSvgByStyleId": {
      "database": "\u003cg transform\u003d\"scale(0.07)\"\u003e\u003cg transform\u003d\"translate(-350, -500)\"\u003e\n    \u003cpath stroke-miterlimit\u003d\"10\" d\u003d\"M384,960C171.969,960,0,902.625,0,832c0-38.625,0-80.875,0-128\n\tc0-11.125,5.562-21.688,13.562-32C56.375,727.125,205.25,768,384,768s327.625-40.875,370.438-96c8,10.312,13.562,20.875,13.562,32\n\tc0,37.062,0,76.375,0,128C768,902.625,596,960,384,960z M384,704C171.969,704,0,646.625,0,576c0-38.656,0-80.845,0-128\n\tc0-6.781,2.562-13.375,6-19.906l0,0C7.938,424,10.5,419.969,13.562,416C56.375,471.094,205.25,512,384,512\n\ts327.625-40.906,370.438-96c3.062,3.969,5.625,8,7.562,12.094l0,0c3.438,6.531,6,13.125,6,19.906c0,37.062,0,76.344,0,128\n\tC768,646.625,596,704,384,704z M384,448C171.969,448,0,390.656,0,320c0-20.22,0-41.595,0-64c0-20.345,0-41.47,0-64\n\tC0,121.344,171.969,64,384,64c212,0,384,57.344,384,128c0,19.969,0,41.155,0,64c0,19.594,0,40.25,0,64C768,390.656,596,448,384,448z\n\t M384,128c-141.375,0-256,28.594-256,64c0,35.405,114.625,64,256,64s256-28.595,256-64C640,156.594,525.375,128,384,128z\"/\u003e\n\u003c/g\u003e\u003c/g\u003e",
      "world": "\u003cg transform\u003d\"scale(0.38)\"\u003e\n    \u003cg transform\u003d\"translate(-62 -62)\"\u003e\n        \u003cpath d\u003d\"M62,0c-5,0-9.8,0.6-14.4,1.7C32.5,5.3,19.5,14.4,11,26.8C4.1,36.8,0,48.9,0,62c0,34.2,27.8,62,62,62s62-27.8,62-62   S96.2,0,62,0z M87.6,103.5c-0.5,4-6.199,6-7.399,1.7c-2.101-7.9-7.7-5.601-7.5-13c0.2-5.601-11.3-1.2-14.3-5.7   c-3-4.4,5.3-8.7,0.8-14.2S45.7,74.4,34.9,70.7c-4.8-1.7-9.4-5.601-12.6-9.5C18,56,16.5,49.8,14.7,43.4c-0.2-0.6-0.3-1.3-0.5-1.9   C19.7,28.8,30,18.7,42.9,13.6c2.4,3.3,10.1,1.7,15,2.2c2.5,0.2,4.3,1,4.4,3.2c0,0.4-0.1,1-0.3,1.6c-1.5,4.1-7.1,11.3-3.6,14   c2.6,2,4.4-1.8,5.9,3.9c0.7,2.6,1,8-1.899,9.9C59.6,50.2,52.6,43,44,47c-4.7,2.3-5.4,13.1,1.1,13.7c3.8,0.3,6.8-7.2,10.4-3.6   c2.9,2.9,5.4,10.8,12.2,11.1c6,0.3,17.5-3.4,25.1,7.1c4.5,6.4-3.8,11.5-5.3,17.601C86.2,97.9,88,100.5,87.6,103.5z\"/\u003e\n    \u003c/g\u003e\n\u003c/g\u003e",
      "documents": "\u003cg transform\u003d\"scale(0.7)\"\u003e\n    \u003cg transform\u003d\"translate(-50, -50)\"\u003e\n        \u003cpolygon points\u003d\"13 28.9 50 7.81 87 28.9 87 71.1 50 92.19 13 71.1 13 28.9\"/\u003e\n        \u003cg\u003e\n            \u003cg\u003e\n                \u003crect x\u003d\"38.34\" y\u003d\"46.09\" width\u003d\"15.08\" height\u003d\"3.89\" fill\u003d\"#fff\"/\u003e\n                \u003crect x\u003d\"38.34\" y\u003d\"53.88\" width\u003d\"15.08\" height\u003d\"3.89\" fill\u003d\"#fff\"/\u003e\n                \u003crect x\u003d\"38.34\" y\u003d\"61.67\" width\u003d\"15.08\" height\u003d\"3.89\" fill\u003d\"#fff\"/\u003e\n            \u003c/g\u003e\n            \u003cg\u003e\n                \u003cpolygon points\u003d\"57.31 40.65 57.31 63.22 61.2 63.22 61.2 36.76 42.23 36.76 42.23 40.65 57.31 40.65\" fill\u003d\"#fff\"/\u003e\n                \u003cpath d\u003d\"M69,29H38.34v7.79H30.55V74.9H61.2V67.11H69ZM57.31,71H34.44V40.65H57.31Zm7.79-7.79H61.2V36.76h-19V32.86H65.1Z\" fill\u003d\"#fff\"/\u003e\n            \u003c/g\u003e\n        \u003c/g\u003e\n    \u003c/g\u003e\n\u003c/g\u003e",
      "client": "\u003cg transform\u003d\"scale(0.9)\"\u003e\n    \u003cg transform\u003d\"translate(-37, -30)\"\u003e\n        \u003cpath d\u003d\"M58.944,49.454H11.053c-2.379,0-4.313,1.932-4.313,4.316v3.479c0,2.387,1.934,4.319,4.313,4.319h47.892\n\t\tc2.386,0,4.315-1.933,4.315-4.319v-3.479C63.26,51.386,61.33,49.454,58.944,49.454z M57.035,57.238H39.129\n\t\tc-0.953,0-1.728-0.772-1.728-1.729c0-0.952,0.774-1.726,1.728-1.726h17.906c0.954,0,1.728,0.773,1.728,1.726\n\t\tC58.763,56.466,57.989,57.238,57.035,57.238z\"/\u003e\n        \u003cpath d\u003d\"M13.313,45.937h43.317c3.603,0,6.535-2.933,6.535-6.537V14.974c0-3.608-2.933-6.542-6.535-6.542H13.313\n\t\tc-3.604,0-6.534,2.934-6.534,6.542v24.426C6.779,43.004,9.71,45.937,13.313,45.937z M11.35,14.974c0-1.087,0.881-1.971,1.964-1.971\n\t\th43.317c1.083,0,1.964,0.884,1.964,1.971v24.426c0,1.084-0.881,1.967-1.964,1.967H13.313c-1.083,0-1.964-0.883-1.964-1.967V14.974z\n\t\t\"/\u003e\n    \u003c/g\u003e\n\u003c/g\u003e"
    },
    "isInvertedTextColorByStyleId": {}
  }
}

export default data