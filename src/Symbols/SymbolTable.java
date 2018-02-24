package Symbols;
import Lexer.Code_Token;
import Parser.Sign;

import java.sql.ParameterMetaData;
import java.util.Vector;

public class SymbolTable{
	Vector<SymbolNode> symbol;
	Vector<FuncNode> funcTable;
	Vector<TypeNode> typeTable;
	Vector<ArrayNode> arrayTable;
	ParserNode ExtDefList, ExtDef, Specifier,  FunDec, CompSt, VarDec, 
			TYPE, StructSpecifier, OptTag, Tag, INT, ID, FLOAT, StmtList, Dec,
			Stmt, Exp, tempVar1, tempVar2, tempVar3, tempVar4, FunName,
			LeftVal, NotRel, OrRel, AndRel, Rel, Add, Term, Factor;
	ParserListNode ExtDecList;
	Vector<ParserNode> DecList, Args , IDlist;
	SymbolNode ParamDec;
	Vector<SymbolNode> DefList, VarList, Def;
	static int count = 0;
	static int IDcount = 0;
	String CurFun;
	
	public SymbolTable(){
		this.symbol = new Vector<SymbolNode>();
		this.funcTable = new Vector<FuncNode>();
		this.typeTable = new Vector<TypeNode>();
		this.arrayTable = new Vector<ArrayNode>();
		init();
	}

