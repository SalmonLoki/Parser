import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws ParserException, FileNotFoundException {
	    System.out.print(new Parser().parse(new FileInputStream(new File("input8.txt"))));
    }
}
