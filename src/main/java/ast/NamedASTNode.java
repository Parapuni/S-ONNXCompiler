package ast;

public class NamedASTNode extends AbstractSyntaxTree {
    public String name;

    public NamedASTNode(String name) {
        super();
        this.name = name;
    }

}
