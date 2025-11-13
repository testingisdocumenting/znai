/*
 * Copyright 2022 znai maintainers
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

import React, { Component } from "react";
import { highlightSearchResultAndMaybeScroll } from "./searchResultHighlighter.ts";

class SearchPreview extends Component {
  componentDidMount() {
    this.highlight();
  }

  componentDidUpdate(_prevProp, _prevState) {
    this.highlight();
  }

  render() {
    const { section, elementsLibrary } = this.props;
    const key = section.id + "#" + section.title;
    return (
      <div key={key} className="znai-search-result-preview" ref={(dom) => (this.dom = dom)}>
        <elementsLibrary.DocElement {...this.props} content={section.content} isPartOfSearch={true} />
      </div>
    );
  }

  highlight() {
    const { snippets } = this.props;
    highlightSearchResultAndMaybeScroll(this.dom, snippets);
  }
}

export default SearchPreview;
