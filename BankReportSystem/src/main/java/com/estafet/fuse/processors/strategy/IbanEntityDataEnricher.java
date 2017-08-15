package com.estafet.fuse.processors.strategy;

import org.apache.camel.Exchange;
/**
 * 
 */
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.estafet.fuse.model.Account;
import com.estafet.fuse.model.IbanSingleReportEntity;

public class IbanEntityDataEnricher implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		IbanSingleReportEntity currentData = oldExchange.getIn().getBody(IbanSingleReportEntity.class);
		Account dbData = newExchange.getIn().getBody(Account.class);
		
		if (dbData == null) {
			newExchange.setException(new Exception("No data is found for the specified IBAN "+ currentData.getIban()));
			return oldExchange;
		}
		
		currentData.setBalance(dbData.getBalance());
		currentData.setCurrency(dbData.getCurrency());
		currentData.setName(dbData.getName());
		return oldExchange;
	}

}
