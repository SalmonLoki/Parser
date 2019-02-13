import java.io.IOException;
import java.io.InputStream;

class LexicalAnalyzer {

    enum Token {
        TYPE, NAME, END, FUNCTION, PROCEDURE, SEMICOLON, OPEN, CLOSE, COLON, COMMA, KW
    }

    private boolean isBlank(int c) {
        return c == ' ' || c == '\r' || c == '\n' || c == '\t';
    }

    LexicalAnalyzer(InputStream is) throws ParserException {
        this.is = is;
        nextChar();
    }

    private InputStream is;
    private int curChar;
    String curWord;
    Token curToken;

    private void nextChar() throws ParserException {
        try {
            curChar = is.read();
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }


    void nextToken() throws ParserException {
        while (isBlank(curChar)) {
            nextChar();
        }

        if (Character.isLetter(curChar)) {
            StringBuilder word = new StringBuilder();
            while (!isBlank(curChar) && (Character.isLetter(curChar) || Character.isDigit(curChar))) {
                word.append((char) curChar);
                nextChar();
            }
            curWord = word.toString();
            switch (curWord) {
                case "function":
                    curToken = Token.FUNCTION;
                    break;
                case "procedure":
                    curToken = Token.PROCEDURE;
                    break;
                case "integer":
                case "byte":
                case "word":
                case "shortint":
                case "longint":
                case "real":
                case "single":
                case "double":
                case "extended":
                case "boolean":
                case "char":
                    curToken = Token.TYPE;
                    break;
                case "var":
                    curToken = Token.KW;
                    break;
                default:
                    curToken = Token.NAME;
                    break;
            }
        } else {
            curWord = "";
            switch (curChar) {
                case '(':
                    nextChar();
                    curToken = Token.OPEN;
                    break;
                case ')':
                    nextChar();
                    curToken = Token.CLOSE;
                    break;
                case ',':
                    nextChar();
                    curToken = Token.COMMA;
                    break;
                case ';':
                    nextChar();
                    curToken = Token.SEMICOLON;
                    break;
                case ':':
                    nextChar();
                    curToken = Token.COLON;
                    break;
                case -1:
                    curToken = Token.END;
                    break;
                default:
                    throw new ParserException("Illegal character " + (char) curChar);
            }
        }
    }
}
