module.exports = {
    presets: [
        ["@babel/preset-react", {
            "runtime": "classic",
            "pragma": "React.createElement",
            "pragmaFrag": "React.Fragment"
        }],
        ['@babel/preset-env', {
            "useBuiltIns": "usage",
            "corejs": 3,
            "targets": {
                "node": "current"
            }
        }
]

    ]
}