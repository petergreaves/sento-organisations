package com.sento.organisations.controller;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sento.organisations.exceptions.ErrorDetails;
import com.sento.organisations.exceptions.InvalidOrganisationException;
import com.sento.organisations.exceptions.OrganisationAlreadyExistsException;
import com.sento.organisations.exceptions.OrganisationNotFoundException;
import com.sento.organisations.model.Organisation;
import com.sento.organisations.service.OrganisationService;

@RestController
public class OrganisationServiceController {

    Logger logger = LoggerFactory.getLogger(OrganisationServiceController.class);

    @Value("${service.external.address}")
    String host;

    @Value("${service.external.port}")
    String port;

    @Value("${service.base.path}")
    String basePath;

    @Autowired
    OrganisationService organisationService;

    @RequestMapping(value="/v1/organisations",method = RequestMethod.GET)
    public ResponseEntity<Iterable<Organisation>> getOrganisations() {

        logger.debug("GET received");
        Iterable<Organisation> allOrgs = organisationService.getAllOrganisations();

        if (allOrgs.iterator().hasNext()) {
            return new ResponseEntity<Iterable<Organisation>> (allOrgs, HttpStatus.OK);
        }
        else{
            logger.debug("No organisations returned for GET /");
            return new ResponseEntity<Iterable<Organisation>>(HttpStatus.NO_CONTENT);

        }
    }

    @RequestMapping(value="/v1/organisations/{organisationId}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?>  getOrganisationById(@PathVariable("organisationId") String organisationId) {

        Optional<Organisation> org= organisationService.getOrganisation(organisationId);


        if (org.isPresent()) {
            return new ResponseEntity<Organisation> (org.get(), HttpStatus.OK);
        }
        else {
            ErrorDetails errorDetails = new ErrorDetails(new Date(), "orgId="+organisationId, "Organisation not found with this ID");

            return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/v1/organisations/{organisationId}",method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<?> deleteOrganisationById(@PathVariable("organisationId") String organisationId) {

        Optional<Organisation> org=organisationService.deleteOrganisation(organisationId);

        Organisation deleted = null;
        if (org.isPresent()) {
            deleted = org.get();
            logger.debug("Organisation found to delete for id = " + organisationId);
            return new ResponseEntity<Organisation> (deleted, HttpStatus.NO_CONTENT);

        }
        else {
            ErrorDetails errorDetails = new ErrorDetails(new Date(), "orgId="+organisationId, "Organisation not found with this ID");
            return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/v1/organisations",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createOrganisation(@Valid @RequestBody Organisation newOrg)  {

        final String orgId = (newOrg.getOrgId()!=null?newOrg.getOrgId():"null ID");

        try{

            Organisation org=organisationService.saveOrganisation(newOrg);
            return new ResponseEntity<Organisation>(org, createLocationHeader(orgId), HttpStatus.CREATED);

        }catch(OrganisationAlreadyExistsException ex) {
            logger.error("Attempted to create duplicate org : " +orgId);
            ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "An Organisation already exists with this ID");

            return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.CONFLICT);

        }
        catch(InvalidOrganisationException ioe) {

            logger.error("Attempted to create invalid org : " +orgId);
            ErrorDetails errorDetails = new ErrorDetails(new Date(), orgId, "Organisation attributes are invalid");

            return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
        }

    }

    @RequestMapping(value="/v1/organisations/{organisationId}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrganisation(@PathVariable("organisationId") String organisationId, @Valid @RequestBody Organisation newOrg) {

        logger.info("Update for organisation in PUT for id "+ newOrg.getOrgId());
        Organisation updatedOrg;

        final String updatedOrgIdFromBody = (newOrg.getOrgId()!=null?newOrg.getOrgId():"null ID");

        if (! updatedOrgIdFromBody.equalsIgnoreCase(organisationId)) {
            logger.error("Update failed - path/body org id mismatch for "+ organisationId);
            ErrorDetails errorDetails = new ErrorDetails(new Date(), organisationId, "Update failed - path/body org id mismatch");

            return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
        }

        try{
            updatedOrg=organisationService.updateOrganisation(newOrg);
        }
        catch(OrganisationNotFoundException ex) {
            logger.error("Attempted to update non-existent  org : " +organisationId);
            ErrorDetails errorDetails = new ErrorDetails(new Date(), "orgId="+organisationId, "An Organisation already exists with this ID");

            return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);

        }
        catch(InvalidOrganisationException ioe) {

            logger.error("Attempted to update with invalid org attributes : " +organisationId);
            ErrorDetails errorDetails = new ErrorDetails(new Date(), organisationId, "Organisation attributes are invalid");

            return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
        }


        return new ResponseEntity<Organisation>(updatedOrg, createLocationHeader(updatedOrg.getOrgId()), HttpStatus.NO_CONTENT);


    }

    private HttpHeaders createLocationHeader(String orgId) {

        URI location = null;
        try {
            location = new URI(host+":"+port+basePath+orgId);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return responseHeaders;

    }

}
