import '@testing-library/jest-dom'
import { vi } from 'vitest'

// Setup DOM environment
global.DOMParser = window.DOMParser
global.document = window.document


// Add any other global mocks or setup needed

beforeAll(() => {
    // Add any setup that should run before all tests
})

afterAll(() => {
    // Add any cleanup that should run after all tests
})