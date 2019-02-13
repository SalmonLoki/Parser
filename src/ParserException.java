import java.io.IOException;

public class ParserException extends IOException {
    public ParserException() {
        super();
    }

    public ParserException(Exception e){
        super(e);
    }

    public ParserException(String message) {
        super(message);
    }
}
