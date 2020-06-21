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

import PresentationRegistry from './PresentationRegistry';
import { PresentationSlideContainer } from './PresentationSlideContainer';
import { PresentationSlide } from './PresentationSlide';

import './SlidesLayout.css';

interface Props {
  presentationRegistry: PresentationRegistry;
  currentSlideIdx: number;
  maxScaleRatio: number;
}

export function SlidesLayout({
                               presentationRegistry,
                               currentSlideIdx,
                               maxScaleRatio
                             }: Props) {
  const slide = presentationRegistry.slideByIdx(currentSlideIdx);

  return (
    <RecursiveLayout presentationRegistry={presentationRegistry}
                     stickySlides={slide.stickySlides}
                     currentSlide={slide}
                     currentSlideIdx={currentSlideIdx}
                     maxScaleRatio={maxScaleRatio}
                     level={0}
                     flex={1}/>
  )
}

interface RecursiveLayoutProps {
  presentationRegistry: PresentationRegistry;
  currentSlideIdx: number;
  maxScaleRatio: number;
  stickySlides: PresentationSlide[];
  currentSlide: PresentationSlide;
  flex: number;
  level: number;
}

function RecursiveLayout({
                           presentationRegistry,
                           stickySlides,
                           currentSlide,
                           currentSlideIdx,
                           maxScaleRatio,
                           flex,
                           level
                         }: RecursiveLayoutProps) {
  if (stickySlides.length === 0) {
    return <SingleSlide presentationRegistry={presentationRegistry}
                        slide={currentSlide}
                        currentSlideIdx={currentSlideIdx}
                        flex={flex}
                        maxScaleRatio={maxScaleRatio}/>;
  }

  const firstSlide = stickySlides[0];
  const className = 'znai-slides-layout-recursive level' + level +
    (firstSlide.stickPlacement?.left ? ' left' : ' top');

  const flexOne = firstSlide.stickPlacement!.percentage;
  const flexTwo = 100 - flexOne;

  return (
    <div className={className} style={{flex}}>
      <SingleSlide presentationRegistry={presentationRegistry}
                   slide={firstSlide}
                   currentSlideIdx={currentSlideIdx}
                   flex={flexOne}
                   maxScaleRatio={maxScaleRatio}/>

      <RecursiveLayout presentationRegistry={presentationRegistry}
                       currentSlideIdx={currentSlideIdx}
                       currentSlide={currentSlide}
                       maxScaleRatio={maxScaleRatio}
                       flex={flexTwo}
                       level={level + 1}
                       stickySlides={stickySlides.slice(1)}/>
    </div>
  )
}

interface SingleSlideProps {
  presentationRegistry: PresentationRegistry;
  slide: PresentationSlide;
  currentSlideIdx: number;
  maxScaleRatio: number;
  flex: number;
}

function SingleSlide({
                       presentationRegistry,
                       slide,
                       currentSlideIdx,
                       flex,
                       maxScaleRatio
                     }: SingleSlideProps) {
  return (
    <PresentationSlideContainer key={slide.componentIdx}
                                slideIdx={currentSlideIdx}
                                maxScaleRatio={maxScaleRatio}
                                isCentered={isSlideCentered(slide.info)}
                                isScaled={isSlideScaled(slide.info)}
                                isPadded={isSlidePadded(slide.info)}
                                flex={flex}
                                render={renderSlideContent}/>
  )

  function renderSlideContent() {
    return presentationRegistry.renderSlide(slide)
  }
}


function isSlideCentered(slideInfo: any) {
  return slideInfoBooleanValue(slideInfo, 'isSlideCentered', true)
}

function isSlidePadded(slideInfo: any) {
  return slideInfoBooleanValue(slideInfo, 'isSlidePadded', true)
}

function isSlideScaled(slideInfo: any) {
  return slideInfoBooleanValue(slideInfo, 'isSlideScaled', true)
}

function slideInfoBooleanValue(info: any, key: string, defaultValue: boolean) {
  const value = info[key];

  return value === undefined ?
    defaultValue :
    value;
}
