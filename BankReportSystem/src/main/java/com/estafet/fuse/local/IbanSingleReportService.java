package com.estafet.fuse.local;

import java.util.HashMap;
import java.util.Map;

import com.estafet.fuse.dto.IbanSingleReportEntity;

public class IbanSingleReportService {
	
	private static final Map<String, IbanSingleReportEntity> records = new HashMap<String, IbanSingleReportEntity>();
	
	static {
		records.put("BG66 ESTF 0616 0000 0000 01", new IbanSingleReportEntity("BG66 ESTF 0616 0000 0000 01", "Ivan Ivanov", 100, "BGN"));
		records.put("BG66 ESTF 0616 0000 0000 02", new IbanSingleReportEntity("BG66 ESTF 0616 0000 0000 02", "Dimitar Iliev", 100, "BGN"));
		records.put("BG66 ESTF 0616 0000 0000 03", new IbanSingleReportEntity("BG66 ESTF 0616 0000 0000 03", "Ivan Miltenov", 100, "BGN"));
	}
	
	public IbanSingleReportService() {
		
	}
	
	public IbanSingleReportEntity getIbanSingleReportEntity(String ibanKey){
		return records.get(ibanKey);
	}
}
