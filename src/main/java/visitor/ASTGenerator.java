package visitor;
import antlr4.*;
import ast.*;
import org.antlr.v4.runtime.tree.ParseTree;
import symbol.Locator;
import symbol.Symbol;
import symbol.SymbolTable;
import symbol.throwable.NameConflictException;

import java.util.HashMap;


/**
 * 生成AST的访问器
 * 主要是将ParseTree转换为AST
 * 其中包含了输入输出符号表和权重符号表
 */
public class ASTGenerator extends SONNXBaseVisitor<AbstractSyntaxTree> {

    public SymbolTable symbols;

    public Locator quetoLocator;

    public AbstractSyntaxTree ast;

    public ASTGenerator(ParseTree tree, String fileName) {
        quetoLocator = new Locator();
        symbols = new SymbolTable(fileName);
        this.ast = visit(tree);
    }

    @Override
    public AbstractSyntaxTree visitModel(SONNXParser.ModelContext ctx){
        return visitGraph_body_def(ctx.model_body_def().graph_def().graph_body_def());
    }
    @Override
    public AbstractSyntaxTree visitGraph_body_def(SONNXParser.Graph_body_defContext ctx) {
        GraphASTNode graphNode = new GraphASTNode(ctx.name_def().StringLiteral().getText());
        graphNode.addChild(visitNode_list(ctx.node_list()));
        graphNode.addChild(visitInput_list(ctx.input_list()));
        graphNode.addChild(visitOutput_list(ctx.output_list()));
        graphNode.addChild(visitInitializer_list(ctx.initializer_list()));
        return graphNode;
    }
    /*
    Node 相关的结点
    node_list----node_list----......
        |           |
    node_def     node_def
    /  |  \
  in  out attr
     */
    @Override
    public AbstractSyntaxTree visitNode_list(SONNXParser.Node_listContext ctx) {
        ListASTNode node_list = new ListASTNode();
        node_list.addChild(visitNode_def(ctx.node_def()));
        if(!ctx.node_repeats().getText().isEmpty()){
            node_list.addChild(visitNode_list(ctx.node_repeats().node_list()));
        }
        return node_list;
    }

    @Override
    public AbstractSyntaxTree visitNode_def(SONNXParser.Node_defContext ctx){
        NodeASTNode node = new NodeASTNode(ctx.op_type_def().StringLiteral().getText(),
                ctx.name_def().StringLiteral().getText());
        /*

        FIXME
        符号表管理


         */
        Symbol symbol = new Symbol(node.name,node.opType, "node",
                "line "+ctx.name_def().getStart().getLine() + ":" + ctx.getText());
        try {
            symbols.addSymbol(symbol, "node");
        } catch (NameConflictException e) {
            System.err.println(this.symbols.scope+": "+e.getMessage());
            System.exit(1);
        }

        /*
        继续遍历AST
         */



        if(ctx.input_list()!=null){
            node.addChild(visitInput_list(ctx.input_list()));
        }else {
            node.addChild(visitInput_arr(ctx.input_arr()));
        }
        if(ctx.output_list()!=null){
            node.addChild(visitOutput_list(ctx.output_list()));
        }else {
            node.addChild(visitOutput_arr(ctx.output_arr()));
        }
        if (ctx.attribute_list()!=null)
            node.addChild(visitAttribute_list(ctx.attribute_list()));
        return node;
    }

    /*
    input，output相关的结点
     */
    @Override
    public AbstractSyntaxTree visitInput_list(SONNXParser.Input_listContext ctx) {
        ListASTNode inputs = new ListASTNode();
        SONNXParser.Input_repeatsContext irCtr = ctx.input_repeats();
        do {
            inputs.addChild(visitValue_info_def(ctx.value_info_def()));
            if(irCtr.getText().isEmpty())
                break;
            ctx = irCtr.input_list();
            irCtr = ctx.input_repeats();
        }while (true);
        return inputs;
    }

    @Override
    public AbstractSyntaxTree visitOutput_list(SONNXParser.Output_listContext ctx) {
        ListASTNode outputs = new ListASTNode();
        SONNXParser.Output_repeatsContext orCtr = ctx.output_repeats();
        do {
            outputs.addChild(visitValue_info_def(ctx.value_info_def()));
            if(orCtr.getText().isEmpty())
                break;
            ctx = orCtr.output_list();
            orCtr = ctx.output_repeats();
        }while (true);
        return outputs;
    }


