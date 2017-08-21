package com.estafet.fuse.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.estafet.fuse.dao.AccountServiceApi;
import com.estafet.fuse.model.Account;
import com.estafet.fuse.model.IbanSingleReportEntity;

public class AccountRegistationRouter implements Processor {
	
	private AccountServiceApi accountService;

	public AccountRegistationRouter() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		IbanSingleReportEntity entity = exchange.getIn().getBody(IbanSingleReportEntity.class);
		Account account = prepareAccount(entity);
		boolean resultFlag = accountService.saveAccount(account);
		exchange.getOut().setHeader("SaveResult", Boolean.valueOf(resultFlag));
		exchange.getOut().setBody(account);
	}
	
	private Account prepareAccount(IbanSingleReportEntity entity) {
		if (entity == null){
			throw new IllegalStateException("The passed entity is null.");
		}
		
		Account account = new Account(entity.getIban());
		account.setBalance(entity.getBalance());
		account.setName(entity.getName());
		account.setFlag(false);
		return account;
	}
	
	public void setAccountService(AccountServiceApi accountServiceBean) {
		this.accountService =accountServiceBean;
	}

}
