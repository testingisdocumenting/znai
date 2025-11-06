import {defineConfig} from "vite";
import react from "@vitejs/plugin-react-swc";

// @ts-ignore
export default defineConfig({
  plugins: [react({
        jsxRuntime: 'classic',
        jsxImportSource: 'react'
      }
  )],
  esbuild: {
    jsx: 'automatic',
    jsxFactory: 'React.createElement',
    jsxFragment: 'React.Fragment',
  },
  build: {
    commonjsOptions: {
      include: [/node_modules/],
      transformMixedEsModules: true
    },
    rollupOptions: {
      external: ['react', 'react-dom'],
      output: {
        manualChunks: undefined
      }
    },
    sourcemap: true
  },
  server: {
    headers: {
      // Ensure proper MIME types
      'Content-Type': 'application/javascript',
      // Allow proper CORS
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET',
      'Access-Control-Allow-Headers': 'Content-Type'
    },
    // Force proper MIME types
    fs: {
      strict: true
    },
    // Ensure proper module loading
    middlewareMode: false
  },
  optimizeDeps: {
    include: ['react', 'react-dom','mermaid'],
    exclude: []
  },
  // Ensure proper asset handling
  assetsInclude: ['**/*.js', '**/*.mjs'],
  // Proper module resolution
  resolve: {
    alias: {
      'react/jsx-runtime': 'react/jsx-runtime',
      'react/jsx-dev-runtime': 'react/jsx-dev-runtime',
      'mermaid': 'mermaid/dist/mermaid.esm.min.mjs'

    },
    extensions: ['.js', '.jsx', '.mjs', '.json', '.ts', '.tsx']
  }

});