	private void init(){
			typeTable.add(new TypeNode(1,null));
			typeTable.add(new TypeNode(2, null));
			ExtDefList = new ParserNode(null, null, null);
			ExtDef  = new ParserNode(null, null, null);
			ExtDecList = new ParserListNode(null, null, null);
			FunDec  = new ParserNode(null, null, null);
			Dec = new ParserNode(null, null, null);
			Exp = new ParserNode(typeTable.get(0), null, null);
			VarDec = Dec;
			Vector<SymbolNode> templist = new Vector<SymbolNode>();
			SymbolNode tempSymbol = new SymbolNode("out", typeTable.get(0), 4, null);
			tempSymbol.curfun = "printi";
			symbol.add(tempSymbol);
			templist.add(tempSymbol);
			FuncNode tempFunc = new FuncNode(1, templist);
			symbol.add( new SymbolNode("printi", typeTable.get(0), 1, tempFunc));
			symbol.add(new SymbolNode("scani", typeTable.get(0), 1, null));
			symbol.add(new SymbolNode("printlln", typeTable.get(0), 1, null));
			IDlist = new Vector<ParserNode>();
	}
	public int getArraySize() {
		int size = 1;
		TypeNode tempNode = VarDec.type;
		while(tempNode != null) {
			size *= ((ArrayNode)tempNode.point).size;
			tempNode = ((ArrayNode)tempNode.point).ctp;
			//System.out.println("inter:" + size);
		}
		return size;
	}
	public int getArrayDimSize(String name, int dim) {
		int size = 1;
		TypeNode tempType = checkTable(name).type;
		for(int index = dim; index > 0; index--) {
			tempType = ((ArrayNode)tempType.point).ctp;
		}
		while(tempType.type == 3) {
			size *= ((ArrayNode)tempType.point).size;
			tempType = ((ArrayNode)tempType.point).ctp;
		}
		return size;
	}
	public int getArrayDim(String name) {
		int size = 0;
		if((ParamDec = checkTable(name)) == null) {
			return 0;
		}
		TypeNode tempType = ParamDec.type;
		while(tempType.type == 3) {
			++size;
			tempType = ((ArrayNode)tempType.point).ctp;
		}
		return size;
	}
	public void leftvalArray(){
		LeftVal.type = ((ArrayNode)LeftVal.type.point).ctp;
	}
	public void expEqual(){
		if(tempVar2.type != LeftVal.type) {
			System.out.println(" the type is not equal");
			System.exit(0);
		}
	}
	public String getFuncDec() {
		return CurFun;
	}
	public void addTempVar(Code_Token token){
		SymbolNode tempSymbol = new SymbolNode(token.word, typeTable.get(0), 3, null);
		symbol.add(tempSymbol);
	}
	public Vector<FunctionArg> functionArgsSize(String funcname){
		int size = 0;
		SymbolNode temp = checkTable(funcname);
		Vector<FunctionArg> argslist = new Vector<FunctionArg>();		
		if(temp.addr != null){
			FuncNode tempfun = (FuncNode)temp.addr;
			Vector<SymbolNode> list = tempfun.parmas;
			for(int i = 0; i < list.size(); i++){
				switch(list.get(i).type.type ){
					case 1:
						argslist.add(new FunctionArg(list.get(i).name, 1, size, 2));
						size += 2;
						break;
					case 2:
						argslist.add(new FunctionArg(list.get(i).name, 2, size, 2));	
						size += 2;
						break;
					case 3:
						argslist.add(new FunctionArg(list.get(i).name, 1, size, (int)list.get(i).addr));
						size += (int)list.get(i).addr;
						break;
					case 4:
						argslist.add(new FunctionArg(list.get(i).name, 1, size, list.get(i).type.size));	
						size += list.get(i).type.size;
						break;
				}
			}
		}
		argslist.add(new FunctionArg(null, temp.type.type, size, temp.type.size));		
		return argslist;
	}
	public int structSize(SymbolNode tempsymbol){
		return tempsymbol.type.size;
	}
	public void tokenOpt(Code_Token token){
		// System.out.println(token.word + token.line);
		if(token.sign == Sign.ID){
			ID = new ParserNode(null, token.word, null);
		}else if(token.sign == Sign.INT){
			INT = new ParserNode(typeTable.get(0), null, null);
			INT.value = Integer.parseInt(token.word);
		}else if(token.sign == Sign.FLOAT){
			FLOAT = new ParserNode(typeTable.get(1), null, null);
			FLOAT.value = Double.parseDouble(token.word);
		}else if(token.sign == Sign.TYPE){
			TYPE = new ParserNode(null, null, null);
			switch(token.word){
				case "int":
					TYPE.type = typeTable.get(0);
					break;
				case "float":
					TYPE.type = typeTable.get(1);
					break;
			}
		}else if(token.sign == Sign.LP){
			FunName = ID;
			IDlist.add(ID);
			count++;
		}else if(token.sign == Sign.RP){
			FunDec = IDlist.get(IDlist.size()-count);
			// System.out.println(FunDec.name);
		}else if (token.sign == Sign.RC){
			count = 0;
			IDlist.removeAllElements();
		}else if(token.sign == Sign.ASSIGNOP) {
			tempVar2 = LeftVal;
		}
	}
	public void insertList(){
		TypeNode type = Specifier.type;
		Vector<ParserNode> list = ExtDecList.value;
		SymbolNode tempSymbol = null;
		int size = 1;
		for(int i = 0; i < list.size(); i++){
			tempVar1 = list.get(i);
			if(checkIdRight(tempVar1.name, null) != null){
				repeatError(tempVar1.name);
				continue;
			}
			if(tempVar1.type == null){
				tempSymbol = insertSymbolNode(tempVar1.name, type, 3, tempVar1.value);				
			}else{
				tempSymbol =  insertSymbolNode(tempVar1.name, tempVar1.type, 3, tempVar1.value);
				TypeNode tempType = tempVar1.type;
				while(((ArrayNode)tempType.point).ctp != null){
					size *= ((ArrayNode)tempType.point).size;
					tempType = ((ArrayNode)tempType.point).ctp;
				}
				((ArrayNode)tempType.point).ctp = type;
				size *= ((ArrayNode)tempType.point).size;				
				tempSymbol.addr = size;
			}
		}
	}
	public void insertFun(){
		if(checkIdRight(FunDec.name, null) == null){
			insertSymbolNode(FunDec.name, Specifier.type, 1, FunDec.value);			
			count = 0;
			IDlist.removeAllElements();
			CurFun = FunDec.name;
		}else{
			for(int i = 0; i < symbol.size(); i++){
				System.out.println(symbol.get(i).name);
			}
			repeatError(FunDec.name);
		}
	}
	public void newSpecifier(){
		if(checkIdRight(Specifier.name, null) == null){
			insertSymbolNode(Specifier.name,Specifier.type, 2, null);			
		}else{
			System.out.println("struct is repeated define");
			System.exit(0);
		}
	}
	public void newExtdeclist(){
		ExtDecList.value = new Vector<ParserNode>();
	}
	public void extdeclistadd(){
		ExtDecList.value.add(VarDec);
	}
	public void specifierFromType(){
		Specifier = TYPE;
	}
	public void specifierFromStruct(){
		Specifier = StructSpecifier;
	}
	public void structSpecifierOpt(){
		Vector<SymbolNode> deflist = DefList;
		String name = Tag.name;
		Vector<StructNode> tempStructTable = new Vector<StructNode>();
		int size = 0;
		for(int i = 0; i < deflist.size(); i++){
			ParamDec = deflist.get(i);
			tempStructTable.add(new StructNode(ParamDec.name, ParamDec.type));
			switch(ParamDec.type.type){
				case 1:
					size +=2;
					break;
				case 2:
					size +=2;
					break;
				case 3:
					size += (int)ParamDec.addr;
					break;
				case 4:
					size += (int)ParamDec.type.size;
					break;
			}
		}
		TypeNode tempType = insertTypeNode(4, (Object)tempStructTable);
		tempType.size = size;
		StructSpecifier = new ParserNode(tempType, name, null);
		DefList = null;
	}
	public void checkStruct(){
		String name = Tag.name;
		if((ParamDec = checkIdRight(name, null)) == null){
			defineError(name);
		}else{
			if(ParamDec.cat == 2){
				StructSpecifier = new ParserNode(ParamDec.type, null, null);
			}else{
				defineError(name);
			}
		}
	}
	public void opttagNull(){
		OptTag = new ParserNode(null, null, null);
	}
	public void opttagID(){
		OptTag = ID;
	}
	public void tagID(){
		Tag = ID;
	}
	public void vardecID(){
		VarDec = ID;
	}
	public void vardecVar(){
		ArrayNode tempArrayNode = insertArrayNode(0, ((int)INT.value) -1, ((int)INT.value), null);
		TypeNode tempType = insertTypeNode(3, tempArrayNode);
		TypeNode type = VarDec.type;
		if(VarDec.type == null){
			VarDec.type = tempType;
		}else{
			while(((ArrayNode)type.point).ctp != null){
				type = ((ArrayNode)type.point).ctp;
			}
			((ArrayNode)type.point).ctp = tempType;
		}
	}
	public void funcNULL(){
		FunDec.name = ID.name;
		FunDec.value = null;
		insertFun();
	}
	public void funcARGS(){
		FuncNode tempfun = new FuncNode(VarList.size(), VarList);		
		FunDec.value = tempfun;
		for(int i = 0; i < VarList.size(); i++){
			VarList.get(i).curfun = FunDec.name;
		}
		insertFun();
	}
	public int funArgsNum() {
		return VarList.size();
	}
	public void varlistOptS(){
		for(int i =0; i < VarList.size(); i++){
			if(ParamDec.name == VarList.get(i).name){
				System.out.println("the arg name is repeat");
				System.exit(0);
			}
		}
		VarList.add(ParamDec);
	}
	public void varlistOpt(){
		VarList = new Vector<SymbolNode>();
		VarList.add(ParamDec);
	}
	public void insertSymbol(){
		int size = 1;
		if(VarDec.type == null){
			ParamDec = insertSymbolNode(VarDec.name, Specifier.type, 4, null);				
		}else{
			ParamDec =  insertSymbolNode(VarDec.name, tempVar1.type, 4, null);
			TypeNode tempType = VarDec.type;
			while(((ArrayNode)tempType.point).ctp != null){
				size *= ((ArrayNode)tempType.point).size;
				tempType = ((ArrayNode)tempType.point).ctp;
			}
			((ArrayNode)tempType.point).ctp = Specifier.type;
			size *= ((ArrayNode)tempType.point).size;				
			ParamDec.addr = size;
		}
	}
	public void defListNULL(){
		DefList = new Vector<SymbolNode>();
	}
	public void defListARGS(){
		for(int i = 0; i < Def.size(); i++){
			Def.get(i).curfun = FunDec.name;
			DefList.add(Def.get(i));
			Def.get(i).curfun = FunDec.name;
			// System.out.println(FunDec.name);
		}
	}
	public void defOpt(){
		Vector<ParserNode> list = DecList;
		TypeNode type = Specifier.type;
		Def = new Vector<SymbolNode>();
		for(int i = 0; i < list.size(); i++){
			tempVar1 = list.get(i);
			int size = 1;			
			if(checkIdRight(tempVar1.name, CurFun) != null){
				repeatError(tempVar1.name);
				continue;
			}
			if(tempVar1.type == null || tempVar1.type.type == 1 || tempVar1.type.type ==2){
				Def.add(insertSymbolNode(tempVar1.name, type, 3, tempVar1.value));				
			}else{
				Def.add(insertSymbolNode(tempVar1.name, tempVar1.type, 3, tempVar1.value));
				TypeNode tempType = tempVar1.type;
				while(((ArrayNode)tempType.point).ctp != null){
					size *= ((ArrayNode)tempType.point).size;
					tempType = ((ArrayNode)tempType.point).ctp;
				}
				size *= ((ArrayNode)tempType.point).size;
				((ArrayNode)tempType.point).ctp = type;
			}
		}
	}
	public void decListOne(){
		DecList = new Vector<ParserNode>();
		DecList.add(Dec);
	}
	public void decListTwo(){
		DecList.add(Dec);
	}
	public void decOne(){
		Dec = VarDec;
	}
	public void decTwo(){
		Dec = VarDec;
		if(Dec.type == null){
			Dec.type = Exp.type;
		}
	}
	public void expOr(){
		Exp = OrRel;
	}
	public void leftVal(){
		if((ParamDec = checkIdRight(ID.name, CurFun)) == null) {
			System.out.println(ID.name + " has not been defined from function leftval");
			System.exit(0);
		}else {
			LeftVal = ID;
			LeftVal.type = ParamDec.type;
		}
	}
	public void notRel(){
		NotRel = Factor;
	}
	public void OrRelAnd(){
		OrRel = AndRel;
	}
	public void AndRelRel(){
		AndRel = Rel;
	}
	public void RelAdd(){
		Rel = Add;		
	}
	public void RelNot(){
		Rel = NotRel;
	}
	public void addTerm(){
		Add = Term;		
	}
	public void termFactor(){
		Term = Factor;	
	}
	public void factorEXP(){
		Factor = Exp;
	}

