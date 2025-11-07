import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    rolldownOptions: {
      output: {
        advancedChunks: {
          groups: [
            {
              name: 'mermaid',
              test: /node_modules[\\/]mermaid/,
              priority: 20,
            }]
        }
      }
    }
  }
})
