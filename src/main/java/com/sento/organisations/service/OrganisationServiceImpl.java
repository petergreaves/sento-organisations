package com.sento.organisations.service;

import com.sento.organisations.repository.OrganisationRepository;
import com.sento.organisations.exceptions.OrganisationAlreadyExistsException;
import com.sento.organisations.exceptions.OrganisationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sento.organisations.model.Organisation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service("organisationService")
public class OrganisationServiceImpl implements OrganisationService{

    @Autowired
    private OrganisationRepository organisationRepository;

    /* (non-Javadoc)
     * @see com.thoughtmechanix.organisations.services.OrganisationsService#getAllOrganisations()
     */
    @Override
    public Page<Organisation> getAllOrganisations(int page, int size, String sortBy) {

        // handle sort request

        Sort.Direction d = Sort.Direction.ASC;
        if (null!=sortBy || "desc".equals(sortBy)){
            d=Sort.Direction.DESC;
        }

        Sort sort = new Sort(d, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Organisation> pageOfOrgs = organisationRepository.findAll(pageable);
      //  Iterable<Organisation> orgList = page.

        return pageOfOrgs;
    }

    /* (non-Javadoc)
     * @see com.thoughtmechanix.organisations.services.OrganisationsService#getOrganisation(java.lang.String)
     */
    @Override
    public Optional<Organisation> getOrganisation(String organisationId) {
        Optional<Organisation> org = organisationRepository.findByOrgId(organisationId);

        return org;
    }

    /* (non-Javadoc)
     * @see com.thoughtmechanix.organisations.services.OrganisationsService#getOrganisationByName(java.lang.String)
     */
    @Override
    public List<Organisation> getOrganisationByName(String name) {
        List<Organisation> orgList = organisationRepository.findByName(name);

        return orgList;
    }

    /* (non-Javadoc)
     * @see com.thoughtmechanix.organisations.services.OrganisationsService#saveOrganisation(com.thoughtmechanix.organisations.model.Organisation)
     */
    @Override
    public Organisation saveOrganisation(Organisation org) throws OrganisationAlreadyExistsException {

        Organisation createdOrg;

        String id = org.getOrgId();
        Optional<Organisation> alreadyExistsOpt = organisationRepository.findByOrgId(id);

        try {
            alreadyExistsOpt.get();

            throw new OrganisationAlreadyExistsException("orgId="+id);
        } catch (NoSuchElementException nse) // no existing org, so we are ok to save this one
        {

            createdOrg = organisationRepository.save(org);
        }

        return createdOrg;

    }

    /* (non-Javadoc)
     * @see com.thoughtmechanix.organisations.services.OrganisationsService#updateOrganisation(com.thoughtmechanix.organisations.model.Organisation)
     */
    @Override
    public Organisation updateOrganisation(Organisation org) {

        // what org id is being updated?
        String id = org.getOrgId();

        Organisation orgToBeUpdated;
        Organisation updatedOrg;

        Optional<Organisation> testForOrg = organisationRepository.findByOrgId(id);

        // if it exists, update the ID to match the existing internal ID
        // and then save it.  otherwise no such element

        try {
            orgToBeUpdated = testForOrg.get();
            org.setId(orgToBeUpdated.getId());
            updatedOrg=organisationRepository.save(org);

        } catch (NoSuchElementException nse) // no existing org, so an error on an update
        {
            throw new OrganisationNotFoundException("Organisation doesn't exist to update.");

        }

        return updatedOrg;

    }

    /* (non-Javadoc)
     * @see com.thoughtmechanix.organisations.services.OrganisationsService#deleteOrganisation(java.lang.String)
     */
    @Override
    public Optional<Organisation> deleteOrganisation(String orgId){

        Optional<Organisation> opt = organisationRepository.findByOrgId(orgId);

        if (opt.isPresent()) {
            organisationRepository.deleteByOrgId(orgId);
        }
        else {

            throw new OrganisationNotFoundException("orgId=" + orgId);

        }
        return opt;

    }


}
