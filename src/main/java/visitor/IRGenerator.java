package visitor;

import ast.*;
import symbol.DataType;
import symbol.Locator;
import symbol.Symbol;
import symbol.SymbolTable;
import symbol.throwable.TypeUnmatchedException;
import symbol.throwable.UndefinedSymbolException;

import java.io.*;
import java.util.*;

public class IRGenerator {
    public int nextTemp;
    public int nextWeight;
    public SymbolTable symbols;
    public Locator locator;
    public GraphASTNode root;
    public BufferedWriter bw;
    public IRGenerator(ASTGenerator astGenerator) {
        this.nextTemp = 1;
        this.nextWeight = 1;
        this.symbols = astGenerator.symbols;
        this.locator = astGenerator.quetoLocator;
        this.root = (GraphASTNode) astGenerator.ast;
    }

    public String newTemp() {
        String t =  "T" + nextTemp;
        nextTemp++;
        return t;
    }

    public String newWeight() {
        String w = "W" + nextWeight;
        nextWeight++;
        return w;
    }


    public void generate(String filePath) throws IOException {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
        visitInitializerList(root.initializerList());
        visitInputList(root.inputList());
        visitNodeList(root.nodeList());
        visitOutputList(root.outputList());
        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkDefinition(String scope) throws UndefinedSymbolException {
        Set<Map.Entry<String,String>> set = locator.entry(scope);
        for (Map.Entry<String, String> entry : set) {
            String name = entry.getKey();
            String location = entry.getValue();
            if(!symbols.hasSymbol(name,scope))
                if ("input".equals(scope) && !symbols.hasSymbol(name,"initializer"))
                    throw new UndefinedSymbolException(name, location);
        }
    }

    public void checkType(List<String> inputs, List<String> outputs) throws TypeUnmatchedException{
        Set<DataType> inputTypes = new HashSet<>();
        Set<DataType> outputTypes = new HashSet<>();
        for (String input : inputs) {
            Symbol symbol = symbols.getSymbol(input, "input");
            if (symbol == null)
                symbol = symbols.getSymbol(input, "initializer");
            inputTypes.add(symbol.dataType);
        }
        for (String output : outputs) {
            Symbol symbol = symbols.getSymbol(output, "output");
            outputTypes.add(symbol.dataType);
        }
        if(inputTypes.size()!=1){
            throw new TypeUnmatchedException(locator.inLocator.get(inputs.get(0)), "between inputs");
        }
        else if(outputTypes.size()!=1){
            throw new TypeUnmatchedException(locator.outLocator.get(outputs.get(0)), "between outputs");
        }else if (!inputTypes.equals(outputTypes)) {
            throw new TypeUnmatchedException(locator.inLocator.get(inputs.get(0)), "between inputs and outputs");
        }

    }

    public void visitNodeList(ListASTNode node) throws IOException {
        while (node != null){
            visitNodeBody(node.body());
            node = node.next();
        }
    }

    public void visitNodeBody(NodeASTNode node) throws IOException {
        List<String> inputs = node.inputs();
        List<String> outputs = node.outputs();
        try {
            checkDefinition("input");
            checkDefinition("output");
            checkType(inputs, outputs);
        }catch (UndefinedSymbolException e) {
            System.err.println(this.symbols.scope+": "+"error: " + e.getMessage());
            System.exit(1);
        }catch (TypeUnmatchedException e) {
            System.err.println(this.symbols.scope+": "+"error: " + e.getMessage());
            System.exit(1);
        }
        String op = node.opType.replace("\"","");
        String attr = node.attribute();
        List<String> temps = new ArrayList<>();
        for (String input : inputs){
            String temp = symbols.getTempName(input, "input");
            if(temp == null)
                temp = symbols.getTempName(input, "initializer");
            temps.add(temp);
        }
        /*
        TODO 处理多个output
         */
        for (String output : outputs) {
            String temp = newTemp();
            //setTempName
            symbols.setTempName(output, "output", temp);
            String tac;
            if(attr != null)
                tac = temp + "=" + op + "(" + String.join(",",temps) + "," + attr + ")\n";
            else
                tac = temp + "=" + op + "(" + String.join(",",temps) + ")\n";
            bw.write(tac);
        }
    }

    public void visitInputList(ListASTNode node) throws IOException {
        for (AbstractSyntaxTree child : node.children) {
            TensorASTNode inputNode = (TensorASTNode) child;
            String name = inputNode.name;
            String elemType = inputNode.elemType;
            String value = inputNode.dims() != null ? inputNode.dims().value.toString() : "";
            String temp = newTemp();
            symbols.setTempName(name, "input", temp);
            String tac = temp+"="+ "Input("+name+","+elemType+","+value+")\n";
            bw.write(tac);
        }
    }

    public void visitOutputList(ListASTNode node) throws IOException {
        for (AbstractSyntaxTree child : node.children){
            TensorASTNode outputNode = (TensorASTNode) child;
            String name = outputNode.name;
            String temp = symbols.getTempName(name, "output");
            if (temp == null) {
                temp = newTemp();
                symbols.setTempName(name, "output", temp);
            }
            String tac = "Output(" + name + "," +  temp + ")\n";
            bw.write(tac);
        }
    }

    public void visitInitializerList(ListASTNode node) throws IOException {
        if(node ==null)
            return;
        for (AbstractSyntaxTree child : node.children) {
            WeightASTNode initializerNode = (WeightASTNode) child;
            String name = initializerNode.name;
            String elemType = initializerNode.dataType;
            String value = initializerNode.dims() != null ? initializerNode.dims().value.toString() : "";
            String rawData = initializerNode.raw_data;
            String weight = newWeight();
            symbols.setTempName(name,"initializer", weight);
            String tac = weight + "=" + "Initializer(" + name + "," + elemType + "," + value + ","  + rawData+ ")\n";
            bw.write(tac);
        }
    }
}
