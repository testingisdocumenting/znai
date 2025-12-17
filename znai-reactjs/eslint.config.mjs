import js from '@eslint/js';
import globals from 'globals';
import react from 'eslint-plugin-react';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import typescriptEslint from '@typescript-eslint/eslint-plugin';
import parser from '@typescript-eslint/parser';

export default [
    // Ignore patterns
    {
        ignores: ['target', 'dist', 'build', 'node_modules', 'public/**/*.js', '*.config.js'],
    },

    // Base JavaScript configuration
    {
        files: ['**/*.{js,jsx,mjs,cjs}'],
        languageOptions: {
            parser: parser,
            ecmaVersion: 2020,
            globals: {
                ...globals.browser,
                ...globals.es2020,
                populateLocalSearchIndexWithData: 'readonly',
                documentationNavigation: 'readonly',
                // Add other globals from znai's generated HTML
                toc: 'readonly',
                znaiSearchData: 'readonly'
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
            '@typescript-eslint': typescriptEslint,
        },
        rules: {
            // ESLint recommended rules
            '@typescript-eslint/consistent-type-imports': [
                'error',
                {
                    prefer: 'type-imports',
                    fixStyle: 'separate-type-imports',
                },
            ],
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
            'no-unused-vars': ['warn', {
                argsIgnorePattern: '^_',
                varsIgnorePattern: '^_'
            }],
            'no-console': ['warn', { allow: ['warn', 'error'] }],
        },
    },
    // TypeScript files
    {
        files: ['**/*.{ts,tsx}'],
        languageOptions: {
            parser: parser,
            parserOptions: {
                ecmaVersion: 2020,
                sourceType: 'module',
                ecmaFeatures: {jsx: true},
                project: './tsconfig.json',  // Important for type-aware rules
            },
            globals: {
                ...globals.browser,
                ...globals.es2020,
            },
        },
        plugins: {
            '@typescript-eslint': typescriptEslint,
            react,
            'react-hooks': reactHooks,
            'react-refresh': reactRefresh,
        },
        rules: {
        // ... your existing rules
        '@typescript-eslint/consistent-type-imports': [
            'error',
            {
                prefer: 'no-type-imports',
                disallowTypeAnnotations: false
            }
        ]
        }
    },
    // Special config for vitest.config.ts (and other config files)
    {
        files: ['vitest.config.ts', '*.config.ts'],
        languageOptions: {
            parser: parser,
            parserOptions: {
                ecmaVersion: 2020,
                sourceType: 'module',
                project: './tsconfig.json',  // Use node tsconfig
            },
            globals: {
                ...globals.node,  // Node globals instead of browser
            },
        },
        plugins: {
            '@typescript-eslint': typescriptEslint,
        },
        rules: {
            ...typescriptEslint.configs.recommended.rules,
            'no-undef': 'off',
            '@typescript-eslint/no-unused-vars': ['warn', { argsIgnorePattern: '^_' }],
        }
    },
    {
        files: ['**/*.test.{js,jsx,ts,tsx}', '**/*.spec.{js,jsx,ts,tsx}'],
        languageOptions: {
            globals: {
                ...globals.node,  // Vitest runs in Node
                describe: 'readonly',
                it: 'readonly',
                test: 'readonly',
                expect: 'readonly',
                beforeEach: 'readonly',
                afterEach: 'readonly',
                beforeAll: 'readonly',
                afterAll: 'readonly',
                vi: 'readonly',  // Vitest's mock utility
            },
        },
    },
    {
        files: ['**/*.demo.{js,jsx,ts,tsx}', '**/*.stories.{js,jsx,ts,tsx}'],
        rules: {
            'react-refresh/only-export-components': 'off',
            'react/display-name': 'off', // Often useful for demos too
        },
    },
];