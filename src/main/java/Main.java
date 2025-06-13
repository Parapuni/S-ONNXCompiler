import java.io.IOException;

public class Main {
    static final String path = "D:\\S-ONNXCompiler\\S-ONNXCompiler\\src\\";
    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        compiler.compile(path + "excase.txt", path + "excase.ir");
/*        for (int i = 6;i <= 6;i++)
            compiler.compile(path + "\\testcases\\case" + i + ".txt", path+"\\results\\case" + i + ".ir");*/
    }
}
