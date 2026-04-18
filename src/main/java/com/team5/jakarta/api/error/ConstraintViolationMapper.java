package com.team5.jakarta.api.error;

import com.team5.jakarta.api.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        response.setError(Response.Status.BAD_REQUEST.getReasonPhrase());
        response.setMessage("Validation failed");
        response.setPath(uriInfo != null ? uriInfo.getPath() : "");

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            response.getViolations().add(
                    new ErrorResponse.Violation(violation.getPropertyPath().toString(), violation.getMessage())
            );
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
