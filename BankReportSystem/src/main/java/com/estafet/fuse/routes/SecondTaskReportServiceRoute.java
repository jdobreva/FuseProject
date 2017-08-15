/**
 * 
 */
package com.estafet.fuse.routes;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.estafet.fuse.json.IbanWrapper;
import com.estafet.fuse.json.ListIbanEntityWrapper;
import com.estafet.fuse.processors.strategy.IbanEntityAggregation;
import com.estafet.fuse.processors.strategy.ReportIbanEntityAggregation;

public class SecondTaskReportServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		ReportIbanEntityAggregation reportIbanEntityAggregation = (ReportIbanEntityAggregation) getContext()
				.getRegistry().lookupByName("reportIbanEntityAggregation");

		onException(Throwable.class).handled(true).transform(constant("There is a proble,"))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
				.log(LoggingLevel.ERROR, "${exception.message} ${exception.stacktrace}").end();

		from("jetty:{{jetty.endpoint.entry.url2}}?httpMethodRestrict=POST&continuationTimeout=5000")
				.routeId("jetty.entry.point2").unmarshal().json(JsonLibrary.Jackson, IbanWrapper.class, true)
				.setHeader("IbanTimestampOfRequest", simple("${date:now:yyyy MM dd HH mm ss SSS}"))
				.split(bodyAs(String.class).tokenize(", ")).log("${body}")
				.to(ExchangePattern.InOnly, "activemq:queue:estafet.iban.report.splitted.queue").end()
				.setBody(constant("OK"));

		//
		from("direct:ibanService").routeId("ibanEntityDao2").process("fakeDataProcessor");

		// processing the data from the queue
		from("activemq:queue:estafet.iban.report.splitted.queue").routeId("messageProcessing")
				.routeId("upload.created.file.to.sftp").process("ibanReportProcessor")
				.log(LoggingLevel.INFO, "Enriching the met entities")
				.enrich("direct:ibanService", new IbanEntityAggregation())
				.aggregate(header("IbanTimestampOfRequest"), reportIbanEntityAggregation).completionTimeout(2000)
				.marshal().json(JsonLibrary.Jackson, ListIbanEntityWrapper.class, true)
				.to("sftp://{{chm.nxp.batch.sftp.host}}:22/{{chm.nxp.batch.remote.error.dir}}?username={{chm.nxp.batch.sftp.username}}&knownHostsFile={{chm.known.hosts.file}}&privateKeyFile={{chm.nxp.batch.sftp.pk.file}}&connectTimeout=20000?fileName=${header.IbanTimestampOfRequest}.txt")
				.id("outputFile");
	}

}
