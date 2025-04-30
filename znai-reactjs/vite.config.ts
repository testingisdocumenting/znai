import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";

export default defineConfig({
  plugins: [react()],
  build: {
    rollupOptions: {
      output: {
        manualChunks: null, // Prevent splitting chunks
        inlineDynamicImports: true,
      },
    },
    commonjsOptions: {
      include: /node_modules/, // Ensure dependencies are included
    },
  },
  test: {
    globals: true,
    environment: "jsdom",
  },
});
