/*
 * Copyright 2020 znai maintainers
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

import * as React from "react";

import ClipboardJS from "clipboard";

import { extractTextFromTokens } from "./codeUtils";

import { Icon } from "../icons/Icon";
import { SnippetOptionallyScrollablePart } from "./SnippetOptionallyScrollablePart";

import { Container } from "../container/Container";

import "./SnippetContainer.css";

class SnippetContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      displayCopied: false,
      collapsed: this.props.collapsed,
    };
  }

  render() {
    const { title, className, resultOutput, wide, isPresentation, noGap, noGapBorder, next, prev, anchorId } =
      this.props;
    const { collapsed } = this.state;
    const renderWide = wide && !isPresentation;

    const snippetClassName =
      "snippet-container" +
      (resultOutput ? " result-output" : "") +
      (className ? " " + className : "");

    return (
      <Container
        className={snippetClassName}
        wide={renderWide}
        title={title}
        anchorId={anchorId}
        collapsed={collapsed}
        onCollapseToggle={this.collapseToggle}
        additionalTitleClassNames="znai-snippet-container-title"
        next={next}
        prev={prev}
        noGap={noGap}
        noGapBorder={noGapBorder}
      >
        {this.renderSnippet()}
      </Container>
    );
  }

  collapseToggle = () => {
    this.setState((prev) => ({ collapsed: !prev.collapsed }));
  };

  renderSnippet() {
    return (
      <div className={this.snippetClassName}>
        <SnippetOptionallyScrollablePart {...this.props} />
        {this.renderCopyToClipboard()}
      </div>
    );
  }

  renderCopyToClipboard() {
    const { isPresentation } = this.props;
    const { displayCopied } = this.state;

    if (isPresentation) {
      return null;
    }

    const className = "snippet-copy-to-clipboard " + (displayCopied ? "copied" : "copy");

    return (
      <div className={className} ref={this.saveCopyToClipboardNode}>
        <Icon id="copy" />
      </div>
    );
  }

  get snippetClassName() {
    const { title } = this.props;
    const { collapsed } = this.state;
    return "snippet" + (title ? " with-title" : "") + (collapsed ? " collapsed" : "");
  }

  saveCopyToClipboardNode = (node) => {
    this.copyToClipboardNode = node;
  };

  componentDidMount() {
    this.setupClipboard();
  }

  componentDidUpdate(prevProps, _prevState, _snapshot) {
    if (prevProps.collapsed !== this.props.collapsed) {
      this.setState({ collapsed: this.props.collapsed });
    }
  }

  componentWillUnmount() {
    this.clearTimer();
    this.destroyClipboard();
  }

  setupClipboard() {
    if (!this.copyToClipboardNode) {
      return;
    }

    this.clipboard = new ClipboardJS(this.copyToClipboardNode, {
      text: () => {
        const { linesOfCode, tokensForClipboardProvider } = this.props;
        this.setState({ displayCopied: true });
        this.startRemoveFeedbackTimer();

        return extractTextFromTokens(tokensToUse());

        function tokensToUse() {
          if (tokensForClipboardProvider) {
            return tokensForClipboardProvider();
          }

          return linesOfCode.reduce((acc, curr) => acc.concat(curr).concat("\n"), []);
        }
      },
    });
  }

  destroyClipboard() {
    if (this.clipboard) {
      this.clipboard.destroy();
    }
  }

  startRemoveFeedbackTimer() {
    this.removeFeedbackTimer = setTimeout(() => {
      this.setState({ displayCopied: false });
    }, 200);
  }

  clearTimer() {
    if (this.removeFeedbackTimer) {
      clearTimeout(this.removeFeedbackTimer);
    }
  }
}

export default SnippetContainer;
