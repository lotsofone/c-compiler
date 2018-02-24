package Parser;
import java.util.Vector;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

class AllSign {
	private Vector<Sign> signstr;
	static int UsedNum = 0;
	public AllSign() {
		// Do nothing
	}
	
	public Sign getElement() {
		return signstr.get(UsedNum++);
	}
}

public class Grammar {
	private ProductionCollection ProColl;
	private Vector<Sign> VOID_ELM;
	private Vector<Sign> Vt;   	 									// terminal
	private Vector<Sign> Vn;										// nonterminal
	private HashMap<Sign, Vector<Sign>> first;						// the set of first about all signs
	private HashMap<Sign, Vector<Sign>> follow;						// the set of follow about all signs
	public Sign StartSign;
	
	public Grammar() {
		VOID_ELM = new Vector<Sign>();
		VOID_ELM.add(Sign.VOID);
		first = new HashMap<Sign, Vector<Sign>>();
		follow = new HashMap<Sign, Vector<Sign>>();
		StartSign = Sign.Program;
		
		ProColl = new ProductionCollection();
		ProColl.setProColl();             							// Initialize grammar productions
		
		Vt = new Vector<Sign>();
		Vn = new Vector<Sign>();
		for(Sign sign : EnumSet.range(Sign.INT, Sign.WHILE))
			Vt.add(sign);
		Vt.add(Sign.VOID);
		Vt.add(Sign.END);
		for(Sign sign : EnumSet.range(Sign.Program, Sign.InitLabel))
			Vn.add(sign);											// Initialize Vt and Vn
		
		//System.out.println(setFIRST(Sign.ExtDefList));
		for(Sign sign : Vt)
			setFIRST(sign);
		for(Sign sign : Vn)
			setFIRST(sign);											// Initialize FIRST(X)
		
		for(Sign sign : Vt)
			setFOLLOW(sign);
		for(Sign sign : Vn)
			setFOLLOW(sign);										// Initialize FOLLOW(X)
	}
	
	public HashMap<Sign, Vector<Sign>> getFOLLOW() {
		return follow;
	}
	
	public ProductionCollection getProColl() {
		return ProColl;
	}
	
	public Vector<Sign> getVt() {
		return Vt;
	}
	
	public Vector<Sign> getVn() {
		return Vn;
	}
	
	public boolean isVt(Sign X) {									// is terminal, return true
		return Vt.indexOf(X) != -1;
	}
	
	public boolean isVn(Sign X) {									// is nonterminal, return true
		return Vn.indexOf(X) != -1;
	}
	
	public Vector<Sign> FIRST(Vector<Sign> signstr) {
		boolean[] added = new boolean[Sign.SIGN_SIZE];
		boolean flag = true;
		Vector<Sign> vecs = new Vector<Sign>();
		Arrays.fill(added, true);
		int index = 0;
		for(Sign sign : signstr) {
			flag = true;
			Vector<Sign> Tmp = setFIRST(sign);
			for(Sign temp : Tmp) {
				if(temp.equals(Sign.VOID))
					flag = false;
				if(added[temp.ordinal()]) {
					vecs.add(temp);
					added[temp.ordinal()] = false;
				}
			}
			if(flag)
				break;
			index++;
		}
		if(index == signstr.size())
			vecs.add(Sign.VOID);
		return vecs;
	}
	
	public Vector<Sign> FIRST(Sign X) {
		return first.get(X);
	}
	
	public Vector<Sign> FOLLOW(Sign X) {
		return follow.get(X);
	}
	
