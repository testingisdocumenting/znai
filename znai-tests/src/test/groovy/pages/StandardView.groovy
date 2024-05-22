/*
 * Copyright 2021 znai maintainers
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

package pages

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

class StandardView {
    def pageTitle = $(".page-title")

    def tocSectionTitles = $(".toc-section .title").all()
    def tocItems = $(".toc-item a")

    def footer = $(".footer")

    def docTitle = $(".toc-panel-header")

    def gettingStartedTocItem = tocItems.get("Getting Started")
    def pageTwoTocItem = tocItems.get("Page Two")
    def pageThreeTocItem = tocItems.get("Page Three")
    def metaSection = $("h1").get("Meta")

    def mainPanel = $(".znai-main-panel")
    def mainPanelScrollTop = mainPanel.scrollTop

    def nextPage = $(".next-prev-page-title.next")
    def prevPage = $(".next-prev-page-title.prev")

    def containerTitle = $(".znai-container-title")

    def presentationButton = $(".presentation-button")
}
