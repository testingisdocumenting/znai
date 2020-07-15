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
import './NotAuthorizedScreen.css';

interface Props {
  docId: string;
  authorizationRequestMessage?: string;
  authorizationRequestLink?: string;
  allowedGroups: string[];
}

export function NotAuthorizedScreen({docId, authorizationRequestLink, authorizationRequestMessage, allowedGroups}: Props) {
  return (
    <div className="znai-not-authorized-screen">
      <div className="znai-not-authorized-message-box">
        <div className="znai-not-authorized-message">
          You are not authorized to view documentation with id
          <span className="znai-not-authorized-doc-id"> {docId}</span>
        </div>

        <div className="znai-not-authorized-allowed-groups-message">
          Following groups are allowed to view this documentation:
          <span className="znai-not-authorized-groups"> {allowedGroups.join(", ")}</span>
        </div>

        {authorizationRequestLink && authorizationRequestMessage && <div className="znai-not-authorized-link-message">
            <a href={authorizationRequestLink}>{authorizationRequestMessage}</a>
        </div>}
      </div>
    </div>
  )
}