import java.io.IOException;

public class Main {
    static final String path = "D:\\S-ONNXCompiler\\S-ONNXCompiler\\src\\";
    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        for (int i = 1;i <= 10;i++)
            compiler.compile(path + "\\testcases\\case" + i + ".txt", path+"\\results\\case" + i + ".ir");
    }
}
