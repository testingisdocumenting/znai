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

import React from 'react';
import { PresentationProps } from '../presentation/PresentationProps';

import { extractTextFromTokens } from './codeUtils';
import { repeatChar } from '../../utils/strings';

import './SnippetOptionallyScrollablePart.css';

interface Props extends PresentationProps {
  linesOfCode: any[];
  numberOfVisibleLines: number;
  scrollToLineIdx: number;
  snippetComponent: React.ComponentType<any>;
}

interface State {
  height?: number;
}

/**
 * renders first N lines to measure the height so it can be scrolled later
 */
export class SnippetOptionallyScrollablePart extends React.Component<Props, State> {
  node?: HTMLDivElement;
  state = {
    height: undefined
  };

  render() {
    const {
      isPresentation,
      snippetComponent: SnippetComponent,
      linesOfCode,
      numberOfVisibleLines,
    } = this.props;

    if (!isPresentation || linesOfCode.length <= numberOfVisibleLines) {
      return <SnippetComponent {...this.props}/>;
    }

    const {height} = this.state;

    const linesOfCodeToRender = !!height ? linesOfCode : this.generateEmptyLongestLines();

    return (
      <div className="znai-snippet-scrollable-part" ref={this.saveNode} style={{height}}>
        <SnippetComponent {...this.props} linesOfCode={linesOfCodeToRender}/>
      </div>
    )
  }

  componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>, snapshot?: any) {
    this.scrollIfRequired();
  }

  componentDidMount() {
    const height = this.node?.offsetHeight;
    this.setState({height});

    this.scrollIfRequired();
  }

  generateEmptyLongestLines() {
    const {linesOfCode, numberOfVisibleLines} = this.props;

    const maxLength = linesOfCode.reduce((max, line) => Math.max(max, extractTextFromTokens(line).length), 0);
    const longLine = repeatChar(maxLength, ' ');

    const result = [];
    for (let idx = 0; idx < numberOfVisibleLines; idx++) {
      result.push([longLine]);
    }

    return result;
  }

  scrollIfRequired() {
    const {scrollToLineIdx} = this.props;
    const {height} = this.state;

    if (!height || scrollToLineIdx === undefined) {
      return;
    }

    const lineNodes = this.node ? this.node.querySelectorAll(".code-line") : undefined;

    if (!lineNodes) {
      return;
    }

    const maxScrollIdx = Math.min(scrollToLineIdx, lineNodes.length - 1)
    lineNodes[maxScrollIdx].scrollIntoView()
  }

  saveNode = (node: HTMLDivElement) => {
    this.node = node;
  }
}