/*
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

import FilterInput from "./FilterInput";

import TocPanel from "../../layout/TocPanel";

import "./Landing.css";

interface Documentation {
  id: string | null;
  name: string;
  category: string;
  description: string;
  url?: string;
}

interface CategoryWithDocsType {
  category: string;
  documentations: Documentation[];
}

interface Props {
  documentations: Documentation[];
  type: string;
  title: string;
}

interface State {
  filterText: string;
  tocCollapsed: boolean;
}

export class Landing extends React.Component<Props, State> {
  state = { filterText: "", tocCollapsed: false };

  render() {
    const { documentations, type, title } = this.props;
    const { filterText, tocCollapsed } = this.state;

    const landingDocMeta = { type: type, title: title };

    const filteredDocumentations = filterDocumentations(
      documentations,
      filterText
    );

    const categoriesWithDocs = groupByCategory(filteredDocumentations);
    const documentationsToc = buildToc(categoriesWithDocs);

    return (
      <React.Fragment>
        <div className="znai-landing">
          <div className="znai-landing-categories-toc-area">
            <TocPanel
              toc={documentationsToc}
              docMeta={landingDocMeta}
              collapsed={tocCollapsed}
              onToggle={this.tocCollapseToggle}
            />
          </div>

          <div className="znai-landing-documentations-area">
            <div className="centered">
              <FilterInput
                filterText={filterText}
                onChange={this.onFilterChange}
              />

              <div className="znai-landing-categories">
                {categoriesWithDocs.map((categoryWithDocs) => (
                  <CategoryWithDocs
                    key={categoryWithDocs.category}
                    category={categoryWithDocs.category}
                    documentations={categoryWithDocs.documentations}
                  />
                ))}
              </div>
            </div>
          </div>
        </div>
      </React.Fragment>
    );
  }

  tocCollapseToggle = () => {
    this.setState((prev) => ({
      tocCollapsed: !prev.tocCollapsed,
    }));
  };

  onFilterChange = (e: Event) => {
    this.setState({ filterText: (e.target! as HTMLInputElement).value });
  };
}

function CategoryWithDocs({ category, documentations }: CategoryWithDocsType) {
  return (
    <div className="znai-landing-category-with-documentations">
      <Category category={category} />
      <Documentations documentations={documentations} />
    </div>
  );
}

function Category({ category }: { category: string }) {
  return (
    <div className="znai-landing-category" id={anchorIdFromName(category)}>
      <div className="small-line" />
      <div className="category">{category}</div>
      <div className="large-line" />
    </div>
  );
}

function Documentations({
  documentations,
}: {
  documentations: Documentation[];
}) {
  return (
    <div className="znai-landing-documentations">
      {documentations.map((d) => (
        <Documentation key={d.id!} documentation={d} />
      ))}
    </div>
  );
}

function Documentation({ documentation }: { documentation: Documentation }) {
  const url = documentation.url ? documentation.url : documentation.id + "/";

  return (
    <div className="znai-landing-documentation">
      <a href={url} target="_blank" rel="noopener noreferrer">
        <div className="name">{documentation.name}</div>
        <div className="description">{documentation.description}</div>
      </a>
    </div>
  );
}

function buildToc(categoriesWithDocs: CategoryWithDocsType[]) {
  const items = categoriesWithDocs.map((withDocs) => ({
    sectionTitle: "Categories",
    pageTitle: withDocs.category,
    fileName: withDocs.category,
    dirName: "categories",
    pageSectionIdTitles: [],
    href: "#" + anchorIdFromName(withDocs.category),
  }));

  return [
    {
      sectionTitle: "Categories",
      dirName: "categories",
      items: items,
    },
  ];
}

function filterDocumentations(
  documentations: Documentation[],
  filterText: string
) {
  filterText = filterText.toLowerCase();
  return documentations.filter((d) => {
    return (
      (d.id && textMatch(d.id, filterText)) ||
      textMatch(d.name, filterText) ||
      textMatch(d.category, filterText) ||
      textMatch(d.description, filterText)
    );
  });
}

function textMatch(text: string, lowerCaseFilter: string) {
  return text.toLowerCase().indexOf(lowerCaseFilter) !== -1;
}

function anchorIdFromName(name: string) {
  return name.toLowerCase().replace(" ", "-");
}

function groupByCategory(documentations: Documentation[]) {
  const groups = new Map<string, Documentation[]>();
  documentations.forEach((d) => {
    let entries = groups.get(d.category);
    if (typeof entries === "undefined") {
      entries = [];
      groups.set(d.category, entries);
    }

    entries.push(d);
  });

  const sortedKeys = Object.keys(groups).sort();
  return sortedKeys.map((category) => ({
    category,
    documentations: groups.get(category)!.sort(),
  }));
}
