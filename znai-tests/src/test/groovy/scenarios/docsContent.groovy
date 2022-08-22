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

package scenarios

import docgen.ScaffoldUtils

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

def scaffoldServerUrl = cache.value('scaffold-server-url')

scenario('scaffold new docs and serve') {
    scaffoldServerUrl.set(
            ScaffoldUtils.scaffoldAndServe('znai-basic-scaffold').baseUrl)
}

scenario('open docs in browser') {
    browser.open(scaffoldServerUrl.get() + '/my-product')
}

scenario('next page has chapter name rendered') {
    standardView.pageTwoTocItem.click()

    standardView.nextPage.should == "Chapter Two: Page Three"
    standardView.prevPage.shouldNotBe visible
}

scenario('navigate to next page and validate prev page') {
    standardView.nextPage.click()
    standardView.prevPage.should == "Chapter One: Page Two"
}

scenario('check generated plugin stats') {
    http.get(http.concatUrl(scaffoldServerUrl.get(), '/my-product/plugin-stats.json')) {
        includePlugins.file.count.shouldBe > 1
        includePlugins.file.params.should contain("title")
    }
}

scenario('table of contents navigation') {
    standardView.tocSectionTitles.should containAll("CHAPTER ONE", "CHAPTER TWO")
    standardView.pageThreeTocItem.click()

    browser.url.path.should contain("/chapter-two/page-three")
    browser.title.should == "Your Product: Page Three"
}

scenario('navigating back and forth should preserve scroll position') {
    standardView.gettingStartedTocItem.click()
    standardView.metaSection.waitTo visible
    standardView.metaSection.scrollIntoView()

    def scrollTopBeforeClick = standardView.mainPanelScrollTop.get()

    standardView.pageThreeTocItem.click()
    standardView.pageTitle.waitTo == "Page Three"
    standardView.mainPanelScrollTop.should == 0

    // TODO replace with webtau shorcut
    browser.driver.navigate().back()
    standardView.mainPanelScrollTop.waitTo == scrollTopBeforeClick

    // TODO replace with webtau shorcut
    browser.driver.navigate().forward()
    standardView.mainPanelScrollTop.waitTo == 0
}