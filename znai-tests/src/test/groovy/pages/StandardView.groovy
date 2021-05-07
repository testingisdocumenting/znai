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
    def tocSectionTitles = $(".toc-section .title")
    def tocItems = $(".toc-item a")

    def shortcutsTocItem = tocItems.get("Shortcuts")
    def externalCodeSnippetsTocItem = tocItems.get("External Code Snippets")
}
