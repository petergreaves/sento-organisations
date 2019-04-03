package com.sento.organisations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sento.organisations.exceptions.ErrorDetails;
import com.sento.organisations.model.Organisation;

public class OrgServiceRestControllerDeleteTest extends AbstractOrgTestBase {
	
	
	@Autowired
	private JdbcTemplate template;
	
	@Value("${test.delete.insert.0}")
	String insert0;
	@Value("${test.delete.delete.0}")
	String delete0;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		
		template.execute(insert0);
	}
	
	@After
	public void tearDown() {
		super.setUp();
		
		template.execute(delete0);
	}


	@Test
	public void deleteOrg() throws Exception {
		String uri = "/v1/organisations/BM023";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(204, status);
	}
	
	@Test
	public void deleteOrgReturns404IfNotFound() throws Exception {
		
		ErrorDetails expected = new ErrorDetails();
		expected.setMessage("orgId=BM099");
		expected.setDetail("Organisation not found with this ID");
		
		String uri = "/v1/organisations/BM099";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		ErrorDetails error = super.mapFromJson(content, ErrorDetails.class);
		int status = mvcResult.getResponse().getStatus();
			
		assertTrue(404==status & expected.getDetail().equals(error.getDetail()) & expected.getMessage().equals(error.getMessage()));
	}
	
	
	
}
