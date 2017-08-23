package com.estafet.fuse.processors;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.estafet.fuse.json.IbanWrapper;

public class IbanWrapperProcessor implements Processor {

	public IbanWrapperProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		List ibansData  = exchange.getIn().getBody(List.class);
		IbanWrapper ibanWrapper = new IbanWrapper();
		List<String> ibans =ibanWrapper.getIbans();
		for(Object obj:ibansData){
			ibans.add((String) obj);
		}
		exchange.getOut().setBody(ibanWrapper);
	}

}
