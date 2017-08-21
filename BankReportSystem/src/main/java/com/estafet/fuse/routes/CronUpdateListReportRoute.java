package com.estafet.fuse.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.estafet.fuse.json.IbanWrapper;

public class CronUpdateListReportRoute extends RouteBuilder {

	public CronUpdateListReportRoute() {
	}

	public CronUpdateListReportRoute(CamelContext context) {
		super(context);
	}

	@Override
	public void configure() throws Exception {
		from("quartz2://ChronTaskAccountsReport/reportAccounts?cron=0+0/1+0/9-18+?+*+MON-FRI")
		.routeId("Cron Accounts Report Route")
		.process("List accounts Route")
		.choice()
			.when(simple("${body.size} > 0"))
			.log(LoggingLevel.INFO, "Found updated accounts")
				.process("ibanWrapperRouter")
				.marshal().json(JsonLibrary.Jackson, IbanWrapper.class)
				.to("jetty:{{jetty.endpoint.entry.url2}}?httpMethodRestrict=POST&continuationTimeout=5000")
			.otherwise()
				.log(LoggingLevel.INFO, "No accounts to update")
				.end();

	}

}
