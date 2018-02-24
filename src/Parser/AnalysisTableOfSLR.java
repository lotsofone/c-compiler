package Parser;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.List;
import Lexer.*;
import Inter.*;

class Pair {
	private Integer line;
	private Sign sign;
	
	public Pair() {
		// Do nothing
	}
	public Pair(int line, Sign sign) {
		this.line = new Integer(line);
		this.sign = sign;
	}
	
	public Integer first() {
		return line;
	}
	public Sign second() {
		return sign;
	}
	
	public void change(Sign sign) {
		this.sign = sign;
	}
	
	public void print() {
		System.out.println("(" + line + "," + sign + ")");
	}
}

@SuppressWarnings("serial")
class StatuteTable extends Vector<HashMap<Vector<Sign>, Pair>>{
	public StatuteTable() {
		// Do nothing
	}
	public StatuteTable(AutoMaton_LR_0 DFA) {
		for(ProductionCollection ProColl : DFA) {
			Vector<Production> PdcList = ProColl.getProductionList();
			HashMap<Vector<Sign>, Pair> map = new HashMap<Vector<Sign>, Pair>();
			for(Production Pdc : PdcList) {
				if(Pdc.second().indexOf(Sign.POINT) == Pdc.second().size()-1) {
					Production temp = new Production(Pdc);
					temp.second().remove(temp.second().size()-1);
					//temp.print();
					int line = DFA.Gram.getProColl().find(temp);	// Find the prodution's line in the production collection
					//System.out.println(line);
					map.put(temp.second(), new Pair(line, temp.first()));
				}
			}
			this.add(map);
		}
	}
	
	public void print(int index) {
		HashMap<Vector<Sign>, Pair> map = this.get(index);
		for(Map.Entry<Vector<Sign>, Pair> entry : map.entrySet()) {
			System.out.print(entry.getKey() + "  ");
			entry.getValue().print();
		}
	}
}

public class AnalysisTableOfSLR {
	public AutoMaton_LR_0 DFA;
	public StatuteTable Statute;
	public Entrance enter;
	private String ACTIONTable[][];
	private RunError Error;
	
	public AnalysisTableOfSLR() {
		DFA = new AutoMaton_LR_0();
		Statute = new StatuteTable(DFA);
		ACTIONTable = new String[Sign.SIGN_SIZE][Sign.SIGN_SIZE];
		setACTION();
		enter = new Entrance();
		Error = new RunError(DFA.getTable(), DFA.Gram);
	}
	
	public void outputInter() {
		System.out.println(enter.code);
	}
	
	public void analysis(List<Code_Token> TokenList) {
		Stack<Integer> stk = new Stack<Integer>();
		stk.push(0);
		int s, index = 0;
		List<Sign> signstr = new Vector<Sign>();
		List<Code_Token> lst = new Vector<Code_Token>();
		Code_Token token = TokenList.get(index);
		Code_Token preToken = token;
		enter.table.tokenOpt(token);
		int before = 0, count = 0;
		while(true) {
			s = stk.peek();
			String act = ACTION(s, token.sign);
			//System.out.println(stk + " " + signstr + " " + token.sign + "  " + act );
			if(act == null) {
				if(signstr.get(signstr.size()-1) != Sign.VOID &&
				needVOID(signstr.get(signstr.size()-1)) && count < 2) {
					index--;
					count++;
					//System.out.println(count);
					token.sign = Sign.VOID;
					lst.add(token);
					enter.table.tokenOpt(token);
					act = ACTION(s, token.sign);
				}
				else {
					//Error.run(stk);
					System.out.println("error line " + token.line + ":");
					System.exit(-1);
					break;
				}
			}
			else if(act.charAt(0) == 's') {
				count = 0;
				signstr.add(token.sign);
				lst.add(token);
				String str = act.substring(1, act.length());
				before = Integer.parseInt(str);
				stk.push(before);
				preToken = token;
				if(index < TokenList.size()-1)
					token = new Code_Token(TokenList.get(++index));
				else
					token = new Code_Token(TokenList.get(index));
				enter.table.tokenOpt(token);
			} else if(act.charAt(0) == 'r') {
				count = 0;
				//System.out.println(stk + " " + signstr + " " + token.sign + "  " + act );
				for(int i = signstr.size()-1; i < signstr.size(); i--) {
					List<Sign> vecs = signstr.subList(i, signstr.size());
					List<Code_Token> tokenlist = lst.subList(i, lst.size());
					if(Statute.get(s).containsKey(new Vector<Sign>(vecs))) {
						for(int j = signstr.size(); j > i; j--)
							stk.pop();
						List<Code_Token> tklst = new LinkedList<Code_Token>(tokenlist);
						Pair pair = Statute.get(s).get(vecs);		// vecs is satute production
						String str = act.substring(1, act.length());		// Get the satute number
						before = Integer.parseInt(str);
						stk.push(GOTO(stk.peek(), pair.second()));
						signstr = signstr.subList(0, i);
						signstr.add(pair.second());
						lst = lst.subList(0,  i);
						lst.add(new Code_Token(pair.second(), tokenlist.get(0).word));
						enter.run(before, tklst, pair.second());		// Enter the SDD
						break;
					}
				}
			} else if(act.equals("acc")) {
				System.out.println("correct");
				break;
			}
		}
	}
	
