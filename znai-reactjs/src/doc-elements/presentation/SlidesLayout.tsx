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

import type PresentationRegistry from './PresentationRegistry';
import { PresentationSlideContainer } from './PresentationSlideContainer';
import type { PresentationSlide } from './PresentationSlide';

import type { PresentationDimension, SlideAreaDimension } from './PresentationDimensions';

import './SlidesLayout.css';

interface Props {
  presentationRegistry: PresentationRegistry;
  currentSlideIdx: number;
  maxScaleRatio: number;
  presentationArea: PresentationDimension;
}

export function SlidesLayout({
                               presentationRegistry,
                               currentSlideIdx,
                               maxScaleRatio,
                               presentationArea
                             }: Props) {
  const slide = presentationRegistry.slideByIdx(currentSlideIdx);

  return (
    <RecursiveLayout presentationRegistry={presentationRegistry}
                     stickySlides={slide.stickySlides}
                     currentSlide={slide}
                     currentSlideIdx={currentSlideIdx}
                     maxScaleRatio={maxScaleRatio}
                     level={0}
                     presentationArea={presentationArea}
                     slideArea={{widthPercentage: 100, heightPercentage: 100}}
    />
  )
}

interface RecursiveLayoutProps {
  presentationRegistry: PresentationRegistry;
  currentSlideIdx: number;
  maxScaleRatio: number;
  stickySlides: PresentationSlide[];
  currentSlide: PresentationSlide;
  level: number;
  presentationArea: PresentationDimension;
  slideArea: SlideAreaDimension;
}

function RecursiveLayout({
                           presentationRegistry,
                           stickySlides,
                           currentSlide,
                           currentSlideIdx,
                           maxScaleRatio,
                           presentationArea,
                           slideArea,
                           level
                         }: RecursiveLayoutProps) {
  if (stickySlides.length === 0) {
    return <SingleSlide presentationRegistry={presentationRegistry}
                        slide={currentSlide}
                        isPresentationDisplayed={false}
                        currentSlideIdx={currentSlideIdx}
                        slideArea={slideArea}
                        presentationArea={presentationArea}
                        maxScaleRatio={maxScaleRatio}/>;
  }

  const firstSlide = stickySlides[0];
  const className = 'znai-slides-layout-recursive level' + level +
    (firstSlide.stickPlacement?.left ? ' left' : ' top');

  const percentageOne = firstSlide.stickPlacement!.percentage;
  const percentageTwo = 100 - percentageOne;

  const slideAreaOne = calcNewSlideArea(slideArea, percentageOne, !!firstSlide.stickPlacement?.left)
  const slideAreaTwo = calcNewSlideArea(slideArea, percentageTwo, !!firstSlide.stickPlacement?.left)

  return (
    <div className={className}>
      <SingleSlide presentationRegistry={presentationRegistry}
                   slide={firstSlide}
                   isPresentationDisplayed={true}
                   currentSlideIdx={currentSlideIdx}
                   presentationArea={presentationArea}
                   slideArea={slideAreaOne}
                   maxScaleRatio={maxScaleRatio}/>

      <RecursiveLayout presentationRegistry={presentationRegistry}
                       currentSlideIdx={currentSlideIdx}
                       currentSlide={currentSlide}
                       maxScaleRatio={maxScaleRatio}
                       level={level + 1}
                       presentationArea={presentationArea}
                       slideArea={slideAreaTwo}
                       stickySlides={stickySlides.slice(1)}/>
    </div>
  )

  function calcNewSlideArea(area: SlideAreaDimension, percentage: number, left: boolean): SlideAreaDimension {
    return left ?
      {
        widthPercentage:  area.widthPercentage * percentage / 100.0,
        heightPercentage: area.heightPercentage
      } :
      {
        widthPercentage: area.widthPercentage,
        heightPercentage: area.heightPercentage * percentage / 100.0
      }
  }
}

interface SingleSlideProps {
  presentationRegistry: PresentationRegistry;
  slide: PresentationSlide;
  isPresentationDisplayed: boolean; // e.g. sticky slide that has already played its animation
  currentSlideIdx: number;
  maxScaleRatio: number;
  presentationArea: PresentationDimension;
  slideArea: SlideAreaDimension;
}

function SingleSlide({
                       presentationRegistry,
                       slide,
                       isPresentationDisplayed,
                       currentSlideIdx,
                       maxScaleRatio,
                       presentationArea,
                       slideArea
                     }: SingleSlideProps) {
  return (
    <PresentationSlideContainer key={slide.componentIdx}
                                slideIdx={currentSlideIdx}
                                maxScaleRatio={maxScaleRatio}
                                isCentered={isSlideCentered(slide.info)}
                                isScaled={isSlideScaled(slide.info)}
                                isPadded={isSlidePadded(slide.info)}
                                presentationArea={presentationArea}
                                slideArea={slideArea}
                                render={renderSlideContent}/>
  )

  function renderSlideContent() {
    return presentationRegistry.renderSlide(slide, {isPresentationDisplayed})
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
