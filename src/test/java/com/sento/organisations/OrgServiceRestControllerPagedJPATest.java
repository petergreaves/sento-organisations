package com.sento.organisations;

import com.sento.organisations.model.Organisation;
import com.sento.organisations.repository.OrganisationRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrgServiceRestControllerPagedJPATest extends AbstractOrgTestBase {
	

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

	final int numCreated=100;

	@Autowired
	private OrganisationRepository organisationRepository;

	@Override
	@Before
//	@Sql(scripts = "classpath:beforeGetTestRun.sql")
	public void setUp() {
		super.setUp();
		
		// create a few orgs

		for (int i = 0; i < numCreated; i++) {
			String sql1 = insert0.replaceAll(orgNameSymbol, (orgNameBase+i));
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
	public void testPagedOrgsList() throws Exception {

		Pageable firstPageWithTenElements = PageRequest.of(0, 10);
		Page<Organisation> orgsPage = organisationRepository.findAll(firstPageWithTenElements);
		assertEquals(orgsPage.getNumberOfElements(),10);
		assertEquals(orgsPage.getTotalPages(),numCreated/10);


	}

	@Test
	public void testPageRequestOverrun()  {

		Pageable pageable = PageRequest.of(11, 10);
		Page<Organisation> orgsPage = organisationRepository.findAll(pageable);

		assertEquals(orgsPage.getNumberOfElements(), 0);
	}

	@Test
	public void testSortedPageElements()  {

		Pageable pageable = PageRequest.of(1, 10, Sort.by("name"));
		Page<Organisation> orgsPage = organisationRepository.findAll(pageable);

		//orgsPage.forEach(System.out::println);

		assertEquals(orgsPage.get().findFirst().get().getName(), "PagedOrgName 18");
	}

}
