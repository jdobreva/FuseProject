package com.estafet.fuse.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.estafet.fuse.dao.AccountServiceApi;
import com.estafet.fuse.model.Account;
import com.estafet.fuse.model.IbanSingleReportEntity;

public class AccountUpdateRouter implements Processor {
	
	private AccountServiceApi accountService;

	public AccountUpdateRouter() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		IbanSingleReportEntity entity = exchange.getIn().getBody(IbanSingleReportEntity.class);
		Account account = findAndUpdateAccount(entity);
		boolean resultFlag = accountService.updateAccount(account);
		exchange.getOut().setHeader("UpdateResult", Boolean.valueOf(resultFlag));
		exchange.getOut().setBody(account);
	}
	
	private Account findAndUpdateAccount(IbanSingleReportEntity entity) {
		if (entity == null){
			throw new IllegalStateException("The passed entity is null.");
		}
		
		Account account = accountService.getAccountByIban(entity.getIban());
		account.setBalance(entity.getBalance());
		account.setFlag(true);
		return account;
	}
	
	public void setAccountService(AccountServiceApi accountServiceBean) {
		this.accountService =accountServiceBean;
	}

}
