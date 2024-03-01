package bdebaro.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class Lexer {

    private String code;
    private String lexed = "";
    private File opened;
    private String tokenized = "";

    final HashMap<String, String> SYMBOLS = new HashMap<>();

    public Lexer() {
        code = "";
        SYMBOLS.put("if", "<IF>");
        SYMBOLS.put("program", "<SOP>");
        SYMBOLS.put("end_program", "<EOP>");
        SYMBOLS.put("=", "<ASSIGN>");
        SYMBOLS.put("+", "<ADD>");
        SYMBOLS.put("*", "<MULT>");
        SYMBOLS.put("/", "<DIV>");
        SYMBOLS.put("%", "<MOD>");
        SYMBOLS.put("(", "<OPP>");
        SYMBOLS.put(")", "<CLP>");
        SYMBOLS.put("end_if", "<END_IF>");
        SYMBOLS.put("==", "<EQ>");
        SYMBOLS.put("!=", "<NEQ>");
        SYMBOLS.put("<", "<LT>");
        SYMBOLS.put(">", "<GT>");
        SYMBOLS.put("<=", "<LE>");
        SYMBOLS.put(">=", "<GE>");
        SYMBOLS.put("loop", "<LOOP>");
        SYMBOLS.put(";", "<EOS>");
        SYMBOLS.put(":", "<COL>");
        SYMBOLS.put("end_loop", "<EL>");
    }

    public void Tokenize(String fileName) {
        code = fullStringify(new File(fileName));
        preprocessCode();
        String[] tokens = listify();
        for (String token : tokens) {
            System.out.println(token);
        }
        convert_tokens(tokens);
        print_lexed();
    }

    private String fullStringify(File opened) {
        try { 
            BufferedReader br = new BufferedReader(new FileReader(opened));
            Iterator<String> allLines = br.lines().iterator();
            while(allLines.hasNext()) {
                String line = allLines.next();
                code += line + "\n";
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    private String[] listify() {
        return code.split("(\\s+|((?<=[^a-zA-Z0-9_=><])|(?=[^a-zA-Z0-9_=><])))|(?=(>=|<=|==))|(?<=(>=|<=|==))");
    }

    private void preprocessCode() {
        //Removes comments
        code = code.replaceAll("//.{0,}\n", "");
        code = code.replaceAll("/\\*(\n|.){0,}\\*/", "");

        code = code.trim();
    }

    private void convert_tokens(String[] strings) {
        for (String string : strings) {
            if (SYMBOLS.containsKey(string)) {
                write_token(SYMBOLS.get(string));
            } else {
                write_token(gen_ident_token(string));
            }
        }
    }
    

    private void write_token(String token) {
        lexed += token;
    }

    private String gen_ident_token(String string) {
        if (string.length() == 0) {
            return "";
        }
        String first_ch  = string.substring(0,1);
        if (first_ch.matches("[a-zA-Z]")) {
            if (string.matches("[a-zA-Z0-9]+")) {
                return "<IDENT>";
            } else {
                return "error";
            }
        } else if (first_ch.matches("[0-9]")) {
            if (string.matches("[0-9]+")) {
                return "<INT>";
            } else {
                return "error";
            }
        }
        return "failed";
    }

    public void print_lexed() {
        System.out.println(lexed);
    }

}
