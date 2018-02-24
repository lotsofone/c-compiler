package Parser;

import java.util.Vector;

public class Production {
	private Sign sign;
	private Vector<Sign> signstr;
	
	public Production() {
		// Do nothing
	}
	public Production(Production Pdc) {
		sign = Pdc.first();
		signstr = new Vector<Sign>(Pdc.second());
	}
	public Production(Sign sign, Vector<Sign> signstr) {
		this.sign = sign;
		this.signstr = new Vector<Sign>(signstr);
	}
	public Production(Sign sign, String str, Sign... signstr) {
		this.sign = sign;
		this.signstr = new Vector<Sign>();
		for(int i = 0; i < signstr.length; i++)
			this.signstr.add(signstr[i]);
	}
	
	public Sign first() {
		return sign;
	}
	public Vector<Sign> second() {
		return signstr;
	}
	
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		final Production Pdc = (Production)obj;
		if(!Pdc.first().equals(this.first()))
			return false;
		if(!Pdc.second().equals(this.second()))
			return false;
		return true;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime*result + sign.hashCode();
		result = prime*result + signstr.hashCode();
		return result;
	}
	
	public Production read() {
		Vector<Sign> signstr = new Vector<Sign>(this.signstr);
		int pos = signstr.indexOf(Sign.POINT);
		if(pos == signstr.size()-1)
			return this;
		signstr.remove(pos);
		signstr.add(pos+1, Sign.POINT);
		return new Production(this.sign, signstr);
	}
	
	public void print() {
		System.out.println(sign + "->" + signstr);
	}
}
