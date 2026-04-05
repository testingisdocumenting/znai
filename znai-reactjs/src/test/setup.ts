
import '@testing-library/jest-dom'
import { vi } from 'vitest'

// Setup DOM environment
global.DOMParser = window.DOMParser
global.document = window.document

// Mock Element and Node constructors to fix instanceof errors
global.Element = window.Element
global.HTMLElement = window.HTMLElement
global.Node = window.Node
global.DocumentFragment = window.DocumentFragment

// Mock additional DOM APIs that might be needed
global.Selection = window.Selection || class MockSelection {}
global.Range = window.Range || class MockRange {}

// Mock getSelection
global.getSelection = vi.fn(() => ({
    removeAllRanges: vi.fn(),
    addRange: vi.fn(),
    toString: vi.fn(() => ''),
    rangeCount: 0,
    anchorNode: null,
    anchorOffset: 0,
    focusNode: null,
    focusOffset: 0,
    isCollapsed: true,
    type: 'None'
}))

// Mock ResizeObserver if needed
global.ResizeObserver = vi.fn(() => ({
    observe: vi.fn(),
    unobserve: vi.fn(),
    disconnect: vi.fn(),
}))

// Mock IntersectionObserver if needed
global.IntersectionObserver = vi.fn(() => ({
    observe: vi.fn(),
    unobserve: vi.fn(),
    disconnect: vi.fn(),
}))

// Mock fetch globally
global.fetch = vi.fn()

beforeAll(() => {
    // Mock fetch to return successful responses by default
    (global.fetch as any).mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({}),
        text: () => Promise.resolve(''),
    })
})

afterAll(() => {
    // Add any cleanup that should run after all tests
})