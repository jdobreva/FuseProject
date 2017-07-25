package com.estafet.fuse.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class IbanWrapper implements Serializable {

	private static final long serialVersionUID = -2746660741284359573L;
	
	private List<String> ibans;

	public List<String> getIbans() {
		if (ibans == null){
			ibans = new ArrayList<String>();
		}
		return ibans;
	}

	public void setIbans(List<String> ibans) {
		this.ibans = ibans;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		for (String iban: ibans){
			str.append(iban);
			str.append(", ");
		}
		if (str.length() > 0)
			str.setLength(str.length()-2);
		
		return str.toString();
	}
	
}
