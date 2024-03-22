package bdebaro.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Lexer {

    private String lexed = "";

    final HashMap<String, String> SYMBOLS = new HashMap<>();

    public Lexer() {
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
        String code = fullStringify(new File(fileName));
        code = preprocessCode(code);
        ArrayList<String> tokens = listify(code);
        for (String token : tokens) {
            System.out.println(token);
        }
        convert_tokens(tokens);
        print_lexed();
    }

    private String fullStringify(File opened) {
        StringBuffer strb = new StringBuffer();
        try { 
            BufferedReader br = new BufferedReader(new FileReader(opened));
            Iterator<String> allLines = br.lines().iterator();
            while(allLines.hasNext()) {
                String line = allLines.next();
                strb.append(line + "\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strb.toString();
    }

    private ArrayList<String> listify(String code) {
        ArrayList<String> result = new ArrayList<String>();
        char[] chararr = code.toCharArray();
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < chararr.length; i++) {
            if (chararr[i] == '=' || chararr[i] == '<' || chararr[i] == '>') {
                if (buf.length() > 0) {
                    result.add(buf.toString());
                    buf.delete(0, buf.length());
                }
                if (chararr.length+1 > i) {
                    if (chararr[i+1] == '=' || chararr[i+1] == '<' || chararr[i+1] == '>') {
                        result.add(String.valueOf(chararr[i]+String.valueOf(chararr[i+1])));
                        i++;
                    } else {
                        result.add(String.valueOf(chararr[i]));
                    }
                }
            } else if (!(chararr[i] >= 'A' && chararr[i] <= 'Z' || chararr[i] >= 'a' && chararr[i] <= 'z' || chararr[i] >= '0' && chararr[i] <= '9' || chararr[i] == '_')) {
                if (buf.length() > 0) {
                    result.add(buf.toString());
                    buf.delete(0, buf.length());
                }
                result.add(String.valueOf(chararr[i]));
            } else {
                buf.append(chararr[i]);
            }
        }
        if (buf.length() > 0) {
            result.add(buf.toString());
            buf.delete(0, buf.length());
        }
        return result;
    }

    private String preprocessCode(String code) {
        //Removes comments
        code = code.replaceAll("//.{0,}\n", "");
        code = code.replaceAll("/\\*(\n|.){0,}\\*/", "");

        return code.trim();
    }

    private void convert_tokens(ArrayList<String> strings) {
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
        } else if (string.matches("\\s+")) {
            return "";
        }
        return "failed";
    }

    public void print_lexed() {
        System.out.println(lexed);
    }

}
