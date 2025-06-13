package ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValueASTNode extends AbstractSyntaxTree {
    public enum ValueKind {
        DIM, ID, ATTRIBUTE
    }
    public ValueKind valueKind;
    public List<String> value;
    public ValueASTNode(ValueKind valueKind) {
        super();
        this.valueKind = valueKind;
        this.value = new ArrayList<>();
    }

    public void addValue(String value) {
        this.value.add(value);
    }

    public String getValue() {
        return  this.value.toString();
    }

    @Override
    public void toStringTree(){
        System.out.print(this.valueKind+value.toString());
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
