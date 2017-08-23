/**
 * 
 */
package com.estafet.fuse.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.estafet.fuse.dto.IbanSingleReportEntity;

/**
 * @author jdobreva
 *
 */
public class IbanSingleEntityReportProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		String iban = exchange.getIn().getBody(String.class);
		exchange.getIn().setBody(new IbanSingleReportEntity(iban));
	}
}
