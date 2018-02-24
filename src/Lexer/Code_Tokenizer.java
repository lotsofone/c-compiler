package Lexer;

import Parser.*;

import java.util.*;

public class Code_Tokenizer {
    private static class Quary {
        int sign;
        char c;
        int tag;//tag = 0表示普通字符，tag = -1表示其他


        Quary(int faced_sign, int the_tag) {
            sign = faced_sign;
            c = '\0';
            tag = the_tag;
        }

        Quary(int faced_sign, char faced_char) {
            sign = faced_sign;
            c = faced_char;
            tag = 0;
        }

        //@Override
        public int hashCode() {
            return (this.tag == 0 ? 0 : 307) + c + this.sign * 407;
        }

        //@Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (null == obj) return false;
            if (this.getClass() != obj.getClass()) return false;
            Quary qry = (Quary) obj;
            if (this.sign != qry.sign) return false;
            if (this.tag != qry.tag) return false;
            if (this.tag != 0) return true;
            return this.c == qry.c;
        }
    }

    static int signsz = Sign.SIGN_SIZE;

    private static int new_sign() {
        int r = signsz;
        signsz++;
        return r;
    }

    private static boolean automaton_tokenize(List<Code_Token> main_list, Map<Quary, Vector<Integer>> automaton, String code) {
        Set<Integer> end_states = new HashSet<Integer>();//终结态集合
        Map<Integer, Sign> anti_table = new HashMap<Integer, Sign>();//int到sign反向映射表
        anti_table.put(Sign.tokenizer_state_identifier_or_reserved.ordinal(), Sign.tokenizer_state_identifier_or_reserved);
        anti_table.put(Sign.tokenizer_state_char.ordinal(), Sign.tokenizer_state_char);
        anti_table.put(Sign.tokenizer_state_delimiter.ordinal(), Sign.tokenizer_state_delimiter);
        anti_table.put(Sign.tokenizer_state_number.ordinal(), Sign.tokenizer_state_number);
        anti_table.put(Sign.tokenizer_state_operator.ordinal(), Sign.tokenizer_state_operator);
        anti_table.put(Sign.tokenizer_state_string.ordinal(), Sign.tokenizer_state_string);
        end_states.addAll(anti_table.keySet());
        //现在从code中读取词语，按规则分词然后放入main_list中
        int text_pointer = 0;
        int line = 0;
        int column = 0;
        //循环分词
        while (text_pointer < code.length()) {//只要这个Code_Token的词没有被吃完
            List<Integer> current_state = new LinkedList<Integer>();//无εNFA的多重状态处理
            current_state.add(Sign.tokenizer_state_zero.ordinal());//初始状态
            Code_Token new_token = new Code_Token();//每次都新建一个词
            new_token.word = "";
            new_token.line = line;
            new_token.column = column;//词初始化，给定位置
            new_token.legal = false;
            while (true) {
                //开始执行自动机处理
                char ch = text_pointer < code.length() ? code.charAt(text_pointer) : '\0'; //得到字符。超出字符则得到‘\0’
                //System.out.println(current_state+"  "+ch);
                //System.out.println(automaton.containsKey(new Quary(current_state, -1)));
                //开始状态转换
                List<Integer> transfering_state = current_state;
                current_state = new LinkedList<Integer>();
                //System.out.println(""+transfering_state+" meet with "+(ch=='\n'?"\\n":ch));
                for (int one_state : transfering_state) {
                    //System.out.println(one_state);
                    if (automaton.containsKey(new Quary(one_state, ch))) {
                        current_state.addAll(automaton.get(new Quary(one_state, ch)));//进行转换
                    } else {//如果没有找到对应的转换
                        if (automaton.containsKey(new Quary(one_state, -1))) {//尝试寻找“其他”转换
                            current_state.addAll(automaton.get(new Quary(one_state, -1)));//进行转换
                        } else {
                        }//没有转换
                    }
                }
                //现在current_state变成了状态转移后的状态
                //System.out.println("get "+current_state);
                List<Integer> full_state = current_state;
                current_state = new LinkedList<Integer>();
                boolean ended = true;
                int end_with_state = -1;
                for (Integer one_state : full_state) {
                    if (one_state == Sign.tokenizer_state_error.ordinal()) {
                        continue;
                    }//进入了错误态就应该移除
                    current_state.add(one_state);//不是错误态就加进去
                    if (!end_states.contains(one_state)) {//如果不是终结态
                        ended = false;//表示至少存在一个非终结态
                    } else {
                        end_with_state = one_state;//自动机要确保同时只有一个终结态
                    }
                }
                if (current_state.size() == 0) {//全都是错误态，那么就是出现了错误
                    new_token.sign = Sign.tokenizer_state_error;
                    new_token.legal = false;
                    //指针前进
                    new_token.word += ch;//吃进字符
                    column++;
                    if (ch == '\n') {
                        line++;
                        column = 0;
                    }
                    text_pointer++;
                    main_list.add(new_token);
                    break;
                }
                if (ended) {//已经找到结束状态
                    new_token.sign = anti_table.get(end_with_state);
                    new_token.legal = true;
                    main_list.add(new_token);//把这个token添加到main_list中
                    break;//退出循环
                }
                //到这里为止既不是结束态也不是全错误态，至少有一个非结束状态
                //前进一个字符
                new_token.word += ch;//吃进字符
                column++;
                if (ch == '\n') {
                    line++;
                    column = 0;
                }
                text_pointer++;
                if (current_state.contains(Sign.tokenizer_state_zero.ordinal())) {//回到起始态。自动机要确保回到起始态时不能有其他叠加状态
                    break;//放弃词语，继续扫描
                }
                if (current_state.contains(Sign.tokenizer_state_eof.ordinal())) {//文件已经结束。进入eof态一定是从0状态读入EOF而进入
                    break;
                }
                //非终结态也非eof也非起始态

            }
            //进入终结态之后就到这里。如果词还没有被吃完，就再次分词，否则退出循环。
        }
        return true;
    }

    private static void set_automaton_add_P(Map<Quary, Vector<Integer>> automaton, int sign, String set, int b) {
        Quary quary;
        for (int i = 0; i < set.length(); i++) {
            quary = new Quary(sign, set.charAt(i));
            if (!automaton.containsKey(quary)) automaton.put(quary, new Vector<Integer>());
            automaton.get(quary).add(b);
        }
    }

    private static void set_automaton_add_P(Map<Quary, Vector<Integer>> automaton, int sign, int sp, int b) {
        Quary quary = new Quary(sign, sp);
        if (!automaton.containsKey(quary)) automaton.put(quary, new Vector<Integer>());
        automaton.get(quary).add(b);
    }

    private static Map<Quary, Vector<Integer>> set_automaton() {
        Map<Quary, Vector<Integer>> ret = new HashMap<Quary, Vector<Integer>>();
        String[] operators = ":: -> . ++ -- ~ ! - + & * / % << >> < <= > >= == != ^ | && || ? : = += -= *= /= %= >>= <<= &= ^= !=".split(" ");
        int symbol1, symbol2, symbol3, symbol4, symbol5, symbol6, symbol7, symbol8, symbol9, symbol10, symbol11;
        Quary quary;
        String letters = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        String numbers = "1234567890";
        String dic = "()[]{};,";
        //String opec = "+-*/%><=!&|^?:~.";
        String temp;
        int zero = Sign.tokenizer_state_zero.ordinal();
        //标识符--------------------------------------------------------------------------------------------------------
        symbol1 = new_sign();
        for (int i = 0; i < letters.length(); i++) {
            quary = new Quary(zero, letters.charAt(i));
            if (!ret.containsKey(quary)) ret.put(quary, new Vector<Integer>());
            ret.get(quary).add(symbol1);
        }
        quary = new Quary(zero, '_');
        if (!ret.containsKey(quary)) ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(symbol1);

        temp = letters + numbers + "_";
        for (int i = 0; i < temp.length(); i++) {
            quary = new Quary(symbol1, temp.charAt(i));
            if (!ret.containsKey(quary)) ret.put(quary, new Vector<Integer>());
            ret.get(quary).add(symbol1);
        }
        quary = new Quary(symbol1, -1);
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(Sign.tokenizer_state_identifier_or_reserved.ordinal());
        //字符串--------------------------------------------------------------------------------------------------------
        symbol1 = new_sign();
        symbol2 = new_sign();
        symbol3 = new_sign();

        quary = new Quary(zero, '\"');
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(symbol1);

        quary = new Quary(symbol1, '\\');
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(symbol2);
        quary = new Quary(symbol1, '\"');
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(symbol3);
        quary = new Quary(symbol1, '\n');
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(Sign.tokenizer_state_error.ordinal());
        quary = new Quary(symbol1, '\0');
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(Sign.tokenizer_state_error.ordinal());
        quary = new Quary(symbol1, -1);
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(symbol1);

        quary = new Quary(symbol2, -1);
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(symbol1);
        quary = new Quary(symbol2, '\n');
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(Sign.tokenizer_state_error.ordinal());
        quary = new Quary(symbol2, '\0');
        ret.put(quary, new Vector<Integer>());
        ret.get(quary).add(Sign.tokenizer_state_error.ordinal());

        set_automaton_add_P(ret, symbol3, -1, Sign.tokenizer_state_string.ordinal());// (3,other)->Success

        //字符--------------------------------------------------------------------------------------------------------
        symbol1 = new_sign();
        symbol2 = new_sign();
        symbol3 = new_sign();
        symbol4 = new_sign();
        set_automaton_add_P(ret, zero, "'", symbol1);
        set_automaton_add_P(ret, symbol1, "\\", symbol2);
        set_automaton_add_P(ret, symbol1, -1, symbol3);
        set_automaton_add_P(ret, symbol1, "'\n\0", Sign.tokenizer_state_error.ordinal());

        set_automaton_add_P(ret, symbol2, "\n\0", Sign.tokenizer_state_error.ordinal());
        set_automaton_add_P(ret, symbol2, -1, symbol3);

        set_automaton_add_P(ret, symbol3, "'", symbol4);
        set_automaton_add_P(ret, symbol3, "\0", Sign.tokenizer_state_error.ordinal());
        set_automaton_add_P(ret, symbol4, -1, Sign.tokenizer_state_char.ordinal());
        //介符--------------------------------------------------------------------------------------------------------
        symbol1 = new_sign();
        set_automaton_add_P(ret, zero, "()[]{};,", symbol1);
        set_automaton_add_P(ret, symbol1, -1, Sign.tokenizer_state_delimiter.ordinal());
        //数字常量（无论对错）-----------------------------------------------------------------------------------------
        symbol1 = new_sign();
        symbol2 = new_sign();
        symbol3 = new_sign();
        symbol4 = new_sign();
        symbol5 = new_sign();
        symbol6 = new_sign();
        symbol7 = new_sign();
        symbol8 = new_sign();
        symbol9 = new_sign();
        symbol10 = new_sign();
        symbol11 = new_sign();
        //十进制数部分
        set_automaton_add_P(ret, zero, numbers, symbol1);
        set_automaton_add_P(ret, zero, ".", symbol2);
        set_automaton_add_P(ret, zero, numbers, symbol3);

        set_automaton_add_P(ret, symbol1, numbers, symbol1);
        set_automaton_add_P(ret, symbol1, ".", symbol2);
        set_automaton_add_P(ret, symbol2, numbers, symbol3);

        set_automaton_add_P(ret, symbol3, "Ee", symbol4);
        set_automaton_add_P(ret, symbol3, numbers, symbol3);
        set_automaton_add_P(ret, symbol3, -1, Sign.tokenizer_state_number.ordinal());
        set_automaton_add_P(ret, symbol3, "FfDdLl", symbol8);
        set_automaton_add_P(ret, symbol3, numbers + letters + "_.", Sign.tokenizer_state_error.ordinal());

        set_automaton_add_P(ret, symbol4, "+-", symbol5);
        set_automaton_add_P(ret, symbol5, numbers, symbol6);
        set_automaton_add_P(ret, symbol4, numbers, symbol6);
        set_automaton_add_P(ret, symbol6, numbers + letters + "_.", Sign.tokenizer_state_error.ordinal());

        set_automaton_add_P(ret, symbol6, numbers, symbol6);
        set_automaton_add_P(ret, symbol6, "FfDdLlQqWw", symbol7);
        set_automaton_add_P(ret, symbol6, -1, Sign.tokenizer_state_number.ordinal());
        set_automaton_add_P(ret, symbol7, -1, Sign.tokenizer_state_number.ordinal());
        set_automaton_add_P(ret, symbol7, numbers + letters + "_.", Sign.tokenizer_state_error.ordinal());

        set_automaton_add_P(ret, symbol8, -1, Sign.tokenizer_state_number.ordinal());
        set_automaton_add_P(ret, symbol8, numbers + letters + "_.", Sign.tokenizer_state_error.ordinal());
        //十六进制数部分
        set_automaton_add_P(ret, zero, "0", symbol9);
        set_automaton_add_P(ret, symbol9, "Xx", symbol10);
        set_automaton_add_P(ret, symbol10, numbers + "AaBbCcDdEeFf", symbol11);

        set_automaton_add_P(ret, symbol11, numbers + "AaBbCcDdEeFf", symbol11);
        set_automaton_add_P(ret, symbol11, "Ll", symbol8);
        set_automaton_add_P(ret, symbol11, numbers + letters + "_.", Sign.tokenizer_state_error.ordinal());
        set_automaton_add_P(ret, symbol11, -1, Sign.tokenizer_state_number.ordinal());
        //运算符--------------------------------------------------------------------------------------------------
        for (String operator : operators) {
            symbol1 = zero;
            for (int i = 0; i < operator.length(); i++) {
                symbol2 = new_sign();
                set_automaton_add_P(ret, symbol1, operator.substring(i, i + 1), symbol2);
                symbol1 = symbol2;
            }
            set_automaton_add_P(ret, symbol1, -1, Sign.tokenizer_state_operator.ordinal());

        }
        //set_automaton_add_P(ret, zero, opec, Sign.tokenizer_state_need_judge.ordinal());
        //set_automaton_add_P(ret, Sign.tokenizer_state_need_judge.ordinal(), opec, Sign.tokenizer_state_need_judge.ordinal());
        //set_automaton_add_P(ret, Sign.tokenizer_state_need_judge.ordinal(), -1, Sign.tokenizer_state_operator.ordinal());
        //注释------------------------------------------------------------------------------------------------------
        symbol1 = new_sign();
        symbol2 = new_sign();
        symbol3 = new_sign();
        symbol4 = new_sign();
        set_automaton_add_P(ret, zero, "/", symbol1);
        set_automaton_add_P(ret, symbol1, "/", symbol2);
        set_automaton_add_P(ret, symbol1, "*", symbol3);

        set_automaton_add_P(ret, symbol2, -1, symbol2);
        set_automaton_add_P(ret, symbol2, "\n\0", zero);

        set_automaton_add_P(ret, symbol3, "\0", Sign.tokenizer_state_error.ordinal());
        set_automaton_add_P(ret, symbol3, "*", symbol4);
        set_automaton_add_P(ret, symbol3, -1, symbol3);

        set_automaton_add_P(ret, symbol4, "*", symbol4);
        set_automaton_add_P(ret, symbol4, "/", zero);
        set_automaton_add_P(ret, symbol4, "\0", Sign.tokenizer_state_error.ordinal());
        set_automaton_add_P(ret, symbol4, -1, symbol3);
        //换行，空格和结尾
        set_automaton_add_P(ret, zero, "\n \t", zero);
        set_automaton_add_P(ret, zero, "\0", Sign.tokenizer_state_eof.ordinal());


        return ret;
    }

    private static void token_retype(List<Code_Token> main_list) {
        for (Code_Token one_token : main_list) {
            switch (one_token.sign) {
                case tokenizer_state_char:
                    break;
                case tokenizer_state_identifier_or_reserved:
                    if (one_token.word.equals("struct")) {
                        one_token.sign = Sign.STRUCT;
                        break;
                    }
                    if (one_token.word.equals("return")) {
                        one_token.sign = Sign.RETURN;
                        break;
                    }
                    if (one_token.word.equals("if")) {
                        one_token.sign = Sign.IF;
                        break;
                    }
                    if (one_token.word.equals("else")) {
                        one_token.sign = Sign.ELSE;
                        break;
                    }
                    if (one_token.word.equals("while")) {
                        one_token.sign = Sign.WHILE;
                        break;
                    }

                    if (one_token.word.equals("int")) {
                        one_token.sign = Sign.TYPE;
                        break;
                    }
                    if (one_token.word.equals("float")) {
                        one_token.sign = Sign.TYPE;
                        break;
                    }
                    one_token.sign = Sign.ID;
                    break;
                case tokenizer_state_operator:
                    if (one_token.word.equals("=")) {
                        one_token.sign = Sign.ASSIGNOP;
                        break;
                    }
                    if (one_token.word.equals(">") || one_token.word.equals("<")) {
                        one_token.sign = Sign.RELOP;
                        break;
                    }
                    if (one_token.word.equals(">=") || one_token.word.equals("<=")) {
                        one_token.sign = Sign.RELOP;
                        break;
                    }
                    if (one_token.word.equals("==") || one_token.word.equals("!=")) {
                        one_token.sign = Sign.RELOP;
                        break;
                    }

                    if (one_token.word.equals("+")) {
                        one_token.sign = Sign.PLUS;
                        break;
                    }
                    if (one_token.word.equals("-")) {
                        one_token.sign = Sign.MINUS;
                        break;
                    }
                    if (one_token.word.equals("*")) {
                        one_token.sign = Sign.STAR;
                        break;
                    }
                    if (one_token.word.equals("/")) {
                        one_token.sign = Sign.DIV;
                        break;
                    }
                    if (one_token.word.equals("&&")) {
                        one_token.sign = Sign.AND;
                        break;
                    }
                    if (one_token.word.equals("||")) {
                        one_token.sign = Sign.OR;
                        break;
                    }
                    if (one_token.word.equals(".")) {
                        one_token.sign = Sign.DOT;
                        break;
                    }
                    if (one_token.word.equals("!")) {
                        one_token.sign = Sign.NOT;
                        break;
                    }
                    break;
                case tokenizer_state_delimiter:
                    switch (one_token.word.charAt(0)) {
                        case ';':
                            one_token.sign = Sign.SEMI;
                            break;
                        case ',':
                            one_token.sign = Sign.COMMA;
                            break;
                        case '(':
                            one_token.sign = Sign.LP;
                            break;
                        case ')':
                            one_token.sign = Sign.RP;
                            break;
                        case '[':
                            one_token.sign = Sign.LB;
                            break;
                        case ']':
                            one_token.sign = Sign.RB;
                            break;
                        case '{':
                            one_token.sign = Sign.LC;
                            break;
                        case '}':
                            one_token.sign = Sign.RC;
                            break;
                    }
                    break;
                case tokenizer_state_string:
                    break;
                case tokenizer_state_number:
                    if (one_token.word.length() >= 3) {
                        if (one_token.word.charAt(1) == 'x' || one_token.word.charAt(1) == 'X') {
                            one_token.sign = Sign.INT;
                            break;
                        }
                    }
                    if (one_token.word.contains("f") || one_token.word.contains("F")) {
                        one_token.sign = Sign.FLOAT;
                        break;
                    }
                    if (one_token.word.contains(".") && !one_token.word.contains("d") && !one_token.word.contains("D")
                            && !one_token.word.contains("l") && !one_token.word.contains("L")) {
                        one_token.sign = Sign.FLOAT;
                        break;
                    }
                    one_token.sign = Sign.INT;
                    break;
            }
        }
    }

    public static List<Code_Token> tokenize(String code) {
        Map<Quary, Vector<Integer>> automaton = set_automaton();
        /*
        //输出产生式用的
        for(Quary key:automaton.keySet())
        {
            String ou = ""+key.sign+" ";
            if(key.c=='\n'){ou+="\\n";}
            else if(key.c=='\0'){ou+="EOF";}
            else{ou+=key.c;}
            ou = ou + " -> ";
            Vector<Integer> tmp = automaton.get(key);
            for(int i=0; i<tmp.size();i++)
            {
                System.out.println(ou+tmp.get(i));
            }
        }*/

        List<Code_Token> main_list = new LinkedList<Code_Token>();
        boolean result;
        result = automaton_tokenize(main_list, automaton, code);
        token_retype(main_list);
        return main_list;
    }
}
