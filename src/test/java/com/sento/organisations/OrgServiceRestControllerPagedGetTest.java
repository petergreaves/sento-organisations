package com.sento.organisations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sento.organisations.exceptions.ErrorDetails;
import com.sento.organisations.model.Organisation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrgServiceRestControllerPagedGetTest extends AbstractOrgTestBase {
	
	@Autowired
	private JdbcTemplate template;
	
	@Value("${test.paged-get.insert.0}")
	String insert0;

	@Value("${test.paged-get.delete.0}")
	String delete0;

	final String orgNameBase="PagedOrgName ";
	final String orgIdBase="PagedOrgId_";

	final String orgNameSymbol="#orgname#";
	final String orgIdSymbol="#orgid#";
	final String orgContactSymbol="#orgcontact#";
	final int numCreated=100;


	@Override
	@Before
//	@Sql(scripts = "classpath:beforeGetTestRun.sql")
	public void setUp() {
		super.setUp();
		
		// create a few orgs

		for (int i = 0; i < numCreated; i++) {
			String sql1 = insert0.replaceAll(orgNameSymbol, (orgNameBase+i)).replaceAll(orgContactSymbol, "Bob "+i);
			String sql2 = sql1.replace(orgIdSymbol, (orgIdBase+i));

			template.execute(sql2);
		}

	}
	
	
	@After
//	@Sql(scripts = "classpath:afterGetTestRun.sql")
	public void tearDown() {

		for (int i = 0; i < numCreated; i++) {
			String sql = delete0.replace(orgIdSymbol, (orgIdBase+i));
			template.execute(sql);
		}
		
	}
	
	

	@Test
	public void getPagedOrgsListSorted() throws Exception {

		int pageSize=20;
		String uri = "/v1/organisations?page=1&size="+pageSize+"&sort=name&dir=asc";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();

		JsonNode rootNode = mapper.readValue(content.getBytes(), JsonNode.class);
		JsonNode orgs = rootNode.get("content");

		for (JsonNode org : orgs) {

			System.out.println(org);

		}

		assertEquals(orgs.size(), pageSize);
		assertEquals(orgs.get(0).get("contactName").textValue(), "Bob 27");
		assertEquals(orgs.get(pageSize-1).get("contactName").textValue(), "Bob 44");

	}

}
