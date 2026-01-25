/*
 * Copyright 2026 znai maintainers
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
export function svg(width = 256, height = 256) {
  const heightPart = height ? 'height="' + height + '"' : "";
  const widthPart = width ? 'width="' + width + '"' : "";

  return (
    '<svg viewBox="0 0 512 512" ' +
    heightPart +
    " " +
    widthPart +
    "\n" +
    '\t xml:space="preserve" xmlns="http://www.w3.org/2000/svg" >\n' +
    '\t<style type="text/css">\n' +
    "\t\t.st0{fill:#5CB8CD;}\n" +
    "\t\t.st1{fill:#FB8A52;}\n" +
    "\t\t.st2{fill:#152D39;}\n" +
    "\t\t.st3{fill:#E8E8E8;}\n" +
    "\t</style>\n" +
    "\n" +
    '\t<g id="partA">\n' +
    '\t\t<path class="st2" d="M109.4,397.7c-0.8-3.8-0.4-7.9,1.4-11.8l127.9-279.4c2.3-5.1,6.4-8.5,11-10.1V6\n' +
    '\t\t\tc-4.6,1.6-8.6,4.9-11,10.1L52.1,423.6c-1.9,4.1-2.2,8.3-1.3,12.2L109.4,397.7z" />\n' +
    '\t\t<path class="st2" d="M160.4,325.4c-43.8,0-79.4-35.6-79.4-79.4s35.6-79.4,79.4-79.4s79.4,35.6,79.4,79.4\n' +
    '\t\t S204.2,325.4,160.4,325.4z" />\n' +
    '\t\t<circle class="st3" cx="160.4" cy="246"  r="68.1"/>\n' +
    '\t\t<path class="st2" d="M168.2,253.3h-11.3l-2.7,7.8H150l10.9-30.1h3.5l10.6,30.1h-4.2L168.2,253.3z M158,249.9h9l-4.4-12.9h-0.1\n' +
    '\t\t L158,249.9z"/>\n' +
    "\t</g>\n" +
    "\n" +
    '\t<g id="partC">\n' +
    '\t\t<path class="st1" d="M394.8,409.5c-3,2.1-6.8,3.4-10.9,3.4H128.1c-4.7,0-8.9-1.6-12.1-4.3l-58.3,37.9\n' +
    '\t\t\tc3.2,2.5,7.2,4,11.8,4h373.1c4,0,7.6-1.2,10.5-3.2L394.8,409.5z" />\n' +
    '\t\t<path class="st1" d="M262.4,506c-43.8,0-79.4-35.6-79.4-79.4s35.6-79.4,79.4-79.4s79.4,35.6,79.4,79.4\n' +
    '\t\t S306.1,506,262.4,506z" />\n' +
    '\t\t<circle class="st3" cx="262.4" cy="426.6" id="XMLID_7_" r="68.1"/>\n' +
    '\t\t<path class="st1" d="M274.8,432.6l0,0.1c0.1,3.1-1,5.7-3.3,7.8c-2.3,2.1-5.2,3.2-8.9,3.2c-3.8,0-6.8-1.3-9.2-4\n' +
    "\t\tc-2.4-2.7-3.6-6.1-3.6-10.2v-5.9c0-4.1,1.2-7.5,3.6-10.2c2.4-2.7,5.4-4,9.2-4c3.8,0,6.8,1,9,3c2.2,2,3.3,4.7,3.2,8l0,0.1h-4.3\n" +
    "\t\tc0-2.3-0.7-4.2-2.1-5.5c-1.4-1.4-3.3-2-5.8-2c-2.5,0-4.5,1-6,3c-1.5,2-2.2,4.5-2.2,7.5v6c0,3,0.7,5.6,2.2,7.6c1.5,2,3.5,3,6,3\n" +
    '\t\t  c2.5,0,4.4-0.7,5.8-2c1.4-1.3,2.1-3.2,2.1-5.6H274.8z"/>\n' +
    "\t</g>\n" +
    "\n" +
    '\t<g id="partB">\n' +
    '\t\t<path class="st0" d="M262.4,96.5c4.6,1.6,8.6,4.9,11,10.1l127.9,279.4c2,4.4,2.2,9.1,1,13.2l58.5,38.1\n' +
    '\t\t\tc1.4-4.3,1.2-9.1-0.9-13.6L273.4,16.1c-2.3-5.1-6.4-8.5-11-10.1V96.5z"/>\n' +
    '\t\t<path class="st0" d="M364.3,325.4c-43.8,0-79.4-35.6-79.4-79.4s35.6-79.4,79.4-79.4s79.4,35.6,79.4,79.4\n' +
    '\t\t S408.1,325.4,364.3,325.4z" />\n' +
    '\t\t<circle class="st3" cx="364.3" cy="246" id="XMLID_9_" r="68.1"/>\n' +
    '\t\t<path class="st0" d="M352.7,263.2v-34.4h11.2c3.6,0,6.4,0.8,8.4,2.3c2,1.6,3,3.9,3,7c0,1.5-0.5,2.8-1.4,4c-0.9,1.2-2.1,2.1-3.6,2.7\n' +
    "\t\t c2.3,0.3,4,1.3,5.3,2.9c1.3,1.6,2,3.6,2,5.8c0,3.1-1,5.6-3.1,7.2c-2,1.7-4.8,2.5-8.3,2.5H352.7z M357.3,243.4h7.5\n" +
    "\t\t  c1.7,0,3.1-0.5,4.2-1.4c1.1-1,1.6-2.3,1.6-4c0-1.9-0.6-3.2-1.8-4.2c-1.2-0.9-2.9-1.4-5.1-1.4h-6.6V243.4z M357.3,247.1v12.5h9\n" +
    '\t\t    c2.1,0,3.8-0.5,4.9-1.6c1.2-1,1.8-2.5,1.8-4.5c0-1.9-0.6-3.4-1.8-4.6c-1.2-1.2-2.8-1.8-4.8-1.9h-0.3H357.3z"/>\n' +
    "\t</g>\n" +
    "</svg>"
  );
}

export function wideBoxesSvg() {
  return `<svg xmlns="http://www.w3.org/2000/svg" width="1200" height="100" viewBox="0 0 1200 100">
  <rect x="10" y="10" width="220" height="80" rx="8" fill="#4a90d9" fill-opacity="0.8" stroke="#2d5a87" stroke-width="2"/>
  <rect x="250" y="10" width="220" height="80" rx="8" fill="#50b87d" fill-opacity="0.8" stroke="#2d7a4d" stroke-width="2"/>
  <rect x="490" y="10" width="220" height="80" rx="8" fill="#e67e22" fill-opacity="0.8" stroke="#a85a18" stroke-width="2"/>
  <rect x="730" y="10" width="220" height="80" rx="8" fill="#9b59b6" fill-opacity="0.8" stroke="#6c3d7a" stroke-width="2"/>
  <rect x="970" y="10" width="220" height="80" rx="8" fill="#e74c3c" fill-opacity="0.8" stroke="#a83228" stroke-width="2"/>
  <rect x="30" y="25" width="50" height="50" rx="4" fill="#ffffff" fill-opacity="0.3" stroke="#ffffff" stroke-opacity="0.5" stroke-width="1"/>
  <rect x="270" y="25" width="50" height="50" rx="4" fill="#ffffff" fill-opacity="0.3" stroke="#ffffff" stroke-opacity="0.5" stroke-width="1"/>
  <rect x="510" y="25" width="50" height="50" rx="4" fill="#ffffff" fill-opacity="0.3" stroke="#ffffff" stroke-opacity="0.5" stroke-width="1"/>
  <rect x="750" y="25" width="50" height="50" rx="4" fill="#ffffff" fill-opacity="0.3" stroke="#ffffff" stroke-opacity="0.5" stroke-width="1"/>
  <rect x="990" y="25" width="50" height="50" rx="4" fill="#ffffff" fill-opacity="0.3" stroke="#ffffff" stroke-opacity="0.5" stroke-width="1"/>
  <text x="155" y="58" font-family="Arial, sans-serif" font-size="16" font-weight="bold" fill="#ffffff" text-anchor="middle">Design</text>
  <text x="395" y="58" font-family="Arial, sans-serif" font-size="16" font-weight="bold" fill="#ffffff" text-anchor="middle">Develop</text>
  <text x="635" y="58" font-family="Arial, sans-serif" font-size="16" font-weight="bold" fill="#ffffff" text-anchor="middle">Test</text>
  <text x="875" y="58" font-family="Arial, sans-serif" font-size="16" font-weight="bold" fill="#ffffff" text-anchor="middle">Release</text>
  <text x="1115" y="58" font-family="Arial, sans-serif" font-size="16" font-weight="bold" fill="#ffffff" text-anchor="middle">Monitor</text>
</svg>`;
}
