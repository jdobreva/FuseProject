package com.estafet.fuse.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

public class IdempotentFilterRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("sftp://{{bankx.endpoint.output.host}}:{{bankx.endpoint.output.port}}/{{bankx.endpoint.output.dir}}?username={{bankx.endpoint.output.username}}&knownHostsFile={{bankx.endpoint.output.knownHostsFile}}&privateKeyFile={{bankx.endpoint.output.privateKeyFile}}&connectTimeout=20000&delay=60000")
				.routeId("scaningRemoteChanges").filter(header(Exchange.FILE_NAME).endsWith(".txt"))
				.idempotentConsumer(header(Exchange.FILE_NAME),
						MemoryIdempotentRepository.memoryIdempotentRepository(200))
				// Log the content of the files.
				.log(LoggingLevel.INFO, "File ${in.header.CamelFileName} received with body: ${in.body}")
				.to("file://{{local.repository}}");
	}
}