    @Override
    public AbstractSyntaxTree visitInput_arr(SONNXParser.Input_arrContext ctx) {
        ValueASTNode inputValues = new ValueASTNode(ValueASTNode.ValueKind.ID);
        inputValues.addValue(ctx.StringLiteral().getText());

        /*
        FIXME
         */
        quetoLocator.addLocation(ctx.StringLiteral().getText(), "input", "line "+ctx.StringLiteral().getSymbol().getLine() + ":" + ctx.getText());




        SONNXParser.Id_repeatsContext idCtx = ctx.id_repeats();
        while (!idCtx.getText().isEmpty()){
            inputValues.addValue(idCtx.StringLiteral().getText());
            /*
            FIXME
             */
            quetoLocator.addLocation(idCtx.StringLiteral().getText(), "input", "line "+ctx.StringLiteral().getSymbol().getLine() + ":" + ctx.getText());



            idCtx = idCtx.id_repeats();
        }
        return inputValues;
    }

    @Override
    public AbstractSyntaxTree visitOutput_arr(SONNXParser.Output_arrContext ctx) {
        ValueASTNode outputValues = new ValueASTNode(ValueASTNode.ValueKind.ID);
        outputValues.addValue(ctx.StringLiteral().getText());


        /*
        FIXME
         */
        if(quetoLocator.contains(ctx.StringLiteral().getText(), "output")){
            System.err.println("Error in file"+this.symbols.scope+" at line "+ctx.getStart().getLine()+"Output tensor '" + ctx.StringLiteral().getText() + "' already exists in the output list of another node.");
            System.exit(1);
        }
        quetoLocator.addLocation(ctx.StringLiteral().getText(), "output", "line "+ctx.StringLiteral().getSymbol().getLine() + ":" + ctx.getText());



        SONNXParser.Id_repeatsContext idCtx = ctx.id_repeats();
        while (!idCtx.getText().isEmpty()){
            outputValues.addValue(idCtx.StringLiteral().getText());
             /*
            FIXME
             */
            if(quetoLocator.contains(idCtx.StringLiteral().getText(), "output")){
                System.err.println("Error in file"+this.symbols.scope+" at line "+idCtx.getStart().getLine()+"Output tensor '" + ctx.StringLiteral().getText() + "' already exists in the output list of another node.");
                System.exit(1);
            }
            quetoLocator.addLocation(idCtx.StringLiteral().getText(), "output", "line "+ctx.StringLiteral().getSymbol().getLine() + ":" + ctx.getText());




            idCtx = idCtx.id_repeats();
        }
        return outputValues;
    }

    /*
    initializer_list相关的结点
        list-------list----......
        |           |
      body        body
    /  |  \
type dims raw_data
     */
    @Override
    public AbstractSyntaxTree visitInitializer_list(SONNXParser.Initializer_listContext ctx) {
        if(ctx==null)
            return null;
        ListASTNode init_list = new ListASTNode();
        init_list.addChild(visitTensor_def(ctx.tensor_def()));
        if(!ctx.initializer_repeats().getText().isEmpty())
            init_list.addChild(visitInitializer_list(ctx.initializer_repeats().initializer_list()));
        return init_list;
    }

    @Override
    public AbstractSyntaxTree visitTensor_def(SONNXParser.Tensor_defContext ctx){
        String data_type = "";
        SONNXParser.Data_type_defContext dCtx = ctx.data_type_def();
        if(dCtx.INT()!=null){
            data_type = dCtx.INT().getText();
        } else if (dCtx.STRING()!=null){
            data_type = dCtx.STRING().getText();
        } else if (dCtx.FLOAT()!=null){
            data_type = dCtx.FLOAT().getText();
        } else if (dCtx.BOOL()!=null){
            data_type = dCtx.BOOL().getText();
        }
        WeightASTNode weightTensorNode = new WeightASTNode(ctx.name_def().StringLiteral().getText(),
                data_type, ctx.raw_data_def().BytesLiteral().getText());
        weightTensorNode.addChild(visitDims_def(ctx.dims_def()));

         /*
        FIXME
         */

        Symbol symbol = new Symbol(weightTensorNode.name, data_type, "initializer", "line "+ctx.name_def().getStart().getLine() + ":" + ctx.getText());
        try {
            symbols.addSymbol(symbol, "initializer");
        } catch (NameConflictException e) {
            System.err.println(this.symbols.scope+": "+e.getMessage());
            System.exit(1);
        }



        return weightTensorNode;
    }