	public void print() {
		System.out.printf("    ");
		for(Sign sign : DFA.Gram.getVt()) {
			System.out.printf("|%s  ", sign.toString());
		}
		for(Sign sign : DFA.Gram.getVn())
			System.out.printf("|%s  ", sign.toString());
		System.out.println();
		
		for(int i = 0; i < DFA.size(); i++) {
			System.out.printf("%-4d", i);
			for(Sign sign : DFA.Gram.getVt()) {
				int length = sign.toString().length()+2;
				System.out.printf("|%s", ((ACTION(i, sign) == null)
				? "" : ACTION(i, sign)));
				int len;
				if(ACTION(i, sign) == null)
					len = 0;
				else
					len = ACTION(i, sign).length();
				for(int j = 0; j < length-len; j++)
					System.out.printf(" ");
			}
			for(Sign sign : DFA.Gram.getVn()) {
				int length = sign.toString().length()+2;
				System.out.printf("|%s", ((GOTO(i, sign) == -1) 
				? "" : GOTO(i, sign).toString()));
				int len;
				len = GOTO(i, sign).toString().length();
				if(GOTO(i, sign) == -1)
					len = 0;
				for(int j = 0; j < length-len; j++)
					System.out.printf(" ");
			}
			System.out.println();
		}
	}
	
	private void setACTION() {
		int i = 0;
		for(int j = 0; j < DFA.size(); j++) {
			for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
				DFA.get(j).entrySet()) {
				for(int k = 0; k < entry.getValue().size(); k++) {	// Traverse A -> ...
					Vector<Sign> Tmp = entry.getValue().get(k);		// Tmp = ������
					//System.out.println(Tmp);
					int pos = Tmp.indexOf(Sign.POINT);
					if(pos != Tmp.size()-1) {						// Find A -> ����a��
						if(GOTO(i, Tmp.get(pos+1)) != -1)
							ACTIONTable[i][Tmp.get(pos+1).ordinal()]
							= (ACTIONTable[i][Tmp.get(pos+1).ordinal()] == null 
							|| ACTIONTable[i][Tmp.get(pos+1).ordinal()].charAt(0) == 's')
							? "s" + GOTO(i, Tmp.get(pos+1)).toString()
							: ACTIONTable[i][Tmp.get(pos+1).ordinal()] + "s" + GOTO(i, Tmp.get(pos+1)).toString(); 
					} else {
						Vector<Sign> TmpCell = DFA.Gram.FOLLOW(entry.getKey());
																	// TmpCell = FOLLOW(A)
						Vector<Sign> signstr = new Vector<Sign>(Tmp);
						signstr.remove(Sign.POINT);
						//System.out.println(signstr);
						for(int index = 0; index < TmpCell.size(); index++) {
							//System.out.println(Statute.size());
							ACTIONTable[i][TmpCell.get(index).ordinal()] 
							= (ACTIONTable[i][TmpCell.get(index).ordinal()] == null) 
							? "r" + Statute.get(i).get(signstr).first().toString()
							: ACTIONTable[i][TmpCell.get(index).ordinal()] + "r" + Statute.get(i).get(signstr).first().toString();
							if(entry.getKey().equals(DFA.Gram.StartSign)
								&& TmpCell.get(index).equals(Sign.END))
								ACTIONTable[i][TmpCell.get(index).ordinal()] = "acc";
						}
					}
				}
			}
			i++;
		}
		//ACTIONTable[104][Sign.ELSE.ordinal()] = "s106";				// Set if else...
	}
	
	private String ACTION(int State, Sign a) {
		//System.out.println(State + " " + a);
		return ACTIONTable[State][a.ordinal()];
	}
	
	private Integer GOTO(int State, Sign a) {
		int i = DFA.GOTO(State, a);
		return new Integer(i);
	}
	
	private boolean needVOID(Sign sign) {
		Vector<Sign> signstr = DFA.Gram.FOLLOW(sign);
		//System.out.println(sign + ": " + signstr);
		int pos = signstr.indexOf(Sign.VOID);
		return pos != -1;
	}
}
