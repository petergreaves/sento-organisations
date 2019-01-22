package com.sento.organisations.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sento.organisations.model.Organisation;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation, Integer>{

    public List<Organisation> findByName(String orgName);
    public Optional<Organisation> findByOrgId(String organisationId);
    public Optional<Organisation> deleteByOrgId(String orgId);
}
