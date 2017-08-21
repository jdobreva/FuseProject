package com.estafet.fuse.routes;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class UpdateAccountServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		onException(Throwable.class)
        .handled(true).transform(constant("!! There is a problem !!!"))
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
        .log(LoggingLevel.ERROR, "${exception.message} ${exception.stacktrace}")
        .end();
		
		from("jetty:{{jetty.endpoint.persist.url}}?httpMethodRestrict=POST&continuationTimeout=5000")
				.routeId("update.account.router")
				.unmarshal().json(JsonLibrary.Jackson, List.class, true).split()
				.method("accountDataSlitterBean", "splitAccounts")
				.log(LoggingLevel.INFO, "Received new Account " + body())
				.process("accountListRouter")
				.filter(header("UpdateResult").isEqualTo(Boolean.TRUE))
				.log(LoggingLevel.INFO, "The account ${body.iban} is updated")
				.end()
				.setBody(constant("OK"));
	}

}