	public void factorLeftval(){
		Factor = LeftVal;
	}
	public void factorINT(){
		Factor = INT;
		
	}
	public void factorFLOAT(){
		Factor = FLOAT;
	}
	public void factorCallNull(){
		if((ParamDec = checkIdRight(FunName.name, null)) == null){
			undefineError(FunName.name);
		}else{
			if(ParamDec.cat == 1 && ParamDec.addr == null){
				Factor = new ParserNode(ParamDec.type, ParamDec.name, ParamDec.addr);
			}else{
				System.out.println("string "+ ParamDec.name + " is not a function name!");
				System.exit(0);
			}
		}
		Factor = new ParserNode(ParamDec.type, null, null);
	}
	public void factorCallArgs(){
		if((ParamDec = checkTable(FunName.name)) == null){
			System.out.println(FunName.name + " has not been defined");
			System.exit(0);
		}else{
			if(ParamDec.cat == 1){
				Factor = new ParserNode(ParamDec.type, ParamDec.name, ParamDec.addr);
				Vector<SymbolNode> list = ((FuncNode)ParamDec.addr).parmas;
				for(int i = 0; i < Args.size(); i++){
					if(Args.get(i).type == list.get(i).type){
						continue;
					}else{
						System.out.println("the arg type is not match");
						System.exit(0);
					}
				}
			}else{
				System.out.println("string "+ ParamDec.name + " is not a function name!");
				System.exit(0);
			}
		}
		Factor = new ParserNode(ParamDec.type, null, null);
	}
	public void factorStruct(){
		if((ParamDec = checkTable(FunName.name)) == null){
			System.out.println("struct " + FunName.name + " is not found");
			System.exit(0);
		}else{
			if(ParamDec.type.type == 4){
				Vector<StructNode> list = ((Vector<StructNode>)ParamDec.type.point);
				for (int i = 0 ; i < list.size(); i++){
					if(list.get(i).name == ID.name){
						Factor = new ParserNode(list.get(i).typeNode, list.get(i).name, null);
						return ;
					}
				}
				System.out.println("struct " + FunName.name + " have not the factor" + ID.name);
				System.exit(0);
			}else{
				System.out.println("struct " + FunName.name + " is not found");
				System.exit(0);
			}
		}
		Factor = new ParserNode(ParamDec.type, null, null);
	}
	public void argsone(){
		Args = new Vector<ParserNode>();
		Args.add(Exp);
	}
	public void argstwo(){
		Args.add(Exp);
	}
	public SymbolNode insertSymbolNode(String name, TypeNode type, int cat, Object obj){
		SymbolNode tempSymbol = new SymbolNode(name, type, cat, obj);
		symbol.add(tempSymbol);
		return tempSymbol;
	}
	public ArrayNode insertArrayNode(int low, int top, int size, TypeNode type){
		ArrayNode tempArrayNode = new ArrayNode(low, top, size,  type);
		arrayTable.add(tempArrayNode);
		return tempArrayNode;
	}
	public TypeNode insertTypeNode(int type, Object obj){
		TypeNode tempNode = new TypeNode(type, obj);
		typeTable.add(tempNode);
		return tempNode;
	}
	public FuncNode insertFuncNode(Vector<SymbolNode> value){
		FuncNode tempFuncNode  = new FuncNode(value.size(), value);
		return tempFuncNode;
	}
	public SymbolNode checkIdRight(String name, String curFun){
		SymbolNode tempSymbol = null;
		if(curFun == null){
			for(int i = 0; i < symbol.size(); i++){
				tempSymbol = symbol.get(i);
				if(tempSymbol.name.equals(name) && tempSymbol.curfun == null){
					return tempSymbol;
				}
			}
		}else{
			for(int i = 0; i < symbol.size(); i++){
				tempSymbol = symbol.get(i);
				// print();
				if(tempSymbol.name.equals(name) && tempSymbol.curfun.equals(curFun)){
					return tempSymbol;
				}
			}
		}
		return null;
	}

