package com.ronjun.entities.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ronjun.entities.services.exceptions.DatabaseException;
import com.ronjun.exceptions.ClientNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ClientNotFoundException e , HttpServletRequest request){
		StandardError error = new StandardError();
		HttpStatus status = HttpStatus.NOT_FOUND;
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setMessage(e.getMessage());
		error.setError("not found");
		error.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e , HttpServletRequest request){
		StandardError error = new StandardError();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setMessage(e.getMessage());
		error.setError("database exception");
		error.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(error);
	}
	
	
}