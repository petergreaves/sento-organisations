package com.sento.organisations.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.sento.organisations.exceptions.ErrorDetails;

import com.sento.organisations.exceptions.InvalidOrganisationException;
import com.sento.organisations.exceptions.OrganisationAlreadyExistsException;
import com.sento.organisations.exceptions.OrganisationNotFoundException;

@ControllerAdvice
public class OrganisationServiceControllerAdvice {

    @ExceptionHandler(OrganisationNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleOrganisationNotFoundException(OrganisationNotFoundException ex) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "Organisation not found with this ID");

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOrganisationException.class)
    public final ResponseEntity<ErrorDetails> handleInvalidOrganisationException(InvalidOrganisationException ex) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "Organisation attributes are invalid in update/create or doesn't match request path.");

        return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler(OrganisationAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleOrganisationAlreadyExistsException(OrganisationAlreadyExistsException ex) {


        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), "An Organisation already exists with this ID");

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
