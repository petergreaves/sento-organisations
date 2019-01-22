package com.sento.organisations.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrganisationAlreadyExistsException extends RuntimeException {

	public OrganisationAlreadyExistsException(String exception) {
	    super(exception);
	  }
}
