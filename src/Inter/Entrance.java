package Inter;

import java.util.Stack;
import java.util.List;
import java.util.LinkedList;
import Parser.*;
import Lexer.*;
import Symbols.SymbolTable;

public class Entrance {
	public static Stack<Code_Token> stack;
	private static Integer temp = 0, label = 0;
	private Integer len = 0;
	public String code;
	public static SymbolTable table;
	public List<QuaterExpression> list;
	private List<Code_Token> tklst;
	private Code_Token tk;
	private Code_Token op, arg1, arg2, result;
	private QuaterExpression rightlabel, leftlabel;
	private int leftdim = 0, dim = 0;
	private String arrayname;
	
	private Code_Token newtemp() {
		++temp;
		return new Code_Token(Sign.ID, "t" + temp.toString());
	}
	
	private void returntemp() {
		--temp;
	}
	
	private Code_Token newlabel() {
		++label;
		return new Code_Token(Sign.Qlabel, "L" + label.toString());
	}
	
	public Entrance() {
		stack = new Stack<Code_Token>();
		code = new String();
		table = new SymbolTable();
		list = new LinkedList<QuaterExpression>();
		rightlabel = new QuaterExpression(new Code_Token(Sign.Label, "Right"),
				     new Code_Token(Sign.RightLabel, "label"), null, null);
		leftlabel = new QuaterExpression(new Code_Token(Sign.Label, "Left"),
			     new Code_Token(Sign.LeftLabel, "label"), null, null);
		arrayname = new String();
	}
	
	public void print() {
		for(QuaterExpression Qexpr : list) {
			System.out.println(Qexpr);
		}
	}
	
	public List<QuaterExpression> getQList() {
		return list;
	}
	
	public void run(int num, List<Code_Token> lst, Sign sign) {
		tklst = lst;
		switch(num) {
		case 68:
			runInit();
			break;
		case 67:
			runLeftLabel();
			break;
		case 66:
			runFunc();
			table.factorCallArgs();
			break;
		case 65:
			runFunc();
			table.factorCallNull();
			break;
		case 64:
			enter();
			table.factorFLOAT();
			break;	
		case 63:
			enter();
			table.factorINT();
			break;
		case 62:
			table.factorLeftval();
			break;
		case 61:
			table.factorEXP();
			break;
		case 60:
			table.termFactor();
			break;
		case 59:
		case 58:
		case 55:
		case 54:
		case 51:
		case 49:
		case 47:
			runBinocular();
			break;
		case 57:
			table.addTerm();
			break;
		case 56:
			table.addTerm();
			runUnary();
			break;
		case 53:
			table.RelNot();
			runNot();
			break;
		case 52:
			table.RelAdd();
			break;
		case 50:
			table.AndRelRel();
			break;
		case 48:
			table.OrRelAnd();
			break;
		case 46:
			table.notRel();
			break;
		case 45:
			//table.leftvalArray();
			runArray();
			break;
		case 44:
			enter();
			getArrayDim();
			table.leftVal();
			break;
		case 43:
			table.expOr();
			break;
		case 42:
			//table.expEqual();
			runAssignment();
			break;
		case 41:
			table.argsone();
			runArgs();
			break;
		case 40:
			table.argstwo();
			runArgs();
			break;
		case 39:
			runAssignment();
			table.decTwo();
			break;
		case 38:
			runDeclare();
			table.decOne();
			break;
		case 37:
			table.decListTwo();
			break;
		case 36:
			table.decListOne();
			break;
		case 35:
			table.defOpt();
			break;
		case 34:
			table.defListARGS();
			break;
		case 33:
			enter();
			table.defListNULL();
			break;
		case 32:
			runLabel();
			break;
		case 31:
			runWhile();
			break;
		case 30:
			runIfElse();
			break;
		case 29:
			runIf();
			break;
		case 28:
			runReturn();
			removeLeftLabel();
			break;
		case 26:
			removeLeftLabel();
			break;
		case 22:
			runTakeArg();
			table.insertSymbol();
			break;
		case 21:
			table.varlistOpt();
			break;
		case 20:
			table.varlistOptS();
			break;
		case 19:
			table.funcNULL();
			declareFunc(0);
			break;
		case 18:
			table.funcARGS();
			declareFunc(table.funArgsNum());
			break;
		case 17:
			table.vardecVar();
			declareArray(table.getArraySize());
			break;
		case 16:
			enter();
			table.vardecID();
			break;
		case 15:
			enter();
			table.tagID();
			break;
		case 12:
			table.checkStruct();
			break;
		case 11:
			declareStruct();
			table.structSpecifierOpt();
			break;
		case 10:
			table.specifierFromStruct();
			break;
		case 9 :
			table.specifierFromType();
			break;
		case 8 :
			runDeclare();
			table.extdeclistadd();
			break;
		case 7 :
			runDeclare();
			table.newExtdeclist();
			table.extdeclistadd();
			break;
		case 6 :
			runEndFunc();
			break;
		case 5:
			table.newSpecifier();
			break;
		case 4 :
			table.insertList();
			break;
		}
	}
	
