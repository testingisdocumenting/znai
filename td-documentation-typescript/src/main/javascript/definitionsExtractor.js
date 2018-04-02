const ts = require('typescript');
const fs = require('fs');

module.exports.extractDefinitions = extractDefinitions;

function extractDefinitions(filePath) {
    const compilerOptions = { module: ts.ModuleKind.None };
    const program = ts.createProgram([filePath], compilerOptions);

    const checker = program.getTypeChecker();

    const fileContent = fs.readFileSync(filePath, 'utf-8');

    const definitions = [];

    const userSpecifiedSources = program.getSourceFiles().filter(sf => sf.fileName.indexOf(filePath) !== -1);
    userSpecifiedSources.forEach(sf => ts.forEachChild(sf, visit));

    return definitions;

    function visit(node) {
        if (ts.isClassDeclaration(node) && node.name) {
            const symbol = checker.getSymbolAtLocation(node.name);

            if (symbol) {
                definitions.push(serializeClass(symbol));
            }
        }
    }

    function serializeSymbol(symbol) {
        return {
            name: symbol.getName(),
            documentation: ts.displayPartsToString(symbol.getDocumentationComment(undefined)),
            type: checker.typeToString(checker.getTypeOfSymbolAtLocation(symbol, symbol.valueDeclaration))
        }
    }

    function serializeClass(symbol) {
        let details = serializeSymbol(symbol);

        if (!symbol) {
            return details;
        }

        const keys = symbol.members.keys();
        let next = keys.next();

        const serializedMembers = [];

        while (! next.done) {
            const member = symbol.members.get(next.value);
            if (member) {
                const serialized = serializeMember(member);
                serializedMembers.push(serialized);
            }

            next = keys.next();
        }

        return Object.assign({}, details, {members: serializedMembers});
    }

    function serializeMember(symbol) {
        const kind = symbol.valueDeclaration.kind;

        switch (kind) {
            case ts.SyntaxKind.PropertyDeclaration:
                return serializeProperty(symbol);
            case ts.SyntaxKind.MethodDeclaration:
                return serializeMethod(symbol);
            default:
                return {};
        }
    }

    function serializeProperty(symbol) {
        return {
            name: symbol.name,
            type: checker.typeToString(checker.getTypeOfSymbolAtLocation(symbol, symbol.valueDeclaration)),
            documentation: ts.displayPartsToString(symbol.getDocumentationComment(checker)),
            kind: 'property'
        }
    }

    function serializeMethod(symbol) {
        const bodyNode = symbol.valueDeclaration.body;
        const body = textOfNode(bodyNode);

        return {
            name: symbol.name,
            kind: 'method',
            documentation: ts.displayPartsToString(symbol.getDocumentationComment( checker)),
            parameters: serializeParameters(symbol),
            body: body
        };
    }

    function serializeParameters(symbol) {
        const paramsDocs = paramsDocsByName(symbol);

        return symbol.valueDeclaration.parameters.map(p => {
            const name = p.name.escapedText;
            return {name: name, type: textOfNode(p.type), documentation: paramsDocs[name]};
        });
    }

    function paramsDocsByName(symbol) {
        const docs = {};
        symbol.valueDeclaration.jsDoc.forEach(jsDoc => {
            const paramTags = jsDoc.tags.filter(tag => tag.kind === ts.SyntaxKind.JSDocParameterTag);

            paramTags.forEach(tag => {
                docs[tag.name.escapedText] = tag.comment;
            });
        });

        return docs;
    }

    function textOfNode(node) {
        return fileContent.substr(node.pos, node.end - node.pos);
    }
}