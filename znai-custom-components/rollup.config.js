import babel from 'rollup-plugin-babel';
import postcss from 'rollup-plugin-postcss';
import autoprefixer from 'autoprefixer';

export default {
    input: 'src/components/index.js',
    output: {
        file: 'bundle.js',
        format: 'iife',
        name: 'myComponents',
        globals: {'react': 'React'}
    },
    external: [
        'react'
    ],
    plugins: [
        postcss({
            plugins: [autoprefixer()],
            extract: true
        }),
        babel({
            exclude: 'node_modules/**'
        }),
    ]
};