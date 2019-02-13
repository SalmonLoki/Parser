import java.io.InputStream;

class Parser {

    private LexicalAnalyzer lex;

    Tree parse(InputStream is) throws ParserException {
        lex = new LexicalAnalyzer(is);
        lex.nextToken();
        Tree result = s();
        if (lex.curToken == LexicalAnalyzer.Token.END) {
            return result;
        }
        throw new AssertionError();
    }

    private Tree Type() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.TYPE) {
            Tree result = new Tree("TYPE", new Tree(lex.curWord));
            lex.nextToken();
            return result;
        }
        throw new AssertionError();
    }

    private Tree Name() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.NAME) {
            Tree result = new Tree("NAME", new Tree(lex.curWord));
            lex.nextToken();
            return result;
        }
        throw new AssertionError();
    }

    private Tree kw() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.KW) {
            Tree result = new Tree("VAR_KW", new Tree(lex.curWord));
            lex.nextToken();
            return result;
        }
        throw new AssertionError();
    }

    private Tree Var() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.KW) {
            Tree result_kw = kw();
            if (lex.curToken == LexicalAnalyzer.Token.NAME) {
                Tree result = Names();
                if (lex.curToken == LexicalAnalyzer.Token.COLON) {
                    lex.nextToken();
                    result = new Tree("VAR", result_kw, result, new Tree("COLON", new Tree(":")), Type());
                    return result;
                }
            }
        } else if (lex.curToken == LexicalAnalyzer.Token.NAME) {
            Tree result = Names();
            if (lex.curToken == LexicalAnalyzer.Token.COLON) {
                lex.nextToken();
                result = new Tree("VAR", result, new Tree("COLON", new Tree(":")), Type());
                return result;
            }
        }
        throw new AssertionError();
    }

    private Tree ArgsRest() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.SEMICOLON) {
            lex.nextToken();
            return new Tree("ARGS'", new Tree("SEMICOLON", new Tree(";")), Args());
        }
        return new Tree("ARGS", new Tree(""));
    }

    private Tree Args() throws ParserException {
        return new Tree("ARGS", Var(), ArgsRest());
    }

    private Tree NamesRest() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.COMMA) {
            lex.nextToken();
            return new Tree("NAMES'", new Tree("COMMA", new Tree(",")), Names());
        }
        return new Tree("NAMES", new Tree(""));
    }

    private Tree Names() throws ParserException {
        return new Tree("NAMES", Name(), NamesRest());
    }

    private Tree NameFunction() throws ParserException {
        Tree result = Name();
        if (lex.curToken == LexicalAnalyzer.Token.OPEN) {
            lex.nextToken();
            if (lex.curToken == LexicalAnalyzer.Token.CLOSE) {
                lex.nextToken();
                return new Tree("NAME_FUNCTION", result, new Tree("("), new Tree(")"));
            } else {
                result = new Tree("NAME_FUNCTION", result, new Tree("("), Args(), new Tree(")"));
                if (lex.curToken != LexicalAnalyzer.Token.CLOSE) {
                    throw new AssertionError();
                }
                lex.nextToken();
                return result;
            }

        }
        throw new AssertionError();
    }

    private Tree function() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.FUNCTION) {
            lex.nextToken();
            Tree result = NameFunction();
            if (lex.curToken == LexicalAnalyzer.Token.COLON) {
                lex.nextToken();
                result = new Tree("FUNCTION", new Tree("function"), result, new Tree(":"), Type());
                return result;
            }
        }
        throw new AssertionError();
    }

    private Tree procedure() throws ParserException {
        if (lex.curToken == LexicalAnalyzer.Token.PROCEDURE) {
            lex.nextToken();
            return NameFunction();
        }
        throw new AssertionError();
    }

    private Tree s() throws ParserException {
        Tree result;
        switch (lex.curToken) {
            case FUNCTION:
                result = function();
                if (lex.curToken == LexicalAnalyzer.Token.SEMICOLON) {
                    lex.nextToken();
                    return new Tree("S", result, new Tree(";"));
                }
            case PROCEDURE:
                result = procedure();
                if (lex.curToken == LexicalAnalyzer.Token.SEMICOLON) {
                    lex.nextToken();
                    return new Tree("S", result, new Tree(";"));
                }
        }
        throw new AssertionError();
    }
}
