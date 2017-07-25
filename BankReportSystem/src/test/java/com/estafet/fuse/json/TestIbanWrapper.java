package com.estafet.fuse.json;

import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;


public class TestIbanWrapper {

	@Test
	public void testIbanWrapper() {
		try{
			String jsonInString = "{\"ibans\":[\"BG66 ESTF 0616 0000 0000 01\", \"BG66 ESTF 0616 0000 0000 02\", \"BG66 ESTF 0616 0000 0000 03\"]}";
			ObjectMapper mapper = new ObjectMapper();
			IbanWrapper ibans = mapper.readValue(jsonInString, IbanWrapper.class);
			assertNotNull(ibans);
			System.out.println(ibans);
			assertTrue(ibans.getIbans().size() == 3);
		
		}catch(IOException jme)
		{
			jme.printStackTrace();
			fail("Mapping doesn't work.");
		}
	}

}
