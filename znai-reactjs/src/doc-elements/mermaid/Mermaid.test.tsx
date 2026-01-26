import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { render } from '@testing-library/react';
import Mermaid from './Mermaid';
import React from 'react';

// Mock mermaid completely to avoid any DOM manipulation
vi.mock('mermaid', () => ({
    default: {
        initialize: vi.fn(),
        render: vi.fn(() => Promise.resolve({ svg: '<div data-testid="mocked-svg">Mocked SVG</div>' })),
        registerIconPacks: vi.fn(),
    }
}));

describe('Mermaid', () => {
    beforeEach(() => {
        vi.clearAllMocks();

        // Simplified window mock
        Object.defineProperty(window, 'znaiTheme', {
            value: {
                name: 'znai-light',
                addChangeHandler: vi.fn(),
                removeChangeHandler: vi.fn(),
            },
            writable: true,
            configurable: true
        });
    });

    afterEach(() => {
        vi.clearAllMocks();
    });

    it('should render mermaid diagram with basic props', () => {
        const { container } = render(<Mermaid mermaid="graph TD; A-->B" />);

        expect(container.firstChild).toHaveClass('znai-mermaid');
        expect(container.firstChild).toHaveClass('content-block');
    });

    it('should apply wide mode styling when wide prop is true', () => {
        const { container } = render(<Mermaid mermaid="graph TD; A-->B" wide={true} />);

        expect(container.firstChild).toHaveClass('znai-mermaid');
        expect(container.firstChild).toHaveClass('wide');
        expect(container.firstChild).not.toHaveClass('content-block');
    });

    it('should handle iconpacks configuration', () => {
        const iconpacks = [{ name: "logos", url: "https://example.com/icons.json" }];
        const { container } = render(<Mermaid mermaid="graph TD; A-->B" iconpacks={iconpacks} />);

        expect(container.firstChild).toHaveClass('znai-mermaid');
    });

    it('should use dark theme when znaiTheme.name is znai-dark', () => {
        (window as any).znaiTheme.name = 'znai-dark';

        const { container } = render(<Mermaid mermaid="graph TD; A-->B" />);

        expect(container.firstChild).toHaveClass('znai-mermaid');
    });
});