import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/preview/all-pages.json": {
        target: "http://localhost:3334",
        changeOrigin: true,
      },
      "/search-index.js": {
        target: "http://localhost:3334",
        changeOrigin: true,
        rewrite: (path) => path.replace("/search-index.js", "/preview/search-index.js"),
      },
    },
  },
  build: {
    target: ['es2020', 'chrome87', 'firefox78', 'safari14'],
    rolldownOptions: {
      jsx: {
        mode: 'automatic'
      },
      logLevel: 'debug',
      output: {
        advancedChunks: {
          groups: [
            {
              name: 'react-libs',
              test: /node_modules[\\/](react|react-dom)/,
              priority: 30,
            },
            {
              name: 'mermaid',
              test: /node_modules[\\/]mermaid/,
              priority: 20,
            }],
        },
        chunkFileNames (chunk) {
          return chunk.name === 'react-libs' ? 'assets/react-libs.js' :  'assets/[name].[hash].js'
        }

      }
    }
  }
})
