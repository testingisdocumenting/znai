import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react-swc'

export default defineConfig({
  plugins: [react()],
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: ['./src/test/setup.ts'],
    include: ['src/**/*.{test,spec}.{js,jsx,ts,tsx}'],
    includeSource: ['src/**/*.{js,ts}'],
    css: true
  },
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx']
  }
})