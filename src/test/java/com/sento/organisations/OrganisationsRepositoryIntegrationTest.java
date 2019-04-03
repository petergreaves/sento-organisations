package com.sento.organisations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sento.organisations.OrganisationServiceApplication;
import com.sento.organisations.model.Organisation;
import com.sento.organisations.repository.OrganisationRepository;

//@RunWith(SpringRunner.class)
//@SpringBootTest 
//@ContextConfiguration(classes=OrgTestConfiguration.class, loader=AnnotationConfigContextLoader.class)

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrganisationServiceApplication.class)
public class OrganisationsRepositoryIntegrationTest {

	
	private int acmeId;
	private int dlcId;
	@Autowired
	private OrganisationRepository orgRepository;

	@Before
	public void setUp() throws Exception {
		Organisation acme = new Organisation();
		acme.setOrgId("ACME001");
		acme.setContactEmail("anne@acme.com");
		acme.setName("Acme Corp");
		acme.setContactPhone("0134 939499");
		acme.setContactName("anne smith");
		acmeId=this.orgRepository.save(acme).getId();
		Organisation delCorp = new Organisation();
		delCorp.setOrgId("DLCP001");
		delCorp.setContactEmail("jones@delcorp.com");
		delCorp.setName("DelCorp");
		delCorp.setContactPhone("03334939499");
		delCorp.setContactName("Bob Jones");
		dlcId=this.orgRepository.save(delCorp).getId();

		assertTrue(0 != dlcId & 0 != acmeId);
	}
	
	//@After
	public void tearDown() throws Exception {
		Organisation acme = new Organisation();
		acme.setOrgId("ACME001");
		
	//	this.orgRepository.deleteById(id);
		Organisation delCorp = new Organisation();
	//	this.orgRepository.save(delCorp);

	//	assertTrue(0 != acme.getId() & 0 != delCorp.getId());
	}

	//@Test
	public void testFetchAllOrgData() {

		/* Get all organisations, list should have four */
		Iterable<Organisation> orgs = orgRepository.findAll();
		int count = 0;
		for (Organisation o : orgs) {
			count++;
		}
		assertEquals(count, 4);
	}
	

	
	
}
