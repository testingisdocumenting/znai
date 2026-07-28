/*
 * Copyright 2026 znai maintainers
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
import { describe, it, expect } from 'vitest';

import { stripShellPromptPrefix } from "./codeUtils";

describe("stripShellPromptPrefix", () => {
    it("removes the $ prompt prefix from bash snippet lines", () => {
        const text = '$ echo hello\n$ ls -la'
        expect(stripShellPromptPrefix(text, 'bash')).toEqual('echo hello\nls -la')
    })

    it("keeps lines without a prompt untouched, e.g. line continuations and output", () => {
        const text = '$ my-tool run \\\n' +
            '  -flag value\n' +
            'output line\n' +
            '\n' +
            '$ another-command'
        expect(stripShellPromptPrefix(text, 'sh')).toEqual(
            'my-tool run \\\n' +
            '  -flag value\n' +
            'output line\n' +
            '\n' +
            'another-command')
    })

    it("preserves indentation before the prompt", () => {
        expect(stripShellPromptPrefix('  $ echo indented', 'bash')).toEqual('  echo indented')
    })

    it("does not remove $ that is part of a variable reference", () => {
        expect(stripShellPromptPrefix('$HOME/bin/tool', 'bash')).toEqual('$HOME/bin/tool')
    })

    it("does not change snippets of other languages", () => {
        expect(stripShellPromptPrefix('$ looks like prompt', 'java')).toEqual('$ looks like prompt')
        expect(stripShellPromptPrefix('$ looks like prompt', undefined)).toEqual('$ looks like prompt')
    })
})
