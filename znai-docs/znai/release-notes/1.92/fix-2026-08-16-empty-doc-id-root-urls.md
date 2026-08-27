* Fix: Empty [`--doc-id ""`](introduction/getting-started#static-site-generation) no longer generates protocol-relative `//chapter/page` links in pages, chapter redirects and [`search-entries.xml`](flow/search#global)
* Fix: Chapter and page redirects use trailing slash targets so static hosts like [GitHub Pages](introduction/getting-started#github-pages) don't serve the redirect page in a loop
