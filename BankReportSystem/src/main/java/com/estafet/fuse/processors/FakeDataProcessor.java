package com.estafet.fuse.processors;

import org.apache.camel.Exchange;
import com.estafet.fuse.model.Account;
import com.estafet.fuse.model.IbanSingleReportEntity;

import org.apache.camel.Processor;

import com.estafet.fuse.dao.AccountServiceApi;

public class FakeDataProcessor implements Processor {

	private AccountServiceApi accountServiceApi;
	
	public FakeDataProcessor() {
		
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		IbanSingleReportEntity ibanData = exchange.getIn().getBody(IbanSingleReportEntity.class);
        if (accountServiceApi != null) {
            Account account = accountServiceApi.getAccountByIban(ibanData.getIban());
            if (account != null) {
                account.setName(account.getName());
                account.setBalance(account.getBalance());
                account.setCurrency(account.getCurrency());
            }
        }
	}

	public AccountServiceApi getAccountService() {
		return accountServiceApi;
	}

	public void setAccountService(AccountServiceApi accountServiceApi) {
		this.accountServiceApi = accountServiceApi;
	}
}
