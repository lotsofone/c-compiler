package Lexer;

import java.util.LinkedList;
import java.io.File;  
import java.io.InputStreamReader;  
import java.io.BufferedReader;  
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class Lexer extends LinkedList<Code_Token> {
	@SuppressWarnings("resource")
	public Lexer(String path) throws IOException {
		File filename = new File(path);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
		BufferedReader buffer = new BufferedReader(reader);
		String code = "";
		String line = "";
		line = buffer.readLine();
		while(line != null) {
			code += "\n" + line;
			line = buffer.readLine();
		}
		//System.out.println(code);
		this.addAll(Code_Tokenizer.tokenize(code));
	}
}