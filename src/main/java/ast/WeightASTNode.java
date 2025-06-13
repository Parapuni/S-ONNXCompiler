package ast;

public class WeightASTNode extends NamedASTNode {

    public String dataType;
    public String raw_data;
    public WeightASTNode(String name, String dataType, String raw_data) {
        super(name);
        this.dataType = dataType;
        this.raw_data = raw_data;
    }

    public ValueASTNode dims(){
        ValueASTNode dimsNode = null;
        if (children.size() > 0 && children.get(0) instanceof ValueASTNode) {
            dimsNode = (ValueASTNode) children.get(0);
        }
        return dimsNode;
    }
}
