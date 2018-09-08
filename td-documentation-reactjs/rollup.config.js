import babel from 'rollup-plugin-babel'
import commonjs from 'rollup-plugin-commonjs'
import postcss from 'rollup-plugin-postcss'
import autoprefixer from 'autoprefixer'
import resolve from 'rollup-plugin-node-resolve'
import {uglify} from 'rollup-plugin-uglify'
import builtins from 'rollup-plugin-node-builtins'

export default {
    input: 'src/export.js',
    output: {
        file: 'build/rollup/main.js',
        format: 'iife',
        name: 'mdoc',
        globals: {'rmeact': 'React'}
    },
    external: [
        'react',
        'react-dom',
    ],
    plugins: [
        postcss({
            plugins: [autoprefixer()],
            extensions: [ '.css' ],
            extract: true
        }),
        babel({
            babelrc: false,
            exclude: 'node_modules/**',
            presets: [ [ 'es2015', { modules: false } ], 'stage-0', 'react' ],
            plugins: [ 'external-helpers' ]
        }),
        resolve(),
        commonjs({
            include: [
                'node_modules/**',
            ]
        }),
        builtins(),
        uglify()
    ]
};