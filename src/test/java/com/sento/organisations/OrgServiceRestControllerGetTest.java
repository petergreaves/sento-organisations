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

public class OrgServiceRestControllerGetTest extends AbstractOrgTestBase {
	
	@Autowired
	private JdbcTemplate template;
	
	@Value("${test.get.insert.0}")
	String insert0;
	@Value("${test.get.insert.1}")
	String insert1;
	@Value("${test.get.insert.2}")
	String insert2;
	@Value("${test.get.insert.3}")
	String insert3;
	@Value("${test.get.delete.0}")
	String delete0;
	
	
	private Organisation acme;
	private Organisation bmc;

	@Override
	@Before
//	@Sql(scripts = "classpath:beforeGetTestRun.sql")
	public void setUp() {
		super.setUp();
		
		acme = new Organisation();
		acme.setOrgId("ACME001");
		
		bmc = new Organisation();
		bmc.setOrgId("BM022");
		
		template.execute(insert0);
		template.execute(insert1);
		template.execute(insert2);
		template.execute(insert3);
		
	}
	
	
	@After
//	@Sql(scripts = "classpath:afterGetTestRun.sql")
	public void tearDown() {
		
		
		acme = null;
		bmc=null;
		
		template.execute(delete0);
		
		
	}
	
	

	@Test
	public void getOrgsList() throws Exception {
		String uri = "/v1/organisations/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Organisation[] orgList = super.mapFromJson(content, Organisation[].class);
		assertTrue(orgList.length == 4);
	}
	
	@Test
	public void getOrgsListNoSlash() throws Exception {
		String uri = "/v1/organisations";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Organisation[] orgList = super.mapFromJson(content, Organisation[].class);
		assertTrue(orgList.length == 4);
	}

	
	@Test
	public void getAnOrgById() throws Exception {
		
		String uri = "/v1/organisations/ACME001/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		Organisation org = super.mapFromJson(content, Organisation.class);
		
		int status = mvcResult.getResponse().getStatus();
		assertTrue(200==status & org.equals(acme));
	
	}
	
	@Test
	public void getAnOrgByIdNoSlash() throws Exception {
		String uri = "/v1/organisations/ACME001";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		
		String content = mvcResult.getResponse().getContentAsString();
		Organisation org = super.mapFromJson(content, Organisation.class);
		
		int status = mvcResult.getResponse().getStatus();
		assertTrue(200==status & org.equals(acme));
	
	}
	
	@Test
	public void get404AndErrorIfNoOrgById() throws Exception{
			String uri = "/v1/organisations/ACME009/";
			
			ErrorDetails expected = new ErrorDetails();
			expected.setMessage("orgId=ACME009");
			expected.setDetail("Organisation not found with this ID");
				
			MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
					.andReturn();
			String content = mvcResult.getResponse().getContentAsString();
			ErrorDetails error = super.mapFromJson(content, ErrorDetails.class);
			int status = mvcResult.getResponse().getStatus();
			assertTrue(404==status & expected.getDetail().equals(error.getDetail()) & expected.getMessage().equals(error.getMessage()));
	}
	
	//@Test
	public void get401IfTokenisInvalid() throws Exception{
		
		String uri = "/v1/organisations/ACME001";
		
		
		//ErrorDetails expected = new ErrorDetails();
		//expected.setMessage("orgId=ACME001");
		//expected.setDetail("Organisation not found with this ID");
		
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.
				get(uri).header("username", "john.cantell").header("Authorization", "Bearer foo").header("Accept", MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		
		assertTrue(401==status);
	}
	

	
}
