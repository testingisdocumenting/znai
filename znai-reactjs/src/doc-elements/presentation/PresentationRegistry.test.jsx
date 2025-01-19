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

import React from "react";

import PresentationRegistry from "./PresentationRegistry";
import {presentationSectionHandler} from "../default-elements/Section";

const elementsLibrary = {
    'Dummy': () => <div/>,
    'DummyTriple': () => <div/>
}

const presentationElementHandlers = {
    'Dummy': {
        component: () => <div/>,
        numberOfSlides: () => 1
    },
    'DummyTriple': {
        component: () => <div/>,
        numberOfSlides: () => 3
    },
    'NonSlide': {
        component: () => <div/>,
        numberOfSlides: () => 0
    },
    'Section': presentationSectionHandler
}

describe('PresentationRegistry', () => {
    describe('slide idx by title', () => {
        it('should find a slide idx by page section id', () => {
            const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, [
                {
                    type: 'Section',
                    title : 'Title One',
                    id : 'title-one',
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code1",
                },
                {
                    type: 'Section',
                    title : 'Title Two',
                    id : 'title-two',
                },
                {
                    type: 'Dummy',
                    lang: 'java',
                    snippet: "code2",
                    meta: {
                        stickySlide: 'top 30%'
                    }
                }
            ])

            expect(registry.findSlideIdxBySectionId('title-one')).toEqual(0)
            expect(registry.findSlideIdxBySectionId('title-two')).toEqual(2)

            expect(registry.findSlideIdxBySectionId(undefined)).toEqual(0)
            expect(registry.findSlideIdxBySectionId('')).toEqual(0)
            expect(registry.findSlideIdxBySectionId('non-existing')).toEqual(0)
        })
    })

    describe('sticky slides', () => {
        it('should clear sticky slide for after first non sticky slide', () => {
            const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, [
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code1",
                },
                {
                    type: 'Dummy',
                    lang: 'java',
                    snippet: "code2",
                    meta: {
                        stickySlide: 'top 30%'
                    }
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code3",
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code4",
                    meta: {
                        stickySlide: 'top 30%'
                    }
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code5",
                    highlight: [1],
                }
            ])

            const slide1 = registry.slideByIdx(0)
            const slide2 = registry.slideByIdx(1)
            const slide3 = registry.slideByIdx(2)
            const slide4 = registry.slideByIdx(3)
            const slide5 = registry.slideByIdx(4)

            expect(slide1.props.snippet).toEqual("code1");
            expect(slide1.stickySlides).toEqual([]);

            expect(slide2.props.snippet).toEqual("code2");
            expect(slide2.stickySlides).toEqual([]);

            expect(slide3.props.snippet).toEqual("code3");
            expect(slide3.stickySlides.length).toEqual(1);
            expect(slide3.stickySlides[0].props.snippet).toEqual("code2");

            expect(slide4.props.snippet).toEqual("code4");
            expect(slide4.stickySlides.length).toEqual(0);

            expect(slide5.props.snippet).toEqual("code5");
            expect(slide5.stickySlides.length).toEqual(1);
            expect(slide5.stickySlides[0].props.snippet).toEqual("code4");
        })

        it('should replace last slide with current one in case of sticky slides', () => {
            const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, [
                {
                    type: 'Dummy',
                    lang: 'java',
                    snippet: "code1",
                    meta: {
                        stickySlide: 'left'
                    }
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code2",
                    meta: {
                        stickySlide: 'temp'
                    }
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code3",
                },
            ])

            const slide1 = registry.slideByIdx(0)
            const slide2 = registry.slideByIdx(1)
            const slide3 = registry.slideByIdx(2)

            expect(slide1.props.snippet).toEqual("code1");

            expect(slide2.stickySlides.length).toEqual(1);
            expect(slide2.stickySlides[0].props.snippet).toEqual("code1");
            expect(slide2.props.snippet).toEqual("code2");

            expect(slide3.stickySlides.length).toEqual(1);
            expect(slide3.stickySlides[0].props.snippet).toEqual("code1");
            expect(slide3.props.snippet).toEqual("code3");
        })

        it('should allow clear and placement at the same time', () => {
            const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, [
                {
                    type: 'Dummy',
                    lang: 'java',
                    snippet: "code1",
                    meta: {
                        stickySlide: 'left'
                    }
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code2",
                    meta: {
                        stickySlide: 'clear top'
                    }
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code3",
                },
            ])

            const slide2 = registry.slideByIdx(1)
            const slide3 = registry.slideByIdx(2)

            expect(slide2.stickySlides.length).toEqual(0);

            expect(slide3.stickySlides.length).toEqual(1);
            expect(slide3.stickySlides[0].props.snippet).toEqual("code2");
        })

        it('should clear sticky slide on section entry', () => {
            const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, [
                {
                    type: 'Dummy',
                    lang: 'java',
                    snippet: "code1",
                    meta: {
                        stickySlide: 'top 30%'
                    }
                },
                {
                    type: 'Dummy',
                    lang: 'java',
                    snippet: "code2",
                    meta: {
                        stickySlide: 'top 30%'
                    }
                },
                {
                    type: 'Section',
                    title: 'Section title'
                },
            ])

            const slide1 = registry.slideByIdx(0)
            const slide2 = registry.slideByIdx(1)
            const slide3 = registry.slideByIdx(2)

            expect(slide1.stickySlides).toEqual([]);

            expect(slide2.stickySlides.length).toEqual(1);
            expect(slide2.stickySlides[0].props.snippet).toEqual("code1");

            expect(slide3.stickySlides).toEqual([]);
        })
    })

    describe('slide boundaries', () => {
        it('should know slideIdx boundaries of a component', () => {
            const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, [
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code1",
                },
                {
                    type: 'DummyTriple',
                    lang: 'java',
                    snippet: "code2",
                },
                {
                    type: 'DummyTriple',
                    lang: 'java',
                    snippet: "code3",
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code4",
                    highlight: [1],
                }
            ])

            expect(registry.numberOfSlides).toEqual(8)

            expect(registry.slideComponentStartIdxByIdx(0)).toEqual(0)
            expect(registry.slideComponentStartIdxByIdx(1)).toEqual(1)
            expect(registry.slideComponentStartIdxByIdx(2)).toEqual(1)
            expect(registry.slideComponentStartIdxByIdx(3)).toEqual(1)
            expect(registry.slideComponentStartIdxByIdx(4)).toEqual(4)
            expect(registry.slideComponentStartIdxByIdx(5)).toEqual(4)
            expect(registry.slideComponentStartIdxByIdx(6)).toEqual(4)
            expect(registry.slideComponentStartIdxByIdx(7)).toEqual(7)
        })
    })
})