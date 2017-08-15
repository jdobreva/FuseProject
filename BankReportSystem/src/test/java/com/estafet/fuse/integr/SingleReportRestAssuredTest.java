package com.estafet.fuse.integr;

import static io.restassured.RestAssured.given;
//import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.estafet.fuse.json.IbanWrapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SingleReportRestAssuredTest {

	@Before
	public void setUp() throws Exception {
		 RestAssured.baseURI = "http://localhost:20616";
	}

	@Test
	public void testRestService() throws JsonGenerationException, IOException {
		given()
			.contentType(ContentType.JSON)
			.body(prepareIbanParameterData())
		.when()
			.post("/estafet/iban/report")
		.then()
			.assertThat()
				.statusCode(is("200"))
				.body("/", equalTo("OK"));
	}

	private String prepareIbanParameterData() throws JsonGenerationException, IOException {
		IbanWrapper ibans = new IbanWrapper();
		List<String> list = ibans.getIbans();
		list.add("BG66 ESTF 0616 0000 0000 01");
		list.add("BG66 ESTF 0616 0000 0000 02");
		list.add("BG66 ESTF 0616 0000 0000 03");
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(ibans);
	}
}
