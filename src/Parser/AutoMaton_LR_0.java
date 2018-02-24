package Parser;

import java.util.Vector;
import java.util.Map;
import java.util.HashSet;
import java.util.Arrays;
import java.util.EnumSet;

@SuppressWarnings("serial")
public class AutoMaton_LR_0 extends Vector<ProductionCollection> {
	public Grammar Gram;
	private ProductionCollection AllItem;
	private Vector<Sign> UsefulSign;
	private int StateTransTable[][];
	
	public AutoMaton_LR_0() {
		Gram = new Grammar();
		UsefulSign = new Vector<Sign>();
		for(Sign sign : EnumSet.range(Sign.INT, Sign.END))
			UsefulSign.add(sign);
		AllItem = new ProductionCollection();
		StateTransTable = new int[Sign.SIGN_SIZE][Sign.SIGN_SIZE];
		for(int i = 0; i < Sign.SIGN_SIZE; i++)
			Arrays.fill(StateTransTable[i], -1);
		ProducteItem();
		items();
	}
	
	public AutoMaton_LR_0(Vector<ProductionCollection> items) {
		this.addAll(items);
	}

	public int[][] getTable() {
		return StateTransTable;
	}
	
	public void print() {
		for(int i = 0; i < this.size(); i++) {
			System.out.println("I[" + i + "]");
			this.get(i).print();
			System.out.println("\n");
		}
	}
	
	public void printKernel() {
		Vector<ProductionCollection> Kernel = getKernel();
		for(int i = 0; i < Kernel.size(); i++) {
			System.out.println("I[" + i + "]");
			Kernel.get(i).print();
			System.out.println("\n");
		}
	}
	
	public void printTable() {
		Vector<Sign> allsign = new Vector<Sign>();
		for(Sign sign : EnumSet.range(Sign.INT, Sign.POINT)) {
			allsign.add(sign);
		}
		for(int i = 0; i < this.size(); i++) {
			System.out.print("I[" + i + "]:\n");
			for(int j = 0; j < allsign.size(); j++) {
				int State = StateTransTable[i][allsign.get(j).ordinal()];
				if(State != -1) {
					System.out.println("       " + allsign.get(j)
					+ "      I[" + State + "]");
				}
			}
			System.out.println("\n");
		}
	}
	
	public Vector<ProductionCollection> getItems() {
		return new Vector<ProductionCollection>(this);
	}
	
