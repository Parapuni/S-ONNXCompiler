package ast;

public class ListASTNode extends AbstractSyntaxTree{
    public ListASTNode next(){
        if(getChildCount() < 2) {
            return null;
        }else {
            return (ListASTNode) getChild(1);
        }
    }

    public NodeASTNode body() {
        return (NodeASTNode) getChild(0);
    }
}
