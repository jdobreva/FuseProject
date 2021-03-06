package com.estafet.fuse.processors;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.estafet.fuse.dao.AccountServiceApi;

public class AccountListProcessor implements Processor {
	
	private AccountServiceApi accountService;

	public AccountListProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		List<String> updatedAccounts = accountService.getAccountsWithRasedFlag();
		exchange.getOut().setBody(updatedAccounts);
	}
	
	public void setAccountService(AccountServiceApi accountServiceBean) {
		this.accountService =accountServiceBean;
	}

}
