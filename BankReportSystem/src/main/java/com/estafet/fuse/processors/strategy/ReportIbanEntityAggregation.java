package com.estafet.fuse.processors.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.estafet.fuse.dto.IbanSingleReportEntity;
import com.estafet.fuse.json.ListIbanEntityWrapper;

public class ReportIbanEntityAggregation implements AggregationStrategy {
	
	public ReportIbanEntityAggregation() {
	}

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		
		ListIbanEntityWrapper list;
		
		if (oldExchange == null){
			list = new ListIbanEntityWrapper();
			list.addAccount(newExchange.getIn().getBody(IbanSingleReportEntity.class));
			newExchange.getIn().setBody(list);
			return newExchange;
		}
		list=oldExchange.getIn().getBody(ListIbanEntityWrapper.class);
		list.addAccount(newExchange.getIn().getBody(IbanSingleReportEntity.class));
		//oldExchange.getIn().setBody(list);
		return oldExchange;
	}

}
