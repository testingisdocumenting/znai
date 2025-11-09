import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/preview": {
        target: "http://localhost:3334",
        changeOrigin: true,
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
