package ast;

public class NodeASTNode extends NamedASTNode{
    public String opType;
    public NodeASTNode(String opType, String name){
        super(name);
        this.opType = opType;
    }
}
