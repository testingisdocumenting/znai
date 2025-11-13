import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";

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
    rollupOptions: {
      output: {
        manualChunks: undefined, // Prevent splitting chunks
      },
    },
  },
  test: {
    globals: true,
    environment: "jsdom",
  },
});
