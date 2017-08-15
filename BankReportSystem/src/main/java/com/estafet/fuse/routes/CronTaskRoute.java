package com.estafet.fuse.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class CronTaskRoute extends RouteBuilder {

	public CronTaskRoute() {
	}

	public CronTaskRoute(CamelContext context) {
		super(context);
	}

	@Override
	public void configure() throws Exception {
		from("quartz2://myFirstChonJob/myTimerName?cron=0+0/1+0/9-18+?+*+MON-FRI")
		.routeId("Cron Route")
		.log(LoggingLevel.INFO, "Hello, this is chron job: " + simple("${date:now:hh-mm-ss}"))
		.end();

	}

}
