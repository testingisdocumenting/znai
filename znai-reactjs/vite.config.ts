import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";

export default defineConfig({
  plugins: [react()],
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
