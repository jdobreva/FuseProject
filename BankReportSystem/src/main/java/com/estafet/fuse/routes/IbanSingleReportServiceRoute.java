/**
 * 
 */
package com.estafet.fuse.routes;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.util.toolbox.AggregationStrategies;

import com.estafet.fuse.dto.IbanSingleReportEntity;
import com.estafet.fuse.json.IbanWrapper;
import com.estafet.fuse.json.ListIbanEntityWrapper;
import com.estafet.fuse.processors.strategy.IbanEntityAggregation;
import com.estafet.fuse.processors.strategy.ReportIbanEntityAggregation;

public class IbanSingleReportServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		ReportIbanEntityAggregation reportIbanEntityAggregation = (ReportIbanEntityAggregation) getContext().getRegistry().lookupByName("reportIbanEntityAggregation");
		
		onException(Throwable.class)
        .handled(true).transform(constant("!! WRONG !!!"))
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
        .log(LoggingLevel.ERROR, "${exception.message} ${exception.stacktrace}")
        .end();
		
		from("jetty:{{jetty.endpoint.entry.url}}?httpMethodRestrict=POST&continuationTimeout=5000")
				.routeId("jetty.entry.point")
				.unmarshal().json(JsonLibrary.Jackson, IbanWrapper.class, true)
				.setHeader("IbanTimestampOfRequest", simple("${date:now:yyyy MM dd HH mm ss SSS}"))
				.split(bodyAs(String.class).tokenize(", "))
					.log("${body}")
					.to(ExchangePattern.InOnly, "activemq:queue:estafet.iban.report.splitted.queue")
				.end()
				.setBody(constant("OK"));

		//
		from("direct:ibanService").routeId("ibanEntityDao").process("ibanEntityProcessor");

		//processing the data from the queue
		from("activemq:queue:estafet.iban.report.splitted.queue").routeId("messageProcessing")
				.process("ibanReportProcessor").enrich("direct:ibanService", new IbanEntityAggregation())
				.aggregate(header("IbanTimestampOfRequest"), reportIbanEntityAggregation).completionTimeout(2000)
				.marshal().json(JsonLibrary.Jackson, ListIbanEntityWrapper.class, true)
				.to("file://D:/jboss_workspace/testResults?fileName=${date:now:yyyy MM dd HH mm ss SSS}.txt");
	}

}
