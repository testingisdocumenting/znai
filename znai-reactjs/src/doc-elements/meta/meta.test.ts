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

import { presentationStickPlacement } from './meta';

describe('presentationStickPlacement', () => {
  it('should have default 50% as percentage', () => {
    const placement = presentationStickPlacement({presentationStickPlacement: 'left'});
    expect(placement).toEqual({left: true, top: false, percentage: 50, clear: false})
  })

  it('should extract percentage as a second parameter', () => {
    const placement = presentationStickPlacement({presentationStickPlacement: 'top 25'});
    expect(placement).toEqual({left: false, top: true, percentage: 25, clear: false})
  })

  it('should strip percentage sign', () => {
    const placement = presentationStickPlacement({presentationStickPlacement: 'left 45%'});
    expect(placement).toEqual({left: true, top: false, percentage: 45, clear: false})
  })

  it('should detect sticky placement clear', () => {
    const placement = presentationStickPlacement({presentationStickPlacement: 'clear'});
    expect(placement).toEqual({left: false, top: false, percentage: 50, clear: true})
  })
});