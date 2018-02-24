package Parser;

import ASM.ASM8086;
import ASM.ASM_Filewrite;
import Inter.QuaterExpression;
import Lexer.*;
import Symbols.FunctionArg;

import java.io.IOException;
import java.util.List;
import java.util.EnumSet;
import java.util.Vector;

public class Entry {
    public static void main(String[] args) throws IOException {
        List<Code_Token> lst = new Lexer("main.c");
        for(Code_Token one_token:lst){
            if(one_token.legal==false)throw new RuntimeException("词语错误 line: "+one_token.line+"  column:"+one_token.column);
            if(one_token.sign == Sign.FLOAT||one_token.word.equals("float"))throw new RuntimeException("暂不支持浮点数 line: "+one_token.line+"  column:"+one_token.column);
        }
        lst.add(new Code_Token(Sign.END));
        AnalysisTableOfSLR table = new AnalysisTableOfSLR();
        //table.print();
        table.analysis(lst);
//        table.enter.print();
        List<QuaterExpression> qlst = table.enter.getQList();

//		Vector<FunctionArg> a = table.enter.table.functionArgsSize("fff");
//        table.enter.table.print();
        //for(Sign sign : EnumSet.range(Sign.INT, Sign.Factor))
        //System.out.println(table.DFA.Gram.FOLLOW(Sign.Label));
        //table.DFA.print();
        //table.DFA.printTable();
        //table.DFA.Gram.getProColl().print();
        //table.outputInter();
		ASM8086 masm = new ASM8086();
        masm.set_quaterexpressions(qlst);
        masm.set_symboltable(table.enter.table);
        String asm_code = masm.asm();
        //System.out.println(asm_code);
//
        ASM_Filewrite output = new ASM_Filewrite("8086.asm");
        output.output(asm_code);


    }
}
