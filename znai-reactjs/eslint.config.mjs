import js from '@eslint/js';
import globals from 'globals';
import react from 'eslint-plugin-react';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';

export default [
    // Ignore patterns
    {
        ignores: ['dist', 'build', 'node_modules', '*.config.js'],
    },

    // Base JavaScript configuration
    {
        files: ['**/*.{js,jsx,mjs,cjs,ts,tsx}'],
        languageOptions: {
            ecmaVersion: 2020,
            globals: {
                ...globals.browser,
                ...globals.es2020,
            },
            parserOptions: {
                ecmaVersion: 'latest',
                ecmaFeatures: { jsx: true },
                sourceType: 'module',
            },
        },
        settings: {
            react: {
                version: 'detect',
            },
        },
        plugins: {
            react,
            'react-hooks': reactHooks,
            'react-refresh': reactRefresh,
        },
        rules: {
            // ESLint recommended rules
            ...js.configs.recommended.rules,

            // React rules
            ...react.configs.recommended.rules,
            ...react.configs['jsx-runtime'].rules,
            'react/jsx-no-target-blank': 'off',
            'react/prop-types': 'off', // Turn off if using TypeScript

            // React Hooks rules
            ...reactHooks.configs.recommended.rules,

            // React Refresh
            'react-refresh/only-export-components': [
                'warn',
                { allowConstantExport: true },
            ],

            // Custom rules
            'no-unused-vars': ['warn', { argsIgnorePattern: '^_' }],
            'no-console': ['warn', { allow: ['warn', 'error'] }],
        },
    },
];