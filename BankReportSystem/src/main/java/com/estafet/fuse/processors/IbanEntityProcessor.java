/**
 * 
 */
package com.estafet.fuse.processors;

import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.estafet.fuse.dao.IbanSingleReportService;
import com.estafet.fuse.model.IbanSingleReportEntity;

/**
 * @author jdobreva
 *
 */
public class IbanEntityProcessor implements Processor {

	@BeanInject
	private IbanSingleReportService ibanSingleReportDaoBean;
	
	public IbanEntityProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		IbanSingleReportEntity ibanData = exchange.getIn().getBody(IbanSingleReportEntity.class);
		
		if ((ibanData == null) || (ibanData.getIban() == null)){
			throw new Exception("Illegal argument to service request!");
		}
		
		IbanSingleReportEntity record = ibanSingleReportDaoBean.getIbanSingleReportEntity(ibanData.getIban());
		exchange.getIn().setBody(record);
	}

}
