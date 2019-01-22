package com.sento.organisations.service;
import com.sento.organisations.model.Organisation;
import com.sento.organisations.exceptions.OrganisationAlreadyExistsException;
import com.sento.organisations.exceptions.OrganisationNotFoundException;
import com.sento.organisations.exceptions.InvalidOrganisationException;
import java.util.List;
import java.util.Optional;



public interface OrganisationService {

    Iterable<Organisation> getAllOrganisations();

    Optional<Organisation> getOrganisation(String organisationId);

    List<Organisation> getOrganisationByName(String name);

    /**
     *
     * Try to create the new org, but first check for pre-existence
     *
     * @param org
     * @return the created organisation
     * @throws OrganisationAlreadyExistsException
     */
    Organisation saveOrganisation(Organisation org) throws OrganisationAlreadyExistsException;

    Organisation updateOrganisation(Organisation org);

    Optional<Organisation> deleteOrganisation(String orgId);
}
