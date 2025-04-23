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

import React from "react";

import { PresentationHeading } from "./PresentationHeading";

import { ElementsLibraryMap } from "./DocElement";
import "./Section.css";

interface Props {
  id: string;
  title: string;
  elementsLibrary: ElementsLibraryMap;
}

function Section({ id, title, ...props }: Props) {
  return (
    <div className="section" key={title}>
      <props.elementsLibrary.SectionTitle
        level={1}
        id={id}
        title={title || ""}
        showPresentationIcon={true}
        {...props}
      />
      <props.elementsLibrary.DocElement {...props} />
    </div>
  );
}

const PresentationTitle = ({ title }: Props) => {
  return <PresentationHeading level={1} title={title} />;
};

const presentationSectionHandler = {
  component: PresentationTitle,
  numberOfSlides: ({ title }: Props) => {
    return title.length === 0 ? 0 : 1;
  },
  slideInfoProvider: ({ title, id }: Props) => {
    return { sectionTitle: title, sectionId: id };
  },
};

export { Section, presentationSectionHandler };