	private void runLeftLabel() {
		int pos = list.lastIndexOf(rightlabel);
		if(pos != -1) {
			list.remove(pos);
			list.add(pos, leftlabel);
		}
	}
	
	private void enter() {											// Reduce production like Factor -> INT|FLOAT|ID
		tk = new Code_Token(tklst.get(0));
		stack.push(tk);
	}

	private void runFunc() {										// Call a function
		op = new Code_Token(Sign.Qcall, "call");
		arg1 = tklst.get(0);
		result = newtemp();
		Code_Token temp = new Code_Token(Sign.Qtemp_declare, "tempDecl");
		list.add(new QuaterExpression(temp, result, null, null));
		list.add(new QuaterExpression(op, arg1, null, result));
		stack.push(result);
	}
	
	private void runArray() {
		//System.out.println(dim);
		if(dim == 1) {
			int pos = list.lastIndexOf(rightlabel);
			if(pos != -1)
				list.remove(pos);
			if(leftdim != 1) {
				result = newtemp();
				arg2 = stack.pop();
				arg1 = stack.pop();

				op = new Code_Token(Sign.PLUS, "+");

				Code_Token temp = new Code_Token(Sign.Qtemp_declare, "tempDecl");
				//System.out.println("inter");
				list.add(new QuaterExpression(temp, result, null, null));
				list.add(new QuaterExpression(op, arg1, arg2, result));
				stack.push(result);

			}
			op = new Code_Token(Sign.Qarrayout, "arrayout");
			arg1 = new Code_Token(Sign.ID, arrayname);
			arg2 = stack.pop();
			result = newtemp();
			Code_Token temp = new Code_Token(Sign.Qtemp_declare, "tempDecl");
			list.add(rightlabel);
			list.add(new QuaterExpression(temp, result, null, null));
			list.add(new QuaterExpression(op, arg1, arg2, result));
			//System.out.println(new QuaterExpression(op, arg1, arg2, result));
			stack.push(result);
			leftdim = 1;
		} else if(dim > 1){
			//System.out.println(dim);
			Code_Token temp = new Code_Token(Sign.Qtemp_declare, "tempDecl");
			op = new Code_Token(Sign.STAR, "*");
			result = newtemp();
			list.add(new QuaterExpression(temp, result, null, null));
			arg2 = stack.pop();
			Integer inter = table.getArrayDimSize(arrayname, leftdim);
			arg1 = new Code_Token(Sign.INT, inter.toString());
			list.add(new QuaterExpression(op, arg1, arg2, result));
			stack.push(result);
			leftdim++;
			--dim;
		}
	}
	
	private int getArrayDim() {
		if(table.getArrayDim(tklst.get(0).toString()) > 0) {
			arrayname = tklst.get(0).toString();
			dim = table.getArrayDim(tklst.get(0).toString());
			//System.out.println(table.getArrayDim(tklst.get(0).toString()));
			leftdim = 1;
			stack.pop();
		}
		return table.getArrayDim(tklst.get(0).toString());
	}
	
	private void runBinocular() {									// Binocular operator
		removeLeftLabel();
		op = tklst.get(1);
		arg2 = stack.pop();
		arg1 = stack.pop();
		result = newtemp();
		Code_Token temp = new Code_Token(Sign.Qtemp_declare, "tempDecl");
		list.add(new QuaterExpression(temp, result, null, null));
		list.add(new QuaterExpression(op, arg1, arg2, result));		// (op , arg1, arg2, result);
		stack.push(result);
		table.addTempVar(result);
	}
	
