import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  define: {
    'process.env': {},
    'process.env.NODE_ENV': JSON.stringify('production'),
    'global': 'globalThis',
  },
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
    lib: {
      entry: {
        'react-bundle' : path.resolve(__dirname, 'src/react-bundle.js'),
        'znai-components' : path.resolve(__dirname, 'src/library.js'),
      },
      name: 'ZnaiComponents',
      formats: ['es'],
      fileName: (format, entryName) => `${entryName}.${format}.js`,
    },
    rolldownOptions: {
      jsx: {
        mode: 'automatic'
      },
      logLevel: 'debug',
      preserveEntrySignatures: 'strict',
      output: {
        exports: 'auto',
        advancedChunks: {
          groups: [

            {
              name: 'mermaid',
              test: /node_modules[\\/]mermaid/,
              priority: 20,
            }],
        },
        chunkFileNames (_chunk) {
          return  'assets/[name].[hash].js'
        }

      }
    }
  }
})
