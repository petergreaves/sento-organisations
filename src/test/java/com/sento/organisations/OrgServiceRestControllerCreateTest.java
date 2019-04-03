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

public class OrgServiceRestControllerCreateTest extends AbstractOrgTestBase {

	@Autowired
	private JdbcTemplate template;

	private Organisation acme;

	// for the duplicate check 409

	@Value("${test.create.delete.0}")
	String delete0;

	//private final String uri = "/v1/organisations/create";

	@Override
	@Before
	public void setUp() {
		super.setUp();

		acme = new Organisation();
		acme.setOrgId("JS0023");
		acme.setContactEmail("anne@js.com");
		acme.setName("JS Ltd");
		acme.setContactPhone("0133334 23211");
		acme.setContactName("anne Jones");

	}

	@After
	public void tearDown() {

		acme = null;

		template.execute(delete0);
	}

	@Test
	public void createOrganisation() throws Exception {
		final String uri = "/v1/organisations";

		String inputJson = super.mapToJson(acme);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String location = mvcResult.getResponse().getHeader("Location");
		assertTrue(201 == status && "http://services.sento.com:8080/v1/organisations/JS0023".equals(location));
	}

	@Test
	public void createOrganisationFromJson() throws Exception {

		String uri = "/v1/organisations";

		String inputJson = super.mapToJson(acme);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String location = mvcResult.getResponse().getHeader("Location");
		Organisation created = super.mapFromJson(mvcResult.getResponse().getContentAsString(), Organisation.class);
		assertTrue(201 == status
				&& "http://services.sento.com:8080/v1/organisations/JS0023".equals(location) & created.equals(acme));
	}

	@Test
	public void receive409IfOrgExists() throws Exception {
		
		final String uri = "/v1/organisations";

		ErrorDetails expected = new ErrorDetails();
		expected.setMessage("orgId=" + acme.getOrgId());
		expected.setDetail("An Organisation already exists with this ID");

		String inputJson = super.mapToJson(acme);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		// try to add the same org id
		MvcResult mvcResult2 = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();	

		int status = mvcResult2.getResponse().getStatus();
		ErrorDetails error = super.mapFromJson(mvcResult2.getResponse().getContentAsString(), ErrorDetails.class);

		assertTrue(409 == status & expected.getDetail().equals(error.getDetail())
				& expected.getMessage().equals(error.getMessage()));
	}

	@Test
	public void createOrgReturns405IfOrgIdMismatch() throws Exception {
		String uri = "/v1/organisations/BM099";

		String inputJson = super.mapToJson(acme);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(405, status);
	}

}
