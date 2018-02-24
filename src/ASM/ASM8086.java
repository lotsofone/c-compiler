package ASM;

import Inter.QuaterExpression;
import Symbols.*;
import Lexer.Code_Token;
import Parser.Sign;

import java.util.*;

public class ASM8086 {
    private Map<String, Set<String>> identifiers_in_functions = new HashMap<String, Set<String>>();//函数的id集合
    private Set<String> identifiers_global = new HashSet<String>();//全局的变量
    private Map<String, Map<String, ID_info>> identifiers_info_in_func = new HashMap<String, Map<String, ID_info>>();//函数内id的信息
    private Map<String, Integer> functions_spaces = new HashMap<String, Integer>();//函数运行需要的内存空间
    private Map<String, ID_info> functions_return_info = new HashMap<String, ID_info>();//函数返回值的信息
    private class Arrayinfo{
        int unit_size = 0; int length = 0; int offset = 0;
    }
    private class ID_info{
        int offset=0; int size=0;
        public ID_info(){        ;}
        public ID_info(ID_info other){
            this.offset = other.offset; this.size = other.size;
        }
    }
    private Map<String, Map<String, Arrayinfo>> arrays_info_in_func = new HashMap<String, Map<String, Arrayinfo>>();//数组内单位元素的信息表
    private List<QuaterExpression> quaterexpressions = new LinkedList<QuaterExpression>();
    private SymbolTable symboltable;
    private void _add_dseg(StringBuffer code){
        code.append("DSEG SEGMENT\n");

        code.append("DSEG ENDS\n");
    }
    private void _add_sseg(StringBuffer code, int length){
        code.append("SSEG SEGMENT STACK\n");
        code.append("STK DB "+length+" DUP(?)\n");
        code.append("SSEG ENDS\n");
    }
    private String id_BPaddress_exp(String id, String positioned_func){//给出一个标识符，和所在函数，给出汇编寻址表示
        if(positioned_func.equals("")){//全局变量
            if(!identifiers_global.contains(id))throw new RuntimeException("尝试对未声明的全局变量寻址");
            return id;
        }else{//局部变量
            Set<String > idset = identifiers_in_functions.get(positioned_func);
            if(!idset.contains(id))throw new RuntimeException("尝试对未声明的局部变量寻址:"+id+" at "+positioned_func);
            int offset = identifiers_info_in_func.get(positioned_func).get(id).offset;
            return "[BP+"+offset+"]";
        }
    }//给出一个标识符，和所在函数，给出汇编寻址表示
    public void set_symboltable(SymbolTable s){this.symboltable = s;}
    private String arg_string_exp(Code_Token token, String positioned_func){//给一个四元式参数，和所在函数，给出其汇编表示
        if(token.sign == Sign.ID)return id_BPaddress_exp(token.word, positioned_func);//变量
        return token.word;//常数
    }
    private void _add_cseg_interfunc(StringBuffer code){
        code.append("\n" +
                "    printlln PROC NEAR\n" +
                "    MOV AH,02H\n" +
                "    MOV DL,10\n" +
                "    INT 21H\n" +
                "    RET\n" +
                "    printlln ENDP\n" +
                "    scani PROC NEAR\n" +
                "    MOV BX,0\n" +
                "    PUSH BX\n" +
                "scanire1:\n" +
                "    POP CX\n" +
                "    MOV AH,01H\n" +
                "    INT 21H\n" +
                "    CMP AL,\"-\"\n" +
                "    PUSHF\n" +
                "\n" +
                "    \n" +
                "    CMP AL,\"0\"\n" +
                "    JB scanire1\n" +
                "    CMP AL,\"9\"\n" +
                "    JA scanire1\n" +
                "    \n" +
                "    SUB AL,30H\n" +
                "    MOV AH,0\n" +
                "    ADD BX,AX\n" +
                "    \n" +
                "    POPF\n" +
                "scanirld:\n" +
                "    MOV AH,01H\n" +
                "    INT 21H\n" +
                "    CMP AL,\"0\"\n" +
                "    JB scaniend\n" +
                "    CMP AL,\"9\"\n" +
                "    JA scaniend\n" +
                "    PUSH AX\n" +
                "    MOV AX,BX\n" +
                "    MOV DX,10\n" +
                "    MUL DX\n" +
                "    MOV BX,AX\n" +
                "    POP AX\n" +
                "    SUB AL,30H\n" +
                "    MOV AH,0\n" +
                "    ADD BX,AX\n" +
                "    JMP scanirld\n" +
                "scaniend:\n" +
                "    PUSH CX\n" +
                "    POPF\n" +
                "    JNZ scaninng\n" +
                "    NEG BX\n" +
                "scaninng:\n" +
                "    MOV BP,SP\n" +
                "    MOV [BP+2],BX\n" +
                "    RET\n" +
                "    scani ENDP\n");
        code.append("    printc PROC NEAR\n" +
                "    MOV BP,SP\n" +
                "    \n" +
                "    MOV DL,[BP+2]\n" +
                "    MOV AH,02H\n" +
                "    INT 21H\n" +
                "    \n" +
                "    RET\n" +
                "    printc ENDP\n");
        code.append("    printi PROC NEAR\n" +
                "    \n" +
                "    MOV BP,SP\n" +
                "    MOV AX,[BP+2]\n" +
                "    CMP AX,0\n" +
                "    JGE printinn\n" +
                "    MOV DL,\"-\"\n" +
                "    MOV AH,02H\n" +
                "    INT 21H\n" +
                "    MOV AX,[BP+2]\n" +
                "    NEG AX\n" +
                "    CMP AX,8000H\n" +
                "    JNE printinn\n" +
                "    MOV CX,8\n" +
                "    PUSH CX\n" +
                "    MOV AX,3276\n" +
                "    MOV CX,10\n" +
                "    JMP printidf\n" +
                "printinn:\n" +
                "    MOV CX,10\n" +
                "    CWD\n" +
                "    DIV CX\n" +
                "    PUSH DX\n" +
                "printidf:\n" +
                "    CWD\n" +
                "    DIV CX\n" +
                "    PUSH DX\n" +
                "    CWD\n" +
                "    DIV CX\n" +
                "    PUSH DX\n" +
                "    CWD\n" +
                "    DIV CX\n" +
                "    PUSH DX\n" +
                "    CWD\n" +
                "    DIV CX\n" +
                "    PUSH DX\n" +
                "    \n" +
                "    MOV CX,5\n" +
                "\n" +
                "printizr:\n" +
                "    POP DX\n" +
                "    CMP DL,0\n" +
                "    JNZ printigo\n" +
                "    CMP CL,1\n" +
                "    JZ printigo\n" +
                "    LOOP printizr\n" +
                "    \n" +
                "printiag:\n" +
                "    POP DX\n" +
                "printigo:\n" +
                "    ADD DL,30H\n" +
                "    MOV AH,02H\n" +
                "    INT 21H\n" +
                "    LOOP printiag\n" +
                "    \n" +
                "    MOV DL,20H\n" +
                "    MOV AH,02H\n" +
                "    INT 21H\n" +
                "    \n" +
                "    RET\n" +
                "    printi ENDP\n");
    }
    private void _add_cseg(StringBuffer code){
        //头部
        code.append("CSEG SEGMENT\n");
        code.append("ASSUME CS:CSEG,DS:DSEG,SS:SSEG\n");
        code.append("START:\n" +
                "    MOV AX,DSEG\n" +
                "    MOV DS,AX\n" +
                "    SUB SP,2\n" +
                "    CALL main\n" +
                "    ADD SP,2\n" +
                "    MOV AH,4CH\n" +
                "    INT 21H\n");
        //手动添加的函数
        _add_cseg_interfunc(code);
        //添加四元式过程中用的缓存部分
        Vector<Code_Token> put_arguments = new Vector<Code_Token>();
        //所在函数
        boolean in_function = false; String in_function_name = "";
        //申请label
        String L1,L2,L3,L4;
        //接下来依次添加四元式
        for(QuaterExpression one_qtr:quaterexpressions){
            if(in_function==false && !in_function_name.equals(""))throw new RuntimeException("in_function与in_function_name不同步");
//            code.append(";"+one_qtr+"    "+one_qtr.operation.sign+"\n");
//            System.out.println(""+one_qtr+" "+one_qtr.operation.sign);
            switch(one_qtr.operation.sign){
                case ASSIGNOP:
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case PLUS:
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                            "    ADD AX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case MINUS:
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                            "    SUB AX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case STAR:
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                            "    CWD\n" +
                            "    MOV BX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +
                            "    IMUL BX\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case DIV:
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                            "    CWD\n" +
                            "    MOV BX," + arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +
                            "    IDIV BX\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case RELOP: //  RELOP, arg1, arg2, result
                    L1 = symboltable.ID_alloc();
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +//MOV AX,arg1
                            "    CMP AX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +//CMP AX,arg2
                            "    MOV AX,1\n");//MOV AX,1  这是真值
                    if(one_qtr.operation.word.equals("==")){code.append("    JE "+L1+"\n");}
                    if(one_qtr.operation.word.equals("!=")){code.append("    JNE "+L1+"\n");}
                    if(one_qtr.operation.word.equals(">=")){code.append("    JGE "+L1+"\n");}
                    if(one_qtr.operation.word.equals("<=")){code.append("    JLE "+L1+"\n");}
                    if(one_qtr.operation.word.equals(">")){code.append("    JG "+L1+"\n");}
                    if(one_qtr.operation.word.equals("<")){code.append("    JL "+L1+"\n");}
                    code.append("    MOV AX,0\n");
                    code.append(L1+":\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");//MOV result,AX
                    break;
                case AND:
                    L1 = symboltable.ID_alloc();L2 = symboltable.ID_alloc();
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                            "    MOV BX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +
                            "    CMP AX,0\n" +
                            "    MOV AX,1\n" +
                            "    JNZ "+L1+"\n" +    //JNZ L1
                            "    MOV AX,0\n" +
                            ""+L1+":\n" +
                            "    CMP BX,0\n" +
                            "    MOV BX,1\n" +
                            "    JNZ "+L2+"\n" +    //JNZ L2
                            "    MOV BX,0\n" +
                            ""+L2+":\n" +
                            "    AND AX,BX\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case OR:
                    L1 = symboltable.ID_alloc();L2 = symboltable.ID_alloc();
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                                    "    MOV BX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +
                                    "    CMP AX,0\n" +
                                    "    MOV AX,1\n" +
                                    "    JNZ "+L1+"\n" +    //JNZ L1
                                    "    MOV AX,0\n" +
                                    ""+L1+":\n" +
                                    "    CMP BX,0\n" +
                                    "    MOV BX,1\n" +
                                    "    JNZ "+L2+"\n" +    //JNZ L2
                                    "    MOV BX,0\n" +
                                    ""+L2+":\n" +
                                    "    OR AX,BX\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case NOT:
                    L1 = symboltable.ID_alloc();
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +
                                    "    CMP AX,0\n" +
                                    "    MOV AX,1\n" +
                                    "    JZ "+L1+"\n" +    //JZ L1
                                    "    MOV AX,0\n" +
                                    ""+L1+":\n" +
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");
                    break;
                case Qarrayin:
                    ID_info id_info = identifiers_info_in_func.get(in_function_name).get(one_qtr.result.word);
                    Arrayinfo arrayinfo = arrays_info_in_func.get(in_function_name).get(one_qtr.result.word);
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +//    MOV AX,arg2
                            "    MOV CX,"+arrayinfo.unit_size+"\n" +//    MOV CX,a.unit_length
                            "    MUL CL\n" +//    MUL CL
                            "    MOV SI,AX\n" +//    MOV SI,AX
                            "    MOV AX,"+arg_string_exp(one_qtr.arg1, in_function_name)+"\n" +//    MOV AX,arg1
                            "    MOV SS:[BP+SI+"+id_info.offset+"],AX\n");//    MOV SS:[BP+SI+offset result],AX
                    break;
                case Qarrayout:
                    id_info = identifiers_info_in_func.get(in_function_name).get(one_qtr.arg1.word);
                    arrayinfo = arrays_info_in_func.get(in_function_name).get(one_qtr.arg1.word);
                    code.append("    MOV AX,"+arg_string_exp(one_qtr.arg2, in_function_name)+"\n" +//    MOV AX,arg2
                            "    MOV CX,"+arrayinfo.unit_size+"\n" +//    MOV CX,a.unit_length
                            "    MUL CL\n" +//    MUL CL
                            "    MOV SI,AX\n" +//    MOV SI,AX
                            "    MOV AX,SS:[BP+SI+"+id_info.offset+"]\n" +//    MOV AX,SS:[BP+SI+offset arg1]
                            "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");//    MOV result,AX
                    break;
                case Qfunc:
                    int func_vars_size = functions_spaces.get(one_qtr.arg1.word);//声明的所有变量的size总和
                    //输出代码
                    code.append("    "+ one_qtr.arg1+" PROC NEAR\n");
                    code.append("    SUB SP,"+func_vars_size+"\n    MOV BP,SP\n");
                    in_function = true; in_function_name = one_qtr.arg1.word;

                    break;
                case Qendfunc:
                    //为了避免没有RET，一律添加一份RET
                    code.append("    ADD SP,"+functions_spaces.get(in_function_name)+"\n");
                    code.append("    RET\n");
                    code.append("    "+ one_qtr.arg1.word+" ENDP\n");
                    in_function = false; in_function_name = "";
                    break;
                case Qreturn:
                    if(one_qtr.arg1!=null) {//指定了返回值
                        int rtoffset = functions_return_info.get(in_function_name).offset;//= symboltable.functionArgsSize(one_qtr.arg1.word).lastElement().offset;//返回的offset
                        rtoffset = rtoffset;
                        code.append("    MOV AX,"+arg_string_exp(one_qtr.arg1,in_function_name)+"\n" +//MOV AX,arg1
                                "    MOV [BP+"+rtoffset+"],AX\n");//MOV [BP+rtoffset],AX
                    }
                    code.append("    ADD SP,"+functions_spaces.get(in_function_name)+"\n");
                    code.append("    RET\n");
                    break;
                case Qdeclare: break;
                case Qif://if 语句  (if, arg1, L1, L2)
                    code.append("    MOV AX," +arg_string_exp(one_qtr.arg1, in_function_name)+"\n"+//MOV AX,arg1
                            "    CMP AX,0\n" + // CMP AX,0
                            "    JNE "+one_qtr.arg2.word+"\n" + // JNE L1
                            "    JMP "+one_qtr.result.word+"\n");// JMP L2
                    break;
                case Qgoto:
                    code.append("    JMP "+one_qtr.arg1.word+"\n");
                    break;
                case Qlabel:
                    code.append(one_qtr.arg1.word+":\n");
                    break;
                case Qputarg:
                    put_arguments.add(one_qtr.arg1);//把arg1加入到参数列表里面
                    break;
                case Qtakearg:
                    break;
                case Qcall:
                    int rtsize = 2;//返回类型的size
                    code.append("    SUB SP,"+rtsize+"\n");//SUB SP,rtsize  分配返回值空间

                    int arguments_size = 0;//一个个输入参数
                    for(int i=0; i<put_arguments.size(); i++){
                        code.append("    MOV AX,"+arg_string_exp(put_arguments.get(i), in_function_name)+"\n" +//MOV AX,arg1
                                "    PUSH AX\n");//PUSH AX
                        arguments_size+=2;//输入参数的总大小
                    }
                    put_arguments.clear();
                    code.append("    CALL "+one_qtr.arg1.word+"\n");//    CALL arg1
                    code.append("    ADD SP,"+arguments_size+"\n");//    ADD SP,arguments_size
                    if(one_qtr.result!=null){//有接收返回值的变量
                        code.append("    MOV BP,SP\n" +//MOV BP,SP
                                "    MOV AX,[BP]\n" +//MOV AX,SS:[BP]  把返回值放入AX
                                "    ADD BP,"+rtsize+"\n" +//ADD BP,rtsize
                                "    MOV "+arg_string_exp(one_qtr.result, in_function_name)+",AX\n");//MOV result,AX
                    }
                    code.append("    ADD SP," + rtsize + "\n");//ADD SP,rtsize  归还返回值空间
                    code.append("    MOV BP,SP\n");//    MOV BP,SP
                    break;
            }
        }
        //尾部
        code.append("CSEG ENDS\n"+
                "    END START\n");
    }
    private void scan_identifier_list(){//扫描所有的声明并添加进 全局变量标识符表identifiers_global 函数变量标识符表  identifiers_in_functions   数组信息 arrays_info_in_func
        this.identifiers_global.clear();this.identifiers_in_functions.clear();this.arrays_info_in_func.clear();//清除原有的东西
        boolean in_function = false;
        String in_function_name = "";
        String in_array_name = ""; boolean in_array = false;
        for(QuaterExpression oneqtr:quaterexpressions) {
            switch(oneqtr.operation.sign){
                case Qfunc:
                    if (in_function) throw new RuntimeException("发现尝试在函数内嵌套函数");
                    if (identifiers_in_functions.containsKey(oneqtr.arg1.word)) throw new RuntimeException("发现函数重复声明");
                    in_function_name = oneqtr.arg1.word;
                    in_function = true;
                    identifiers_in_functions.put(in_function_name, new HashSet<String>());
                    arrays_info_in_func.put(in_function_name, new HashMap<String, Arrayinfo>());
                    break;
                case Qendfunc:
                    if (!in_function) throw new RuntimeException("未在函数时遇到Qendfunc");
                    if (!in_function_name.equals(oneqtr.arg1.word))
                        throw new RuntimeException("Qendfunc的函数名" + oneqtr.arg1.word + "与Qfunc的函数名" + oneqtr.arg1.word + "不一致");
                    in_function = false; in_function_name = "";
                    break;
                case Qarray:
                    if(!in_function)throw new RuntimeException("暂时没有支持声明全局数组");
                    if(in_array)throw new RuntimeException("发现尝试在数组"+in_array_name+"内嵌套数组"+oneqtr.arg1.word);
                    Map<String, Arrayinfo> arrset = arrays_info_in_func.get(in_function_name);
                    if(arrset.containsKey(oneqtr.arg1.word)) throw new RuntimeException("发现数组重复声明:"+oneqtr.arg1.word);
                    in_array = true; in_array_name = oneqtr.arg1.word;
                    Arrayinfo arrayinfo = new Arrayinfo(); arrayinfo.unit_size = -1; arrayinfo.length = Integer.valueOf(oneqtr.arg2.word);
                    arrset.put(oneqtr.arg1.word, arrayinfo);//为这个数组id添加一个单位长度为-1的记录

                    Set<String> idset;
                    if (in_function) {
                        idset = identifiers_in_functions.get(in_function_name);
                    } else {
                        idset = identifiers_global;
                    }
                    idset.add(oneqtr.arg1.word);
                    break;
                case Qarrayend:
                    if(!in_array) throw new RuntimeException("未遇到Qarray时遇到Qarrayend");
                    //if(!in_array_name.equals(oneqtr.arg1.word))throw new RuntimeException("Qarrayend的数组名与Qarray不一致");
                    in_array = false; in_array_name = "";
                    break;
                case Qdeclare:
                case Qtemp_declare:
                    if(!in_array) {//不是数组内的声明
                        if (in_function) {
                            idset = identifiers_in_functions.get(in_function_name);
                        } else {
                            idset = identifiers_global;
                        }
                        if (idset.contains(oneqtr.arg1.word)) throw new RuntimeException("重复声明变量：" + oneqtr.arg1.word);
                        idset.add(oneqtr.arg1.word);
                        break;
                    }else{//是在数组内的声明
                        arrayinfo = arrays_info_in_func.get(in_function_name).get(in_array_name);
                        if(arrayinfo.unit_size>0)throw new RuntimeException("数组内重复声明单位类型" + in_array_name);
                        arrayinfo.unit_size=2;
                        break;
                    }
            }
        }
    }//扫描所有的声明并添加进 全局变量标识符表identifiers_global
    // 函数变量标识符表  identifiers_in_functions   数组信息 arrays_info_in_func
    private void count_offsets(){//计算函数占用空间与该函数的标识符的偏移量
        functions_spaces.clear();
        identifiers_info_in_func.clear();
        for(String funcname: identifiers_in_functions.keySet()){
//            System.out.println("In Function "+funcname);
            Set<String> idset = identifiers_in_functions.get(funcname);
            Map<String, Arrayinfo> arrset = arrays_info_in_func.get(funcname);
            identifiers_info_in_func.put(funcname, new HashMap<String, ID_info>());
            Map<String, ID_info> offmap = identifiers_info_in_func.get(funcname);
            ID_info one_info = new ID_info();
            one_info.offset = 0;
            for(String id:idset){
                int ca_size;
                if(arrset.containsKey(id)){
                    Arrayinfo arrayinfo = arrset.get(id);
                    ca_size = arrayinfo.unit_size*arrayinfo.length;
                }else {
                    ca_size = 2;/********Integer的大小是2**/
                }
                one_info.size = ca_size;
//                System.out.println(""+id+"   "+one_offset);
                offmap.put(id, one_info);
                one_info = new ID_info(one_info);
                one_info.offset+=ca_size;
            }
//            System.out.println("total space "+one_offset);
            functions_spaces.put(funcname, one_info.offset);//函数内除参数外声明的变量所占的空间
        }
    }//计算函数占用空间与该函数的标识符的偏移量  要放入functions_spaces和identifiers_offsets
    private void count_front_spaces(){//对函数的输入参数和返回值求偏移量
        boolean in_function = false;
        String in_function_name = "";
        for(QuaterExpression oneqtr:quaterexpressions) {
            if (oneqtr.operation.sign == Sign.Qfunc) {
                in_function_name = oneqtr.arg1.word;
                in_function = true;
                ID_info offset_sum = new ID_info();
                offset_sum.offset = functions_spaces.get(in_function_name);//目前的总偏移量
                offset_sum.offset+=2;
                functions_return_info.put(in_function_name, offset_sum);//当前返回的位置
                offset_sum = null;
            } else if (oneqtr.operation.sign == Sign.Qendfunc) {
                ID_info offset_point = functions_return_info.get(oneqtr.arg1.word);
                offset_point.size = 2;
                in_function = false;
            } else if (oneqtr.operation.sign == Sign.Qtakearg) {
                Set<String> idset = identifiers_in_functions.get(in_function_name);//这个函数的标识符集合
                Map<String, ID_info> offsets = identifiers_info_in_func.get(in_function_name);//标识符的偏移量
                ID_info offset_sum = new ID_info(functions_return_info.get(in_function_name));//目前的总偏移量
                idset.add(oneqtr.arg1.word);
                ID_info temp = new ID_info(); temp.offset = offset_sum.offset; temp.size = 2;
                offsets.put(oneqtr.arg1.word, temp);
                offset_sum.offset+=2;
                functions_return_info.put(in_function_name, offset_sum);//当前返回的位置

            }
        }
    }//对函数的输入参数和返回值求偏移量
    private void println_offsets(){//debug用，输出偏移表
        for(String funcname: identifiers_in_functions.keySet()){
            System.out.println("In function: "+funcname);
            Map<String, ID_info> idoffsets = identifiers_info_in_func.get(funcname);
            for(String oneid:idoffsets.keySet()){
                System.out.println(""+oneid+" : "+idoffsets.get(oneid).offset+" "+idoffsets.get(oneid).size);
            }
            System.out.println("total space : "+functions_spaces.get(funcname)+"   return offset : "+functions_return_info.get(funcname).offset);
        }
    }//debug用，输出偏移表
    public void set_quaterexpressions(List<QuaterExpression> q){
        quaterexpressions = q;
    }
    public String asm(){
        StringBuffer code = new StringBuffer("");
        this.scan_identifier_list();
        this.count_offsets();
        this.count_front_spaces();
        //println_offsets();
        this._add_dseg(code);
        this._add_sseg(code, 8192);
        this._add_cseg(code);
        return new String(code);
    }
}
