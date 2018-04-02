import * as ts from 'typescript';
import * as fs from 'fs';

export function extractDefinitions(filePath: string): object[] {
    const compilerOptions = { module: ts.ModuleKind.None };
    const program = ts.createProgram([filePath], compilerOptions);

    const checker = program.getTypeChecker();

    const fileContent = fs.readFileSync(filePath, 'utf-8');

    const definitions: object[] = [];

    const userSpecifiedSources = program.getSourceFiles().filter(sf => sf.fileName.indexOf(filePath) !== -1);
    userSpecifiedSources.forEach(sf => ts.forEachChild(sf, visit));

    return definitions;

    function visit(node: ts.Node) {
        if (ts.isClassDeclaration(node) && node.name) {
            const symbol = checker.getSymbolAtLocation(node.name);

            if (symbol) {
                definitions.push(serializeClass(symbol));
            }
        }
    }

    function serializeSymbol(symbol: ts.Symbol): object {
        return {
            name: symbol.getName(),
            documentation: ts.displayPartsToString(symbol.getDocumentationComment(undefined)),
            type: checker.typeToString(checker.getTypeOfSymbolAtLocation(symbol, symbol.valueDeclaration!))
        };
    }

    function serializeClass(symbol: ts.Symbol) {
        let details = serializeSymbol(symbol);

        if (!symbol) {
            return details;
        }

        const keys = symbol.members!.keys();
        let next = keys.next();

        const serializedMembers = [];

        while (! next.done) {
            const member = symbol.members!.get(next.value);
            if (member) {
                const serialized = serializeMember(member);
                serializedMembers.push(serialized);
            }

            next = keys.next();
        }

        return {...details, members: serializedMembers};
    }

    function serializeMember(symbol: ts.Symbol) {
        const kind = symbol.valueDeclaration!.kind;

        switch (kind) {
            case ts.SyntaxKind.PropertyDeclaration:
                return serializeProperty(symbol);
            case ts.SyntaxKind.MethodDeclaration:
                return serializeMethod(symbol);
            default:
                return {};
        }
    }

    function serializeProperty(symbol: ts.Symbol) {
        return {
            name: symbol.name,
            type: checker.typeToString(checker.getTypeOfSymbolAtLocation(symbol, symbol.valueDeclaration!)),
            documentation: ts.displayPartsToString(symbol.getDocumentationComment(checker)),
            kind: 'property'
        };
    }

    function serializeMethod(symbol: ts.Symbol) {
        const bodyNode = symbol.valueDeclaration!.body;
        const body = textOfNode(bodyNode);

        return {
            name: symbol.name,
            kind: 'method',
            documentation: ts.displayPartsToString(symbol.getDocumentationComment( checker)),
            parameters: serializeParameters(symbol),
            body: body
        };
    }

    function serializeParameters(symbol: ts.Symbol) {
        const paramsDocs = paramsDocsByName(symbol);

        return symbol.valueDeclaration!.parameters.map(p => {
            const name = p.name.escapedText;
            return {name: name, type: textOfNode(p.type), documentation: paramsDocs[name]};
        });
    }

    function paramsDocsByName(symbol: ts.Symbol) {
        const docs = {};
        symbol.valueDeclaration.jsDoc.forEach(jsDoc => {
            const paramTags = jsDoc.tags.filter(tag => tag.kind === ts.SyntaxKind.JSDocParameterTag);

            paramTags.forEach(tag => {
                docs[tag.name.escapedText] = tag.comment;
            });
        });

        return docs;
    }

    function textOfNode(node: ts.Node) {
        return fileContent.substr(node.pos, node.end - node.pos);
    }
}