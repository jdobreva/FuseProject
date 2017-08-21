package com.estafet.fuse.route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.estafet.fuse.beans.IbanSingleEntitySplitterBean;
import com.estafet.fuse.dao.AccountServiceApi;
import com.estafet.fuse.model.Account;
import com.estafet.fuse.model.IbanSingleReportEntity;
import com.estafet.fuse.processors.AccountRegistationRouter;
import com.fasterxml.jackson.core.JsonGenerationException;

public class TestPersistAccountServiceRoute extends CamelTestSupport {

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;
	

	public TestPersistAccountServiceRoute() {
		
	}

	@Test
	public void testSendMatchingMessage() throws Exception {
		resultEndpoint.expectedMessageCount(3);
		template.requestBody(createServiceTestData());
		resultEndpoint.assertIsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() {
		IbanSingleEntitySplitterBean splitterBean = new IbanSingleEntitySplitterBean();
		AccountRegistationRouter savingRouter = new AccountRegistationRouter();
		savingRouter.setAccountService(new MyMockedAccountServiceApi());
		
		return new RouteBuilder() {
			public void configure() {
				from("direct:start").unmarshal().json(JsonLibrary.Jackson, List.class, true).split()
						.method(splitterBean, "splitAccounts").log("Item " + body())
						.filter(simple("${in.body} is 'com.estafet.fuse.model.IbanSingleReportEntity'"))
						.process(savingRouter).filter(simple("${in.body} is 'com.estafet.fuse.model.Account'"))
						.to("mock:result");
			}
		};
	}

	private String createServiceTestData() throws JsonGenerationException, IOException {
		List<IbanSingleReportEntity> entities = new ArrayList<IbanSingleReportEntity>();
		entities.add(new IbanSingleReportEntity("BG12313123123123", "Kamen Dimitrov", 10.0, null));
		entities.add(new IbanSingleReportEntity("BG12346453453312", "Damyan Gerogiev", 40, null));
		entities.add(new IbanSingleReportEntity("BG12313175672312", "Valeria Dimitrova", 10, null));
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(entities);
	}
	
	private class MyMockedAccountServiceApi implements AccountServiceApi{

		@Override
		public Account getAccountByIban(String iban) {
			return null;
		}

		@Override
		public boolean saveAccount(Account account) {
			return true;
		}

		@Override
		public List<String> getAccountsWithRasedFlag() {
			return null;
		}

		@Override
		public boolean updateAccount(Account account) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
