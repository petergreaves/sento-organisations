package com.sento.organisations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.sento.organisations.model.Organisation;

public class OrgServiceRestControllerUpdateTest extends AbstractOrgTestBase {

	@Autowired
	private JdbcTemplate template;

	@Value("${test.update.insert.0}")
	String insert0;
	@Value("${test.update.delete.0}")
	String delete0;

	private Organisation acmeUpdated;

	@Override
	@Before
//	@Sql(scripts = "classpath:beforeGetTestRun.sql")
	public void setUp() {
		super.setUp();

		acmeUpdated = new Organisation();
		acmeUpdated.setOrgId("BM023");
		acmeUpdated.setContactEmail("anne_updated@js.com");
		acmeUpdated.setName("JS Ltd updated");
		acmeUpdated.setContactPhone("0133334 23211 updated");
		acmeUpdated.setContactName("anne Jones updated");
		template.execute(insert0);

	}

	@After
//	@Sql(scripts = "classpath:afterGetTestRun.sql")
	public void tearDown() {

		acmeUpdated = null;

		template.execute(delete0);

	}

	@Test
	public void updateOrganisation() throws Exception {

		String uri = "/v1/organisations/BM023";

		String inputJson = super.mapToJson(acmeUpdated);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		// get it back

		String sql = "SELECT * FROM ORGANISATIONS WHERE ORG_ID = ?";

		Organisation updatedOrg = (Organisation) template.queryForObject(sql, new Object[] { "BM023" },
				new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Organisation o = new Organisation();
						o.setOrgId(rs.getString("ORG_ID"));
						o.setName(rs.getString("NAME"));
						o.setContactEmail(rs.getString("email"));
						o.setContactName(rs.getString("contact_name"));
						o.setContactPhone(rs.getString("contact_phone"));
						o.setId(rs.getInt("id"));
						return o;
					}

				});

		boolean match = (acmeUpdated.getContactEmail().equals(updatedOrg.getContactEmail())
				& acmeUpdated.getContactName().equals(updatedOrg.getContactName())
				& acmeUpdated.getContactPhone().equals(updatedOrg.getContactPhone())
				& acmeUpdated.getOrgId().equals(updatedOrg.getOrgId())
				& acmeUpdated.getName().equals(updatedOrg.getName())
				& 0 != updatedOrg.getId());
		assertTrue(match & status == 204);
	}

	@Test
	public void updateOrganisationFromJson() throws Exception {

		String uri = "/v1/organisations/BM023";

		String inputJson = "{\"orgId\":\"BM023\",\"name\":\"JS Ltd updated\",\"contactName\":\"anne Jones updated\",\"contactEmail\":\"anne_updated@js.com\",\"contactPhone\":\"0133334 23211 updated\"}";
		
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		// get it back

		String sql = "SELECT * FROM ORGANISATIONS WHERE ORG_ID = ?";

		Organisation updatedOrg = (Organisation) template.queryForObject(sql, new Object[] { "BM023" },
				new RowMapper() {

					public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
						Organisation o = new Organisation();
						o.setOrgId(rs.getString("ORG_ID"));
						o.setName(rs.getString("NAME"));
						o.setContactEmail(rs.getString("email"));
						o.setContactName(rs.getString("contact_name"));
						o.setContactPhone(rs.getString("contact_phone"));
						o.setId(rs.getInt("id"));
						return o;
					}

				});

		boolean match = ("anne_updated@js.com".equals(updatedOrg.getContactEmail())
				& "anne Jones updated".equals(updatedOrg.getContactName())
				& "0133334 23211 updated".equals(updatedOrg.getContactPhone())
				& "BM023".equals(updatedOrg.getOrgId())
				& "JS Ltd updated".equals(updatedOrg.getName())
				& 0 != updatedOrg.getId());
		assertTrue(match & status == 204);
	}

	@Test
	public void updateOrgReturns404IfNotFound() throws Exception {
		String uri = "/v1/organisations/BM099";

		acmeUpdated.setOrgId("BM099");

		String inputJson = super.mapToJson(acmeUpdated);
		// System.out.println(inputJson);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(404, status);
	}

	@Test
	public void updateOrgReturns405IfOrgIdMismatch() throws Exception {
		String uri = "/v1/organisations/BM099";

		String inputJson = super.mapToJson(acmeUpdated);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(405, status);
	}
	
	@Test
	public void failUpdateIfEmailNull() throws Exception{
		
		acmeUpdated.setContactEmail(null);
		
		String uri = "/v1/organisations/BM023";

		String inputJson = super.mapToJson(acmeUpdated);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(400, status);
	
	}
	
	@Test
	public void get400OnUpdateIfEmailInvalidFormat() throws Exception{
		
		acmeUpdated.setContactEmail("foo");
		
		String uri = "/v1/organisations/BM023";

		String inputJson = super.mapToJson(acmeUpdated);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(400, status);
	
	}
	
	@Test
	public void get400OnUpdateIfContactNameNull() throws Exception{
		
		acmeUpdated.setContactName(null);
		
		String uri = "/v1/organisations/BM023";

		String inputJson = super.mapToJson(acmeUpdated);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(400, status);
	
	}

}
