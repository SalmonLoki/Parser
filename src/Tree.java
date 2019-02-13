import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree {

    private String node;
    private List<Tree> children;

    Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    Tree(String node){
        this.node = node;
        this.children = new ArrayList<>();
    }

    private String toString(int n) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            s.append(" ");
        }
        s.append(node);
        s.append("\n");
        for(Tree child : children) {
            s.append(child.toString(n + 2));
        }
        return s.toString();
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