	private Vector<Sign> setFIRST(Sign X) {
		//System.out.println(X);
		if(first.containsKey(X))
			return first.get(X);
		Vector<Sign> signstr = new Vector<Sign>();
		boolean[] added = new boolean[Sign.SIGN_SIZE];
		boolean flag;
		Arrays.fill(added, true);
		while(true) {
			flag = true;
			if(isVt(X) && added[X.ordinal()])						// If X is terminal and not added
				flag = addSign(signstr, X, added);
			else if(isVn(X) && added[X.ordinal()]) {
				Vector<Vector<Sign>> ProductionsOfX = ProColl.get(X);
																	// Find all X -> ...
				for(int i = 0; i < ProductionsOfX.size(); i++) {	// Get a X -> ��
					boolean void_flag = true;
					for(int j = 0; j < ProductionsOfX.get(i).size(); j++) {
						Sign sign = ProductionsOfX.get(i).get(j);   // Traverse ��
						if(isVt(sign) && added[sign.ordinal()]) {   // if �� = a��, a is Vt, end traverse ��
							flag = addSign(signstr, sign, added);
							//System.out.println(X + "" +signstr);
							break;
						} else if(isVn(sign) && added[sign.ordinal()]) {
							if(sign.equals(X)) {
								if(signstr.indexOf(Sign.VOID) == -1)
									break;
								else
									continue;
							}
																	// if �� = Y��, Y is Vn
							Vector<Sign> Tmp = setFIRST(sign);      // Tmp = FIRST(Y)
							//System.out.println(sign +""+ Tmp);
							for(int k = 0; k < Tmp.size(); k++) {   // Traverse FIRST(Y)
								if(added[Tmp.get(k).ordinal()])
									flag = addSign(signstr, Tmp.get(k), added);
								if(Tmp.get(k).equals(Sign.VOID))	// If have Y -> ��
									void_flag = false;
							}
						} else if(sign.equals(Sign.VOID) && added[sign.ordinal()]) {
							flag = addSign(signstr, sign, added);
							break;
						}
						if(void_flag)
							break;									// X -> Y��, Y can't producte 
					}
				}
			}
			added[X.ordinal()] = false;
			if(flag)
				break;
		}
		first.put(X, signstr);
		return signstr;
	}
	
