import * as React from "react"
import * as CodeMirror from "codemirror/addon/runmode/runmode.node"

import "codemirror/mode/groovy/groovy"
import "codemirror/mode/clike/clike"
import "codemirror/lib/codemirror.css"

class Snippet extends React.Component {
    render() {
        return (<div className="snippet cm-s-default">
            <pre ref={(dn) => this.runCodeMirror(dn, this.props.snippet)}/>
        </div>)
    }

    runCodeMirror(node, snippet) {
        CodeMirror.runMode(snippet, "groovy", updateNode);

        // TODO following snippet was copied from runmode browser version. nodejs version doesn't have it
        var col = 0;
        const tabSize = 4;
        function updateNode(text, style) {
            if (text === "\n") {
                node.appendChild(document.createElement("br"));
                col = 0;
                return;
            }

            console.log(text, style);

            var content = "";
            // replace tabs
            for (var pos = 0; ;) {
                var idx = text.indexOf("\t", pos);
                if (idx === -1) {
                    content += text.slice(pos);
                    col += text.length - pos;
                    break;
                } else {
                    col += idx - pos;
                    content += text.slice(pos, idx);
                    var size = tabSize - col % tabSize;
                    col += size;
                    for (var i = 0; i < size; ++i) content += " ";
                    pos = idx + 1;
                }
            }

            if (style) {
                var sp = node.appendChild(document.createElement("span"));
                sp.className = "cm-" + style.replace(/ +/g, " cm-");
                sp.appendChild(document.createTextNode(content));
            } else {
                node.appendChild(document.createTextNode(content));
            }
        }
    }
}

export default Snippet;
