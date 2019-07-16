const path = require('path');

module.exports = {
    entry: './src/main/javascript/main.js',
    target: 'node',
    output: {
        filename: 'typeScriptParserBundle.js',
        path: path.resolve(__dirname, 'target/classes')
    }
};