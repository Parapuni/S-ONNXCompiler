import antlr4.SONNXLexer;
import antlr4.SONNXParser;
import ast.AbstractSyntaxTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitor.ASTGenerator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CharStream input = CharStreams.fromFileName("D:\\S-ONNXCompiler\\S-ONNXCompiler\\src\\main\\resources\\case3.txt");
        SONNXLexer lexer = new SONNXLexer(input);
        TokenStream tokens = new CommonTokenStream(lexer);
        SONNXParser parser = new SONNXParser(tokens);
        ParseTree tree = parser.model();
        ASTGenerator generator = new ASTGenerator(tree);
        generator.ast.toStringTree();
    }
}
