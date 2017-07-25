package com.estafet.fuse.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.estafet.fuse.model.IbanSingleReportEntity;

public class ListIbanEntityWrapper implements Serializable {

	private static final long serialVersionUID = -5898228127354025696L;
	private List<IbanSingleReportEntity> accounts;

	public ListIbanEntityWrapper() {
		accounts = new ArrayList<IbanSingleReportEntity>();
	}

	public List<IbanSingleReportEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<IbanSingleReportEntity> accounts) {
		this.accounts = accounts;
	}
	
	public void addAccount(IbanSingleReportEntity account) {
		this.accounts.add(account);
	}
}
