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

import React, { useEffect, useState } from "react";
import type { SupportMeta } from "../../structure/docMeta";
import { getSupportLinkAndTitlePromise } from "../../structure/docMeta";

export function Support() {
    const [support, setSupport] = useState<SupportMeta>();

    useEffect(() => {
        getSupportLinkAndTitlePromise().then(supportMeta => setSupport(supportMeta));
    }, []);

    return (
      <div className="page-support">
          {support && support.link ?
            <a href={support.link} target="_blank" rel="noopener noreferrer">{support.title}</a> : null
          }
      </div>
    )
}
