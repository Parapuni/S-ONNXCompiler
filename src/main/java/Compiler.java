import antlr4.SONNXLexer;
import antlr4.SONNXParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitor.ASTGenerator;
import visitor.IRGenerator;

import java.io.IOException;

public class Compiler {
    public void compile(String src, String dest) throws IOException {
        CharStream input = CharStreams.fromFileName(src);
        SONNXLexer lexer = new SONNXLexer(input);
        TokenStream tokens = new CommonTokenStream(lexer);
        SONNXParser parser = new SONNXParser(tokens);
        ParseTree tree = parser.model();
        /*
        errors in syntax tree
         */
        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("errors found in the source file:"+src);
            System.exit(1);
        }
        ASTGenerator generator = new ASTGenerator(tree, src);
        generator.ast.toStringTree();
        System.out.println();
        IRGenerator ig = new IRGenerator(generator);
        ig.generate(dest);
    }
}
