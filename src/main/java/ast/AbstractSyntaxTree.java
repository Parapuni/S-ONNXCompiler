package ast;

import org.antlr.v4.runtime.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbstractSyntaxTree {
    public List<AbstractSyntaxTree> children;
    public AbstractSyntaxTree(){
        this.children = new ArrayList<>();
    }

    public Boolean addChild(AbstractSyntaxTree node){
        if(node == null)
            return false;
        this.children.add(node);
        return true;
    }

    public AbstractSyntaxTree getChild(int i){
        if(i<this.children.size()){
            return this.children.get(i);
        } else
            return null;
    }

    public void toStringTree(){
        System.out.print(this.getClass().getSimpleName());
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
