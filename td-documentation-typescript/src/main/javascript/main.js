const readline = require('readline');
const { extractDefinitions } = require('./definitionsExtractor');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
    terminal: false
});

rl.on('line', function (cmd) {
    const definitions = extractDefinitions(cmd);
    console.log(JSON.stringify(definitions));
});
