package ast;

public class TensorASTNode extends NamedASTNode {
    public String elemType;
    public TensorASTNode(String name, String elemType) {
        super(name);
        this.elemType = elemType;
    }

    public ValueASTNode dims(){
        ValueASTNode dimsNode = null;
        if (children.size() > 0 && children.get(0) instanceof ValueASTNode) {
            dimsNode = (ValueASTNode) children.get(0);
        }
        return dimsNode;
    }

    @Override
    public void toStringTree(){
        System.out.print("tensor("+this.name+":"+this.elemType+")");
        if (!this.children.isEmpty()){
            System.out.print(" ( ");
            for (AbstractSyntaxTree child : this.children) {
                child.toStringTree();
                System.out.print(" ");
            }
            System.out.print(") ");
        }
    }

}
