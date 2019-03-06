package com.sento.organisations.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.sento.organisations.model.Organisation;

@Repository
public interface OrganisationRepository extends PagingAndSortingRepository<Organisation, Long> {

     List<Organisation> findByName(String orgName);
     Optional<Organisation> findByOrgId(String organisationId);
     Optional<Organisation> deleteByOrgId(String orgId);

   // @Query(value = "select * from organisations", nativeQuery = false)
   //  Page<Organisation> findAll(Pageable p);
}
