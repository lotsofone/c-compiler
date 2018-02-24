package Parser;

import Lexer.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Stack;

public class RunError {
	int[][] table;
	Grammar grammar;
	Stack<Integer> stack;
	
	public RunError() {
		// Do nothing
	}
	public RunError(int[][] table, Grammar Gram) {
		this.table = table;
		grammar = Gram;
	}
	
	public void run(Stack<Integer> stk) {
		Sign result = null;
		int top = 0;
		boolean flag = true;
		while(!stk.empty() && flag) {
			top = stk.pop();
			for(Sign sign : grammar.getVn()) {
				if(table[top][sign.ordinal()] != -1) {
					result = sign;
					flag = false;
					break;
				}
			
			}
		}
		System.out.println(top + " " + result + " " + grammar.FOLLOW(result));
	}
	
	private String errorInfor(Sign sign) {
		String error = new String();
		switch(sign) {
		case SEMI:
			error = ";";
			break;
		case LC:
			error = "{";
			break;
		case RC:
			error = "}";
			break;
		case LB:
			error = "[";
			break;
		case RB:
			error = "]";
			break;
		case LP:
			error = "(";
			break;
		case RP:
			error = ")";
			break;
		default:
			error = "";
			break;
		}
		return error;
	}
}
