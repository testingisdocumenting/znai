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

    def tocSectionTitles = $(".toc-section .title")
    def tocItems = $(".toc-item a")

    def footer = $(".footer")

    def shortcutsTocItem = tocItems.get("Shortcuts")

    def codeSnippetsTocItem = tocItems.get("Code Snippets")
    def externalCodeSnippetsTocItem = tocItems.get("External Code Snippets")
    def externalCodeWideCodeSection = $("h1").get("Wide Code")

    def apiParametersTocItem = tocItems.get("API Parameters")

    def mainPanel = $(".znai-main-panel")
    def mainPanelScrollTop = mainPanel.scrollTop
}
