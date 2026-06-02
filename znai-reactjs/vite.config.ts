import { defineConfig, ProxyOptions } from "vite";
import react from "@vitejs/plugin-react";

function serveEmptyWhenPreviewOffline(body: string, contentType: string): ProxyOptions["configure"] {
  return (proxy) => {
    queueMicrotask(() => {
      proxy.removeAllListeners("error");
      proxy.on("error", (_err, _req, res) => {
        if (res && "writeHead" in res && !res.headersSent) {
          res.writeHead(200, { "Content-Type": contentType });
          res.end(body);
        }
      });
    });
  };
}

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/preview/all-pages.json": {
        target: "http://localhost:3334",
        changeOrigin: true,
        configure: serveEmptyWhenPreviewOffline("[]", "application/json"),
      },
      "/search-index.js": {
        target: "http://localhost:3334",
        changeOrigin: true,
        rewrite: (path) => path.replace("/search-index.js", "/preview/search-index.js"),
        // Real search-index.js sets `window.znaiSearchData` (see LocalSearchEntries.java);
        // App.jsx feeds it to populateLocalSearchIndexWithData, so the offline stub
        // must define it as an empty array rather than be a no-op.
        configure: serveEmptyWhenPreviewOffline(
          "/* znai preview server offline */ window.znaiSearchData = [];",
          "application/javascript"
        ),
      },
    },
  },
  build: {
    target: ["es2020", "chrome87", "firefox78", "safari14"],
    rolldownOptions: {
      jsx: {
        mode: "automatic",
      },
      logLevel: "debug",
      output: {
        advancedChunks: {
          groups: [
            {
              name: "mermaid",
              test: /node_modules[\\/]mermaid/,
              priority: 20,
            },
          ],
        },
      },
    },
  },
});
