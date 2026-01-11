/*
 * Copyright 2025 znai maintainers
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

import { useState, useCallback } from 'react';

interface Dimensions {
    width: number;
    height: number;
}

export function useImageDimensions() {
    const [dimensions, setDimensions] = useState<Dimensions | null>(null);

    const handleImageLoad = useCallback((event: React.SyntheticEvent<HTMLImageElement>) => {
        const { naturalWidth, naturalHeight } = event.currentTarget;
        setDimensions({ width: naturalWidth, height: naturalHeight });
    }, []);

    return { dimensions, handleImageLoad };
}
