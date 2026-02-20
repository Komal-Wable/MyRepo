/// <reference types="vitest" />
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],

  // ✅ FIX for sockjs global error
  define: {
    global: 'window',
  },

  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/test/setup.js'
  }
})