	private void runUnary() {										// Reduce production like Add -> -Term | Rel -> NOT NotRel
		op = new Code_Token(tklst.get(0));							// op = minus
		arg2 = stack.pop();
		arg1 = new Code_Token(Sign.INT, "0");
		result = newtemp();
		Code_Token temp = new Code_Token(Sign.Qtemp_declare, "tempDecl");
		list.add(new QuaterExpression(temp, result, null, null));
		list.add(new QuaterExpression(op, arg1, arg2, result));
		stack.push(result);
		table.addTempVar(result);
	}
	
	private void runNot() {											// Unary operator, like !
		op = new Code_Token(tklst.get(0));
		arg1 = stack.pop();
		result = newtemp();
		Code_Token temp = new Code_Token(Sign.Qtemp_declare, "tempDecl");
		list.add(new QuaterExpression(temp, result, null, null));
		list.add(new QuaterExpression(op, arg1, null, result));
		stack.push(result);
		table.addTempVar(result);
	}
	
	private void runAssignment() {									// Reduce Exp -> Rel = Exp
		int pos = list.lastIndexOf(leftlabel);
		List<QuaterExpression> Tmp = new LinkedList<QuaterExpression>();
		op = new Code_Token(Sign.ASSIGNOP, tklst.get(1).toString());
		arg1 = stack.pop();
		result = stack.pop();
		//System.out.println(stack);
		if(pos != -1) {
			list.remove(pos);
			for(int index = pos; true; ) {
				QuaterExpression temp = list.remove(index);
				if(temp.getOp().sign.equals(Sign.Qarrayout)) {
					Code_Token inter = new Code_Token(temp.getResult());
					temp.getOp().sign = Sign.Qarrayin;
					temp.getOp().word = "arrayin";
					temp.getResult().sign = temp.getArg1().sign;
					temp.getResult().word = temp.getArg1().toString();
					temp.getArg1().sign = inter.sign;
					temp.getArg1().word = inter.toString();
					Tmp.add(temp);
					break;
				}
				Tmp.add(temp);
			}
			Tmp.add(Tmp.size()-1, new QuaterExpression(op, arg1, null, result));
			list.addAll(Tmp);
		}
		else {
			Code_Token inter = list.get(list.size()-1).getResult();
			if(arg1 != null && inter != null && inter.equals(arg1)) {
				QuaterExpression TmpCell = list.remove(list.size()-1);
				list.remove(list.size()-1);
				list.add(new QuaterExpression(TmpCell.getOp(), TmpCell.getArg1(),
							 TmpCell.getArg2(), result));
			}
			else
				list.add(new QuaterExpression(op, arg1, null, result));
		}
		stack.push(result);
	}
	
	
	private void runArgs() {										// save function's parameters
		op = new Code_Token(Sign.Qputarg, "putarg");
		arg1 = stack.pop();
		list.add(new QuaterExpression(op, arg1, null, null));
	}
	
	private void runLabel() {										// Place a label in here, in order to if | if else | while
		Code_Token Label = newlabel();
		stack.push(Label);
		op = new Code_Token(Sign.Qlabel, "label");
		list.add(new QuaterExpression(op, Label, null, null));
	}
	
	private void runWhile() {										// Run the while stmt
		op = new Code_Token(Sign.Qif, "if");
		arg2 = stack.pop();
		//System.out.println(list);
		Code_Token temp = stack.pop();
		Code_Token Goto = new Code_Token(Sign.Qgoto, "goto");
		Code_Token begin;
		while(!temp.sign.equals(Sign.Qlabel)) {
			temp = stack.pop();
		}
		arg1 = temp;
		result = stack.pop();
		do {
			begin = stack.pop();
		}while(!begin.sign.equals(Sign.Qlabel));
		int pos = list.indexOf(new QuaterExpression(null, arg1, null, null));
		list.add(pos, new QuaterExpression(op, result, arg1, arg2));
		pos = list.indexOf(new QuaterExpression(null, arg2, null, null));
		list.add(pos, new QuaterExpression(Goto, begin, null, null));
	}
	