    @Override
    public AbstractSyntaxTree visitDims_def(SONNXParser.Dims_defContext ctx) {
        ValueASTNode tensorDims =new ValueASTNode(ValueASTNode.ValueKind.DIM);
        tensorDims.addValue(ctx.IntegerLiteral().getText());
        SONNXParser.Dims_repeatsContext dCtx = ctx.dims_repeats();
        while (!dCtx.getText().isEmpty()){
            tensorDims.addValue(dCtx.IntegerLiteral().getText());
            dCtx = dCtx.dims_repeats();
        }
        return tensorDims;
    }

    /*
    attribute_list part
     */
    @Override
    public AbstractSyntaxTree visitAttribute_list(SONNXParser.Attribute_listContext ctx) {
        ValueASTNode attrsNode = new ValueASTNode(ValueASTNode.ValueKind.ATTRIBUTE);
        SONNXParser.Attribute_defContext aCtx ;
        do {
            aCtx = ctx.attribute_def();
            attrsNode.addValue(aCtx.name_def().StringLiteral().getText().replace("\"","")+"="+aCtx.value_def().StringLiteral().getText().replace("\"",""));
            if(ctx.attribute_repeats().getText().isEmpty())
                break;
            ctx = ctx.attribute_repeats().attribute_list();
        }while (true);

        return attrsNode;
    }

    @Override
    public AbstractSyntaxTree visitValue_info_def(SONNXParser.Value_info_defContext ctx) {
        String name = ctx.name_def().StringLiteral().getText();
        SONNXParser.Tensor_type_defContext ttdCtx = ctx.type_def().tensor_type_def();
        String elemType = "";
        if(ttdCtx.elem_type_def().INT()!=null) {
            elemType = ttdCtx.elem_type_def().INT().getText();
        }else if (ttdCtx.elem_type_def().FLOAT()!=null){
            elemType = ttdCtx.elem_type_def().FLOAT().getText();
        }else if (ttdCtx.elem_type_def().STRING()!=null){
            elemType = ttdCtx.elem_type_def().STRING().getText();
        }else if (ttdCtx.elem_type_def().BOOL()!=null){
            elemType = ttdCtx.elem_type_def().BOOL().getText();
        }
        TensorASTNode tensorNode = new TensorASTNode(name, elemType);
        tensorNode.addChild(visitDim_list(ttdCtx.shape_def().dim_list()));



        /*
        FIXME
         */
        String scope;
        if(ctx.getParent() instanceof SONNXParser.Input_listContext) {
            scope = "input";
        } else {
            scope = "output";
        }
        Symbol symbol = new Symbol(name, elemType, scope, "line "+ctx.name_def().getStart().getLine() + ":" + ctx.getText());
        try {
            symbols.addSymbol(symbol, scope);
            /*
            TODO : 异常处理
             */
        } catch (NameConflictException e) {
            System.err.println(this.symbols.scope+": "+e.getMessage());
            System.exit(1);
        }





        return tensorNode;
    }

    @Override
    public AbstractSyntaxTree visitDim_list(SONNXParser.Dim_listContext ctx) {
        ValueASTNode dimValues = new ValueASTNode(ValueASTNode.ValueKind.DIM);
        SONNXParser.Dim_repeatsContext drCtx = ctx.dim_repeats();
        do {
            if(!ctx.dim_def().IntegerLiteral().getText().isEmpty() ) {
                dimValues.addValue(ctx.dim_def().IntegerLiteral().getText());
            } else if (!ctx.dim_def().StringLiteral().getText().isEmpty()) {
                dimValues.addValue(ctx.dim_def().StringLiteral().getText());
            }
            if (drCtx.getText().isEmpty())
                break;
            ctx = drCtx.dim_list();
            drCtx = ctx.dim_repeats();
        }while (true);
        return dimValues;
    }

}
