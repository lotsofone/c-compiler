package Parser;

public enum Sign {
	INT, FLOAT, ID, SEMI, COMMA, ASSIGNOP, RELOP, PLUS, MINUS, STAR, DIV, 
	AND, OR, DOT, NOT, TYPE, LP, RP, LB, RB, LC, RC, STRUCT, RETURN, IF, ELSE, WHILE,
									// Tokens
	Program, ExtDefList, ExtDef, ExtDecList, 
									// High-level Definitions
	Specifier, StructSpecifier, OptTag, Tag,
									// Specifiers
	VarDec, FunDec, VarList, ParamDec,
									// Declarators
	CompSt, StmtList, Stmt, Label,
									// Statements
	DefList, Def, DecList, Dec,
									// Local Definitions
	Args, Exp, LeftVal, NotRel, OrRel, AndRel, Rel, Add, Term, Factor, LeftLabel, InitLabel,
									// Expressions
	VOID, END, POINT, RightLabel,
									// ��, #, ��
	tokenizer_state_zero, tokenizer_state_eof, tokenizer_state_identifier_or_reserved, tokenizer_state_char, 
	tokenizer_state_delimiter, tokenizer_state_number, tokenizer_state_operator, tokenizer_state_string, 
	tokenizer_state_error,
	
	Qif, Qdeclare, Qtemp_declare, Qlabel, Qgoto, Qfunc, Qendfunc, Qtakearg, Qputarg, Qcall, Qreturn,
	Qarrayin, Qarrayout, Qarray, Qarrayend, Qstruct, Qstructend;
	public static final int SIGN_SIZE = 255;
}
