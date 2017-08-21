package com.estafet.fuse.routes;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class PersistAccountServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		onException(Throwable.class)
        .handled(true).transform(constant("!! There is a problem !!!"))
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
        .log(LoggingLevel.ERROR, "${exception.message} ${exception.stacktrace}")
        .end();
		
		from("jetty:{{jetty.endpoint.persist.url}}?httpMethodRestrict=POST&continuationTimeout=5000")
				.routeId("save.account.router")
				.unmarshal().json(JsonLibrary.Jackson, List.class, true).split()
				.method("accountDataSlitterBean", "splitAccounts")
				.log(LoggingLevel.INFO, "Received new Account " + body())
				.process("accountSavingRouter")
				.filter(header("SaveResult").isEqualTo(Boolean.TRUE))
				.log(LoggingLevel.INFO, "A new account ${body.iban} is created")
				.end()
				.setBody(constant("OK"));
	}

}
