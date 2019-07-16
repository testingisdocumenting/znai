const path = require('path');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

const distDir = path.resolve(__dirname, 'dist');

module.exports = {
    entry: './src/TestApp.jsx',
    output: {
        path: distDir,
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: [MiniCssExtractPlugin.loader, 'css-loader']
            }
        ]
    },
    devServer: {
        contentBase: path.resolve(__dirname, "dist")
    }
};