	private void runIfElse() {										// Run the if stmt else stmt
		op = new Code_Token(Sign.Qif, "if");
		Code_Token Goto = new Code_Token(Sign.Qgoto, "goto");
		Code_Token end = stack.pop();
		Code_Token temp;
		do {
			temp = stack.pop();
		}while(!temp.sign.equals(Sign.Qlabel));
		arg2 = temp;
		do {
			temp = stack.pop();
		}while(!temp.sign.equals(Sign.Qlabel));
		arg1 = temp;
		result = stack.pop();
		int pos = list.indexOf(new QuaterExpression(null, arg1, null, null));
		list.add(pos, new QuaterExpression(op, result, arg1, arg2));
		pos = list.indexOf(new QuaterExpression(null, arg2, null, null));
		list.add(pos, new QuaterExpression(Goto, end, null, null));
	}
	
	
	private void runIf() {											// Run the if stmt
		op = new Code_Token(Sign.Qif, "if");
		arg2 = stack.pop();											// arg2 = label2
		Code_Token temp;
		do {
			temp = stack.pop();
		}while(!temp.sign.equals(Sign.Qlabel));
		arg1 = temp;												// arg1 = label1
		result = stack.pop();										// temp = bool
		int pos = list.indexOf(new QuaterExpression(null, arg1, null, null));
		//System.out.println(arg1.sign + "" + arg1);
		list.add(pos, new QuaterExpression(op, result, arg1, arg2));		// (if, bool, arg1, arg2)
	}
	
	private void runReturn() {
		op = new Code_Token(Sign.Qreturn, "return");
		arg1 = stack.pop();
		list.add(new QuaterExpression(op, arg1, null, null));
	}
	
	private void removeLeftLabel() {
		int pos = list.lastIndexOf(rightlabel);
		if(pos != -1)
			list.remove(pos);
	}
	
	private void runTakeArg() {										// Use in declare function
		op = new Code_Token(Sign.Qtakearg, "takearg");
		arg1 = stack.pop();
		arg2 = new Code_Token(Sign.INT, "int");
		list.add(new QuaterExpression(op, arg1, arg2, null));
	}
	
	private void runEndFunc() {
		op = new Code_Token(Sign.Qendfunc, "endfunc");
		arg1 = new Code_Token(Sign.ID, table.getFuncDec());
		list.add(new QuaterExpression(op, arg1, null, null));
	}
	
	private void declareFunc(int count) {
		op = new Code_Token(Sign.Qfunc, "func");
		arg1 = new Code_Token(tklst.get(0));
		arg2 = new Code_Token(Sign.INT, "int");
		list.add(list.size() - count, new QuaterExpression(op, arg1, arg2, null));
	}
	
	
	private void declareArray(int length) {
		len = length;
	}
	
	private void runInit() {
		op = new Code_Token(Sign.Qdeclare, "declare");
		//System.out.println(stack);
		arg1 = stack.pop();
		arg2 = new Code_Token(Sign.TYPE, "int");
		list.add(new QuaterExpression(op, arg1, arg2, null));
		stack.push(arg1);
	}
	
	public void declareStruct() {
		op = new Code_Token(Sign.Qstruct, "struct");
		arg1 = tklst.get(1);
		Code_Token find = new Code_Token(Sign.Qdeclare, "declare");
		int index;
		for(index = list.size()-1; index >= 0; index--) {
			if(!list.get(index).getOp().equals(find))
				break;
		}
		list.add(index+1, new QuaterExpression(op, arg1, null, null));
		op = new Code_Token(Sign.Qstructend, "structend");
		list.add(new QuaterExpression(op, arg1, null, null));
	}
	
	private void runDeclare() {
		if(len == 0) {
			//System.out.println(stack.peek());
			op = new Code_Token(Sign.Qdeclare, "declare");
			arg1 = stack.pop();
			arg2 = new Code_Token(Sign.TYPE, "int");
			list.add(new QuaterExpression(op, arg1, arg2, null));
		} else {
			op = new Code_Token(Sign.Qarray, "array");
			arg1 = stack.pop();
			arg2 = new Code_Token(Sign.INT, len.toString());
			list.add(new QuaterExpression(op, arg1, arg2, null));
			op = new Code_Token(Sign.Qdeclare, "declare");
			arg2 = new Code_Token(Sign.TYPE, "int");
			list.add(new QuaterExpression(op, null, arg2, null));
			op = new Code_Token(Sign.Qarrayend, "arrayend");
			list.add(new QuaterExpression(op, null, null, null));
		}
		len = 0;
	}
}
