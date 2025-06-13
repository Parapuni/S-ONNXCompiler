package ast;

import java.util.List;

public class NodeASTNode extends NamedASTNode{
    public String opType;
    public NodeASTNode(String opType, String name){
        super(name);
        this.opType = opType;
    }

    public List<String> inputs(){
        AbstractSyntaxTree inputs = getChild(0);
        if (inputs instanceof ValueASTNode){
            return ((ValueASTNode) inputs).value;
        } else {
            return null;
        }
    }


    public String attribute() {
        ValueASTNode attrs = (ValueASTNode) getChild(2);
        if (attrs == null) {
            return null;
        }else {
            return String.join(",", attrs.value);
        }
    }

    public List<String> outputs() {
        AbstractSyntaxTree outputs = getChild(1);
        if (outputs instanceof ValueASTNode){
            return ((ValueASTNode) outputs).value;
        } else {
            //TODO output_list语法？
            return null;
        }
    }
}
