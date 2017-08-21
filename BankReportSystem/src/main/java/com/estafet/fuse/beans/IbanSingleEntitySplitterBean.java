package com.estafet.fuse.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.estafet.fuse.model.IbanSingleReportEntity;

public class IbanSingleEntitySplitterBean {

	public IbanSingleEntitySplitterBean() {
		
	}
	
	public List<IbanSingleReportEntity> splitAccounts(Object obj){
		
		if (obj instanceof List) {
			return normalizeList((List) obj);	
		}
		
		List<IbanSingleReportEntity> list = new ArrayList<IbanSingleReportEntity>();
		if (obj instanceof Map){
			list.add(normalizeObject((Map)obj));
		}
		
		return list;
	}
	
	private List<IbanSingleReportEntity> normalizeList(List<?> someList){
		List<IbanSingleReportEntity> newList = new ArrayList<IbanSingleReportEntity>();
		for(Object item: someList){
			if (item instanceof Map){
				newList.add( normalizeObject((Map) item));
			}
		}
		return newList;
	}
	
	private IbanSingleReportEntity normalizeObject(Map data){
		IbanSingleReportEntity item = new IbanSingleReportEntity();
		item.setIban((String)data.get("iban"));
		item.setName((String)data.get("name"));
		item.setBalance((Double)data.get("balance"));
		return item;
	}
	

}
