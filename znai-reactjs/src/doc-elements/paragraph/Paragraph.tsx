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

import { paragraphStartsWith, removeSuffixFromParagraph } from "./paragraphUtils";

import { elementMetaValue } from "../meta/meta";

import { DocElementContent, DocElementProps } from "../default-elements/DocElement";
import { AttentionBlock } from "./AttentionBlock";

import "./Paragraph.css";

const noteSuffix = "Note:";
const warningSuffix = "Warning:";
const questionSuffix = "Question:";
const exerciseSuffix = "Exercise:";
const avoidSuffix = "Avoid:";
const dontSuffix = "Don't:";
const doNotSuffix = "Do not:";
const recommendationSuffix = "Recommendation:";
const tipSuffix = "Tip:";

const allSuffixes = [
  noteSuffix,
  warningSuffix,
  questionSuffix,
  exerciseSuffix,
  avoidSuffix,
  dontSuffix,
  doNotSuffix,
  recommendationSuffix,
  tipSuffix,
];

const DefaultParagraph = (props: DocElementProps) => {
  return (
    <div className="paragraph content-block">
      <props.elementsLibrary.DocElement {...props} />
    </div>
  );
};

interface WithAttentionProps extends DocElementProps {
  attentionType: string;
  suffix: string;
  icon: string;
}

const ParagraphWithAttention = ({ attentionType, suffix, icon, ...props }: WithAttentionProps) => {
  const contentWithRemovedSuffix = removeSuffixFromParagraph(props.content, suffix);
  const suffixColonIdx = suffix.indexOf(":");

  const iconLabel = suffix.substr(0, suffixColonIdx);

  return (
    <AttentionBlock
      attentionType={attentionType}
      label={iconLabel}
      content={contentWithRemovedSuffix}
      elementsLibrary={props.elementsLibrary}
    />
  );
};

const NoteParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="note" suffix={noteSuffix} icon="info" {...props} />
);
const WarningParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="warning" suffix={warningSuffix} icon="alert-triangle" {...props} />
);
const QuestionParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="question" suffix={questionSuffix} icon="help-circle" {...props} />
);
const ExerciseParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="question" suffix={exerciseSuffix} icon="help-circle" {...props} />
);
const AvoidParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="avoid" suffix={avoidSuffix} icon="x-octagon" {...props} />
);
const DontParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="avoid" suffix={dontSuffix} icon="x-octagon" {...props} />
);
const DoNotParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="avoid" suffix={doNotSuffix} icon="x-octagon" {...props} />
);
const RecommendationParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="recommendation" suffix={recommendationSuffix} icon="check-circle" {...props} />
);
const TipParagraph = (props: DocElementProps) => (
  <ParagraphWithAttention attentionType="recommendation" suffix={tipSuffix} icon="check-circle" {...props} />
);

const Paragraph = (props: DocElementProps) => {
  if (paragraphStartsWith(props.content, noteSuffix)) {
    return <NoteParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, warningSuffix)) {
    return <WarningParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, questionSuffix)) {
    return <QuestionParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, exerciseSuffix)) {
    return <ExerciseParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, avoidSuffix)) {
    return <AvoidParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, dontSuffix)) {
    return <DontParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, doNotSuffix)) {
    return <DoNotParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, recommendationSuffix)) {
    return <RecommendationParagraph {...props} />;
  }

  if (paragraphStartsWith(props.content, tipSuffix)) {
    return <TipParagraph {...props} />;
  }

  return <DefaultParagraph {...props} />;
};

const PresentationParagraph = (props: DocElementProps) => {
  const className = "znai-presentation-paragraph-wrapper" + buildAdditionalClassName();

  return (
    <div className={className}>
      <Paragraph {...props} />
    </div>
  );

  function buildAdditionalClassName() {
    if (allSuffixes.some((suffix) => paragraphStartsWith(props.content, suffix))) {
      return "";
    }

    if (isParagraphPresentationForced(props.content!)) {
      return " znai-presentation-paragraph-default";
    }

    return "";
  }
};

const presentationParagraph = {
  component: PresentationParagraph,
  numberOfSlides: ({ content }: { content: DocElementContent }) => {
    const presentParagraph = isParagraphPresentationForced(content);
    return presentParagraph ||
      paragraphStartsWith(content, questionSuffix) ||
      paragraphStartsWith(content, exerciseSuffix)
      ? 1
      : 0;
  },
};

function isParagraphPresentationForced(content: DocElementContent) {
  if (content.length === 0) {
    return false;
  }

  return elementMetaValue(content[0], "presentationParagraph") === "default";
}

export { Paragraph, presentationParagraph };
