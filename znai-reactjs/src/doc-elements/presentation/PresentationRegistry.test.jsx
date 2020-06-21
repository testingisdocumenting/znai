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

describe('PresentationRegistry', () => {
    const elementsLibrary = {
        'Dummy': () => <div/>
    }

    const presentationElementHandlers = {
        'Dummy': {
            component: () => <div/>,
            numberOfSlides: () => 1
        },
        'NonSlide': {
            component: () => <div/>,
            numberOfSlides: () => 0
        }
    }

    describe('sticky slides', () => {
        it('should clear sticky slide for after first non sticky slide', () => {
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
                    type: 'NonSlide',
                    lang: 'python',
                    snippet: "code1_",
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code2",
                },
                {
                    type: 'Dummy',
                    lang: 'python',
                    snippet: "code3",
                    highlight: [1],
                }
            ])

            const slide1 = registry.slideByIdx(0)
            const slide2 = registry.slideByIdx(1)
            const slide3 = registry.slideByIdx(2)

            expect(slide1.stickySlides).toEqual([]);

            expect(slide2.props.snippet).toEqual("code2");
            expect(slide2.stickySlides.length).toEqual(1);
            expect(slide2.stickySlides[0].props.snippet).toEqual("code1");

            expect(slide3.props.snippet).toEqual("code3");
            expect(slide3.stickySlides.length).toEqual(0);
        })
    })
})