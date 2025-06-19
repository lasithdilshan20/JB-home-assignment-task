/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#6366f1', // indigo-500
          dark: '#4f46e5',    // indigo-600
          light: '#818cf8',   // indigo-400
        },
        secondary: {
          DEFAULT: '#10b981', // emerald-500
          dark: '#059669',    // emerald-600
          light: '#34d399',   // emerald-400
        },
        background: {
          dark: '#111827',    // gray-900
          DEFAULT: '#1f2937', // gray-800
          light: '#374151',   // gray-700
          panel: 'rgba(31, 41, 55, 0.8)', // semi-transparent gray-800
        },
      },
      boxShadow: {
        'glow': '0 0 15px rgba(99, 102, 241, 0.5)',
      },
    },
  },
  plugins: [],
}