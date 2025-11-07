import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    rolldownOptions: {
      jsx: {
        mode: 'automatic'
      },
      logLevel: 'debug',
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
  },
  logLevel: 'debug'
})