	public SymbolNode checkTable (String name){
		SymbolNode tempSymbol = null;
		for(int i = 0; i < symbol.size(); i++){
			tempSymbol = symbol.get(i);
			if(tempSymbol.name.equals(name)){
				return tempSymbol;
			}
		}
		return null;
	}
	private void repeatError(String string){
		System.out.println(string  + " repeat");
		System.exit(0);
	}
	private void defineError(String name){
		System.out.println("struct type " + name + " is not found");
		System.exit(0);
	}
	public void print() {
		for(int i = 0; i < symbol.size(); i++) {
			System.out.print(symbol.get(i).name);
			switch(symbol.get(i).type.type) {
				case 1:
					System.out.print(" int ");
					break;
				case 2:
					System.out.print(" float ");
					break;
				case 3:
					System.out.print(" array ");
					break;
				case 4:
					System.out.print(" struct ");
					break;
			}
			System.out.println(symbol.get(i).curfun);
		}
	}
	public String ID_alloc(){
		return "comp" + IDcount++;
	}
	private void undefineError(String name){
		System.out.println(name + "undefine");
	}
}
class ParserNode{
	public ParserNode(TypeNode type, String name, Object value){
		this.name = name;
		this.type = type;
		this.value = value;
	}
	String name;
	TypeNode type;
	Object value;
}
class ParserListNode{
	String name;
	TypeNode type;
	Vector<ParserNode> value;
	public ParserListNode(String name, TypeNode type, Vector<ParserNode> value){
		this.name = name;
		this.type = type;
		this.value = value;
	}
}
class SymbolNode{
	String name;
	TypeNode type;
	int cat;//1.func  2.kind 3.var 4.arg
	Object addr;
	String curfun;
	public SymbolNode(String name, TypeNode type, int cat,  Object obj){
		this.name = name;
		this.type = type;
		this.cat = cat;
		this.addr = obj;
		this.curfun = null;
	}
}
class FuncNode{
	int args;
	Vector<SymbolNode> parmas;
	public FuncNode(int args, Vector<SymbolNode> value){
		this.args = args;
		this.parmas = value;
	}
}
class TypeNode{
	int type;//1.int  2.float 3.array  4.struct
	Object point;
	int size;
	public TypeNode(int type, Object obj){
		this.type = type;
		if(type == 1 || size == 2){
			this.size = 2;
		}
		point = obj;
	}
}
class ArrayNode{
	int low;
	int top;
	int size;
	TypeNode ctp;
	public ArrayNode(int low, int top, int size, TypeNode type){
		this.low = low;
		this.top = top;
		this.size = size;
		this.ctp = type;
	}
}
class StructNode{
	String name;
	TypeNode typeNode;
	public StructNode(String name, TypeNode type){
		this.name = name;
		this.typeNode = type;
	}
}