	private Vector<Sign> setFOLLOW(Sign X) {
		//System.out.println(X);
		if(follow.containsKey(X))
			return follow.get(X);
		Vector<Sign> signstr = new Vector<Sign>();
		boolean[] added = new boolean[Sign.SIGN_SIZE];
		boolean flag;
		Arrays.fill(added, true);
		while(true) {
			flag = true;
			if(X.equals(StartSign) && added[Sign.END.ordinal()])	// If X is StartSign and not added
				flag = addSign(signstr, Sign.END, added);
			for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
				ProColl.entrySet()) {
				for(int i = 0; i < entry.getValue().size(); i++) {	// Traverse A -> ...
					Vector<Sign> Searchstr = entry.getValue().get(i);
					int pos = Searchstr.indexOf(X);                 // Find Production that A -> ��B��
					if(pos == -1)									// Have not
						continue;
					if(pos == Searchstr.size()-1) {					// is A -> ��B
						if(Searchstr.get(pos).equals(entry.getKey()))
							continue;
						Vector<Sign> Tmp = setFOLLOW(entry.getKey());
						for(int j = 0; j < Tmp.size(); j++)
							if(added[Tmp.get(j).ordinal()])
								flag = addSign(signstr, Tmp.get(j), added);
						continue;
					}
					Vector<Sign> Beta = new Vector<Sign>();
					boolean void_flag = true;
					for(++pos; pos < Searchstr.size(); pos++)
						Beta.add(Searchstr.get(pos));
					Vector<Sign> Tmp = FIRST(Beta);
					//if(X.equals(Sign.LC))
					//System.out.println(X + " " + Searchstr + ": " + Tmp);
					for(Sign sign : Tmp) {
						if(sign.equals(Sign.VOID))
							void_flag = false;
						if(added[sign.ordinal()])
							flag = addSign(signstr, sign, added);
					}
					if(!void_flag && ! X.equals(entry.getKey())) {
						Tmp = setFOLLOW(entry.getKey());
						for(int index = 0; index < Tmp.size(); index++) 
							if(added[Tmp.get(index).ordinal()])
								flag = addSign(signstr, Tmp.get(index), added);
					}
				}
			}
			if(flag)
				break;
		}
		follow.put(X, signstr);
		return signstr;
	}
	
	public ProductionCollection EltSimLeftRec(Sign A) {								  // Eltiminate simple left recursive
	    	/*
		    * Start: A -> A�� | ��
		    * End: A -> ��R
		    *      R -> ��R | ��
		    */
			ProductionCollection ProAgg = new ProductionCollection(ProColl);
		    Production Pdc = NotLeftRec(A);                                           // Find A -> ��, then Pdc = A -> ��
		    Vector<Vector<Sign>> SetPro = ProAgg.get(A);					  	      // Find all A -> ..., then SetPro = ...
		    AllSign alls = new AllSign();
		    for(int i = 0; i < SetPro.size(); i++) { 
		        if(SetPro.get(i).firstElement().equals(A)) {                               // If find A -> A��
		            Sign sign = alls.getElement();     						          // Find SpareSign, then sign = R
		            Pdc.second().add(sign);                                           // Now Pdc = A -> ��R
		            ProAgg.put(Pdc);                                  				  // Add A -> ��R into ProAgg
		            Vector<Sign> temp = new Vector<Sign>();  					      
		            for(int index = 1; index < SetPro.get(i).size(); index++)         // Now temp = ��
		            	temp.add(SetPro.get(i).get(index));
		            temp.add(sign);                                                   // Now temp = ��R
		            ProAgg.put(sign, temp);                              			  // Add R -> ��R into ProAgg
		            if(temp.isEmpty())
		                ProAgg.put(sign, VOID_ELM);					              	  // Add R -> �� into ProAgg
		        } else
		            ProAgg.put(A, SetPro.get(i));
		    }
		    return ProAgg;
	}
	
	public ProductionCollection EltLeftRec() {										  // Eltiminate left recursive
	    ProductionCollection PdcAgg = new ProductionCollection();
	    for(int i = 0; i < Vn.size(); i++) {
	    	Sign Ai = Vn.get(i);
	        for(int j = 0; j < i; j++) {
	        	Sign Aj = Vn.get(j);
	            Vector<Vector<Sign>> SetPro = ProColl.get(Ai);
	            for(int k = 0; k < SetPro.size(); k++) {  					          // Traverse all Ai -> ...
	                if(SetPro.get(k).firstElement().equals(Aj)) {                     // Find Ai -> Aj��
	                	Vector<Vector<Sign>> SetProAj = ProColl.get(Aj);              // All Aj -> ...
	                    for(int m = 0; m < SetProAj.size(); m++) {
	                    	// Traverse all Aj -> ..., now Aj -> ��
	                        Vector<Sign> Delta = new Vector<Sign>(SetProAj.get(m));   // Delta = ��
	                        Vector<Sign> Gamma = new Vector<Sign>();                  
	                        for(int n = 1; n < SetPro.get(k).size(); n++) 			  // Gamma = ��
	                        	Gamma.add(SetPro.get(k).get(m));	                  
	                        Delta.addAll(Gamma);							  		  // Delta = �Ħ�
	                        PdcAgg.put(Ai, Delta);				                      // Add Ai -> �Ħ� into PdcAgg
	                    }
	                }
	            }
	        }
	        ProductionCollection temp = EltSimLeftRec(Ai);
	        PdcAgg.putAll(temp);
	    }
	    return PdcAgg;
	}
	
	private boolean addSign(Vector<Sign> signstr, Sign sign, boolean[] added) {
		signstr.add(sign);
		added[sign.ordinal()] = false;
		return false;
	}
	
	private Production NotLeftRec(Sign A) {
		Vector<Vector<Sign>> SetPro = ProColl.get(A);
	    for(int i = 0; i < SetPro.size(); i++) {
	    	if(SetPro.get(i).firstElement() != A)
	    		return new Production(A, SetPro.get(i));
	    }
	    return new Production(A, new Vector<Sign>());
	}
}
