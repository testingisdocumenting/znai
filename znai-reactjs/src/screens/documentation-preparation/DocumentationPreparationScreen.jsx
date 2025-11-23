/*
 * Copyright 2025 znai maintainers
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

import DocumentationPreparation from "./DocumentationPreparation";
import { socketUrl } from "../../utils/socket";

import "./DocumentationPreparationScreen.css";

export class DocumentationPreparationScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = props;
  }

  render() {
    return (
      <div className="documentation-preparation-screen">
        <DocumentationPreparation {...this.state} />
      </div>
    );
  }

  componentDidMount() {
    this._connect();
  }

  componentWillUnmount() {
    this._disconnect();
  }

  _connect() {
    this.ws = new WebSocket(socketUrl("_doc-update/" + this.props.docId));

    this.ws.onopen = () => {};

    this.ws.onclose = () => {};

    this.ws.onmessage = (message) => {
      const data = JSON.parse(message.data);
      this._update(data);
    };
  }

  _disconnect() {
    this.ws.close();
  }

  _update({ message, keyValues, progress }) {
    this.setState({ statusMessage: message, keyValues: keyValues || [], progressPercent: progress });
    if (progress >= 100) {
      setTimeout(() => window.location.reload(), 100);
    }
  }
}
