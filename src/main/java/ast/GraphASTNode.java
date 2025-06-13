package ast;

public class GraphASTNode extends NamedASTNode{
    public GraphASTNode(String name) {
        super(name);
    }

    public ListASTNode nodeList() {
        return (ListASTNode) getChild(0);
    }

    public ListASTNode inputList() {
        return (ListASTNode) getChild(1);
    }

    public ListASTNode outputList() {
        return (ListASTNode) getChild(2);
    }

    public ListASTNode initializerList() {
        return (ListASTNode) getChild(3);
    }
}
