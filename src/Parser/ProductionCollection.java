package Parser;
import java.util.TreeMap;
import java.util.Map;
import java.util.Vector;

@SuppressWarnings("serial")
public class ProductionCollection extends TreeMap<Sign, Vector<Vector<Sign>>> {
	public ProductionCollection() {	
		// Do nothing
	}
	public ProductionCollection(ProductionCollection ProColl) {
		this.clear();
		this.putAll(ProColl);
	}
	
	public void put(Production pro) {		
		if(!this.containsKey(pro.first()))
			this.put(pro.first(), new Vector<Vector<Sign>>());
		this.get(pro.first()).add(pro.second());
	}
	public void put(Sign key, Vector<Sign> value) {
		if(!this.containsKey(key))
			this.put(key, new Vector<Vector<Sign>>());
		this.get(key).add(value);
	}
	
	public Vector<Production> getProductionList() {					// Turn ProductionCollection to ProductionList
		Vector<Production> PdcList = new Vector<Production>();
		for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
			this.entrySet()) {
			for(int i = 0; i < entry.getValue().size(); i++) {
				Production Pdc = new Production(entry.getKey(), entry.getValue().get(i));
				PdcList.add(Pdc);
			}
		}
		return PdcList;
	}
	
	public int find(Production Pdc) {								// Find the prodution's line in the production collection
		int count = 1;
		for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
			this.entrySet()) {
			for(int i = 0; i < entry.getValue().size(); i++) {
				if(entry.getKey().equals(Pdc.first()) && 
				entry.getValue().get(i).equals(Pdc.second()))
					return count;
				count++;
			}
		}
		return -1;
	}
	
	public void print() {
		for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
			this.entrySet()) {
			for(int i = 0; i < entry.getValue().size(); i++)
				System.out.println(entry.getKey() + "->" + entry.getValue().get(i));
		}
	}
	
	public void setProColl() {
		if(!this.isEmpty())
			return;
		this.put(new Production(Sign.Program, "->", Sign.ExtDefList));
		this.put(new Production(Sign.ExtDefList, "->", Sign.ExtDef, Sign.ExtDefList));
		this.put(new Production(Sign.ExtDefList, "->", Sign.VOID));
		this.put(new Production(Sign.ExtDef, "->", Sign.Specifier, Sign.ExtDecList, Sign.SEMI));
		this.put(new Production(Sign.ExtDef, "->", Sign.Specifier, Sign.SEMI));
		this.put(new Production(Sign.ExtDef, "->", Sign.Specifier, Sign.FunDec, Sign.CompSt));
		this.put(new Production(Sign.ExtDecList, "->", Sign.VarDec));
		this.put(new Production(Sign.ExtDecList, "->", Sign.ExtDecList, Sign.COMMA, Sign.VarDec));
		

		this.put(new Production(Sign.Specifier, "->", Sign.TYPE));
		this.put(new Production(Sign.Specifier, "->", Sign.StructSpecifier));
		this.put(new Production(Sign.StructSpecifier, "->", Sign.STRUCT, Sign.Tag, Sign.LC, Sign.DefList, Sign.RC));
		this.put(new Production(Sign.StructSpecifier, "->", Sign.STRUCT, Sign.Tag));
		this.put(new Production(Sign.OptTag, "->", Sign.ID));
		this.put(new Production(Sign.OptTag, "->", Sign.VOID));
		this.put(new Production(Sign.Tag, "->", Sign.ID));
		

		this.put(new Production(Sign.VarDec, "->", Sign.ID));
		this.put(new Production(Sign.VarDec, "->", Sign.VarDec, Sign.LB, Sign.INT, Sign.RB));
		this.put(new Production(Sign.FunDec, "->", Sign.ID, Sign.LP, Sign.VarList, Sign.RP));
		this.put(new Production(Sign.FunDec, "->", Sign.ID, Sign.LP, Sign.RP));
		this.put(new Production(Sign.VarList, "->", Sign.VarList, Sign.COMMA, Sign.ParamDec));
		this.put(new Production(Sign.VarList, "->", Sign.ParamDec));
		this.put(new Production(Sign.ParamDec, "->", Sign.Specifier, Sign.VarDec));
		

		this.put(new Production(Sign.CompSt, "->" , Sign.LC, Sign.DefList, Sign.StmtList, Sign.RC));
		this.put(new Production(Sign.StmtList, "->", Sign.VOID));
		this.put(new Production(Sign.StmtList, "->", Sign.StmtList, Sign.Stmt));
		this.put(new Production(Sign.Stmt, "->", Sign.Exp, Sign.SEMI));
		this.put(new Production(Sign.Stmt, "->", Sign.CompSt));
		this.put(new Production(Sign.Stmt, "->", Sign.RETURN, Sign.Exp, Sign.SEMI));
		this.put(new Production(Sign.Stmt, "->", Sign.IF, Sign.LP, Sign.Exp, Sign.RP, Sign.Label, Sign.Stmt, Sign.Label));
		this.put(new Production(Sign.Stmt, "->", Sign.IF, Sign.LP, Sign.Exp, Sign.RP, Sign.Label, Sign.Stmt, Sign.ELSE, 
								Sign.Label, Sign.Stmt, Sign.Label));
		this.put(new Production(Sign.Stmt, "->", Sign.Label, Sign.WHILE, Sign.LP, Sign.Exp, Sign.RP, Sign.Label, Sign.Stmt, Sign.Label));
		this.put(new Production(Sign.Label, "->", Sign.VOID));
		
		
		this.put(new Production(Sign.DefList, "->", Sign.VOID));
		this.put(new Production(Sign.DefList, "->", Sign.DefList, Sign.Def));
		this.put(new Production(Sign.Def, "->", Sign.Specifier, Sign.DecList, Sign.SEMI));
		this.put(new Production(Sign.DecList, "->", Sign.Dec));
		this.put(new Production(Sign.DecList, "->", Sign.DecList, Sign.COMMA, Sign.Dec));
		this.put(new Production(Sign.Dec, "->", Sign.VarDec));
		this.put(new Production(Sign.Dec, "->", Sign.VarDec, Sign.InitLabel, Sign.ASSIGNOP, Sign.Exp));
		

		this.put(new Production(Sign.Args, "->", Sign.Args, Sign.COMMA, Sign.Exp));
		this.put(new Production(Sign.Args, "->", Sign.Exp));
		this.put(new Production(Sign.Exp, "->", Sign.LeftVal, Sign.LeftLabel, Sign.ASSIGNOP, Sign.Exp));
		this.put(new Production(Sign.Exp, "->", Sign.OrRel));
		this.put(new Production(Sign.LeftVal, "->", Sign.ID));
		this.put(new Production(Sign.LeftVal, "->", Sign.LeftVal, Sign.LB, Sign.Add, Sign.RB));
		this.put(new Production(Sign.OrRel, "->", Sign.OrRel, Sign.OR, Sign.AndRel));
		this.put(new Production(Sign.OrRel, "->", Sign.AndRel));
		this.put(new Production(Sign.AndRel, "->", Sign.AndRel, Sign.AND, Sign.Rel));
		this.put(new Production(Sign.AndRel, "->", Sign.Rel));
		this.put(new Production(Sign.Rel, "->", Sign.Rel, Sign.RELOP, Sign.Add));
		this.put(new Production(Sign.Rel, "->", Sign.Add));
		this.put(new Production(Sign.Rel, "->", Sign.NOT, Sign.NotRel));
		this.put(new Production(Sign.NotRel, "->", Sign.Factor));
		this.put(new Production(Sign.Add, "->", Sign.Add, Sign.PLUS, Sign.Term));
		this.put(new Production(Sign.Add, "->", Sign.Add, Sign.MINUS, Sign.Term));
		this.put(new Production(Sign.Add, "->", Sign.MINUS, Sign.Term));
		this.put(new Production(Sign.Add, "->", Sign.Term));
		this.put(new Production(Sign.Term, "->", Sign.Term, Sign.STAR, Sign.Factor));
		this.put(new Production(Sign.Term, "->", Sign.Term, Sign.DIV, Sign.Factor));
		this.put(new Production(Sign.Term, "->", Sign.Factor));
		this.put(new Production(Sign.Factor, "->", Sign.LP, Sign.Exp, Sign.RP));
		this.put(new Production(Sign.Factor, "->", Sign.LeftVal));
		//this.put(new Production(Sign.Factor, "->", Sign.ID));
		this.put(new Production(Sign.Factor, "->", Sign.INT));
		this.put(new Production(Sign.Factor, "->", Sign.FLOAT));
		this.put(new Production(Sign.Factor, "->", Sign.ID, Sign.LP, Sign.RP));
		this.put(new Production(Sign.Factor, "->", Sign.ID, Sign.LP, Sign.Args, Sign.RP));
		this.put(new Production(Sign.LeftLabel, "->", Sign.VOID));
		this.put(new Production(Sign.InitLabel, "->", Sign.VOID));
	}
}