	public AutoMaton_LR_0 getKernel() {
		Vector<Sign> Start = new Vector<Sign>();
		Start.add(Sign.POINT); Start.add(Sign.ExtDefList);				// Start = Program -> ��ExtDefList
		AutoMaton_LR_0 Kernel = new AutoMaton_LR_0();
		Kernel.clear();
		for(int i = 0; i < this.size(); i++) { 							// Traverse I[i]
			ProductionCollection Tmp = new ProductionCollection();
			for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :			
				this.get(i).entrySet()) {
				for(int j = 0; j < entry.getValue().size(); j++) {
					Vector<Sign> Pro = entry.getValue().get(j);			// Get the production in I[i]
					if(Pro.equals(Start) || !Pro.firstElement().equals(Sign.POINT))
						Tmp.put(entry.getKey(), Pro);					// Pro is kernel
				}
			}
			Kernel.add(Tmp);
		}
		return Kernel; 
	}
	
	public Vector<Production> getItemKernelList() {						// Turn AllItem to item kernel list
		Vector<Production> ItemKernelList = new Vector<Production>();
		if(AllItem.isEmpty())
			ProducteItem();
		for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
			AllItem.entrySet()) {
			for(int i = 0; i < entry.getValue().size(); i++) {
				Production Pdc = new Production(entry.getKey(), entry.getValue().get(i));
																		// Get a production in the AllItem
				ItemKernelList.add(Pdc);
			}
		}
		return ItemKernelList;
	}
	
	public int GOTO(int State, Sign X) {							// Find the state translate table
		return StateTransTable[State][X.ordinal()];
	}
	
	public int find(Production Pdc) {
		int pos, key = -1;
		for(pos = 0; pos < this.size(); pos++) {
			Vector<Vector<Sign>> allvecs = this.get(pos).get(Pdc.first());
			if(allvecs == null)
				continue;
			key = allvecs.indexOf(Pdc.second());
			if(key != -1)
				break;
		}
		return pos;
	}
	
	private void ProducteItem() {
		for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
			Gram.getProColl().entrySet()) {							// entrySet() = A -> ��|��|...
			for(int i = 0; i < entry.getValue().size(); i++) {		// get A -> ��
				Vector<Sign> item = entry.getValue().get(i);		// item = ��
				for(int index = 0; index <= item.size(); index++) {
					Vector<Sign> temp = new Vector<Sign>(item);
					temp.add(index, Sign.POINT);
					//System.out.println(temp);
					AllItem.put(entry.getKey(), temp);
				}
			}
		}
		//AllItem.print();
	}
	
	private ProductionCollection CLOSURE(ProductionCollection I) {
		ProductionCollection J = new ProductionCollection(I);
		boolean[] added = new boolean[Sign.SIGN_SIZE];			
		Arrays.fill(added, true);
		while(true) {
			boolean flag = true;
			flag = true;
			for(Map.Entry<Sign, Vector<Vector<Sign>>> entry : 
				I.entrySet()) {										// entrySet() = A -> ��|��|...
				for(int i = 0; i < entry.getValue().size(); i++) {  // get A -> ��
					Vector<Sign> item = entry.getValue().get(i);    // item = ��
					int pos = item.indexOf(Sign.POINT);
					if(pos == item.size()-1)						// if is A -> ���¡�
						continue;
																	// else, A -> ����B��
					if(Gram.getVt().indexOf(item.get(pos+1)) != -1) // if B is terminal or B is ��
						continue;
					Vector<Vector<Sign>> ProductionOfB = AllItem.get(item.get(pos+1));
																	// get B -> ��|��|..., have ��
					//if(find != -1)
					//	{System.out.println(J); }
					if(!added[item.get(pos+1).ordinal()])
						continue;
					//if(onlyone)
					//	System.out.println(J);
					for(int index = 0; index < ProductionOfB.size(); index++) {
						Vector<Sign> itemB = ProductionOfB.get(index);
																	// get B -> ��, have ��
						if(itemB.firstElement().equals(Sign.POINT))	{
																	// if is B -> ����
							J.put(item.get(pos+1), itemB);			// Add B -> ���� into I
							flag = false;
						}
					}
					//if(find != -1)
					//	System.out.println(item.get(pos+1));
					added[item.get(pos+1).ordinal()] = false;		// Mark, B is added
				}
			}
			if(flag)												// Now, have not item into I
				break;
			I = new ProductionCollection(J);
		}
		//if(onlyone)
		//	System.out.println(J);
		return J;
	}
	
	private ProductionCollection GOTO(ProductionCollection I, Sign X) {
		ProductionCollection ProColl = new ProductionCollection();
		HashSet<Vector<Sign>> added = new HashSet<Vector<Sign>>();
		for(Map.Entry<Sign, Vector<Vector<Sign>>> entry :
			I.entrySet()) {											// entrySet() = A -> ��|��|...
			for(int i = 0; i < entry.getValue().size(); i++) { 		
				Vector<Sign> item = new Vector<Sign>(entry.getValue().get(i));
																	// item = A -> ��
				int pos = item.indexOf(Sign.POINT);
				if(pos != item.size()-1 && item.get(pos+1).equals(X)) {		
																	// Find item = A -> ����B��
					item.remove(pos);
					item.add(pos+1, Sign.POINT);					// Now, item = A -> ��B����
					ProductionCollection Tmp = new ProductionCollection();
					Tmp.put(entry.getKey(), item);
					Tmp = CLOSURE(Tmp);								// Get CLOSURE of {A -> ��B����}
					for(Map.Entry<Sign, Vector<Vector<Sign>>> entryTmp :
						Tmp.entrySet()) {
						for(int j = 0; j < entryTmp.getValue().size(); j++) {
							Vector<Sign> itemTmp = entryTmp.getValue().get(j);

							if(added.contains(itemTmp)) 			// if added, continue
								continue;
							ProColl.put(entryTmp.getKey(), itemTmp);
							added.add(itemTmp);
						}
					}
				}
			}
		}
		return ProColl;
	}
	
	private void items() {
		ProductionCollection C = new ProductionCollection();
		Vector<Sign> Ext = new Vector<Sign>();
		Ext.add(Sign.POINT); Ext.add(Sign.ExtDefList);
		C.put(Sign.Program, Ext);									// C = {[Program -> ��ExtDefList]}
		this.add(CLOSURE(C)); 										// Now, I[0] add into DFA
		HashSet<ProductionCollection> added = new HashSet<ProductionCollection>();
		//this.get(0).print();
		while(true) {
			boolean flag = true;
			int size = this.size();
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < UsefulSign.size(); j++) {
					ProductionCollection ProColl = GOTO(this.get(i), UsefulSign.get(j));
																	// ProColl = GOTO(I, X)
					//if(i == 57)
					//	{ System.out.println(UsefulSign.get(j));ProColl.print();System.out.println("***********"); }
					if(!ProColl.isEmpty() && StateTransTable[i][UsefulSign.get(j).ordinal()] == -1
					&& !added.contains(ProColl)) {
						this.add(ProColl);							// Add GOTO(I, X) into DFA
						flag = false;
						added.add(ProColl);							// ProColl is added
						StateTransTable[i][UsefulSign.get(j).ordinal()] = this.size()-1;
					} else if(!ProColl.isEmpty() && 
					  StateTransTable[i][UsefulSign.get(j).ordinal()] == -1) {
						int pos = this.indexOf(ProColl);
						//if(i == 57)
						//	System.out.println(UsefulSign.get(j));
						StateTransTable[i][UsefulSign.get(j).ordinal()] = pos;
					}
				}
			}
			if(flag)												// Have not ProColl added
				break;
		}
	}
}
