package Inter;

import Lexer.*;

public class QuaterExpression {
	public Code_Token operation;
	public Code_Token arg1, arg2;
	public Code_Token result;
	public QuaterExpression() {
		// Do nothing
	}
	public QuaterExpression(Code_Token op, Code_Token arg1, Code_Token arg2, Code_Token result) {
		this.operation = (op == null) ? null : new Code_Token(op);
		this.arg1 = (arg1 == null) ? null : new Code_Token(arg1);
		this.arg2 = (arg2 == null) ? null : new Code_Token(arg2);
		this.result = (result == null) ? null : new Code_Token (result);
	}
	
	public Code_Token getLabel() {
		return arg1;
	}
	
	public Code_Token getOp() {
		return operation;
	}
	public Code_Token getArg1() {
		return arg1;
	}
	public Code_Token getArg2() {
		return arg2;
	}
	public Code_Token getResult() {
		return result;
	}
	
	@Override
	public String toString() {
		String str = new String();
		str = "(";
		if(operation == null)
			str += "_, ";
		else
			str += operation.toString() + ", ";
		if(arg1 == null)
			str += "_, ";
		else
			str += arg1.toString() + ", ";
		if(arg2 == null)
			str += "_, ";
		else
			str += arg2.toString() + ", ";
		if(result == null)
			str += "_)";
		else
			str += result.toString() + ")";
		//str += " " + operation.sign;
		return str;
	}
	
	@Override
	public boolean equals(Object obj) {
		//System.out.println("inter");
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		final QuaterExpression qt = (QuaterExpression)obj;
		if(qt.arg1 == null)
			return false;
		if(!qt.arg1.equals(this.arg1)) {
			//System.out.println("inter");
			return false;
		}
		return true;
	}
}
