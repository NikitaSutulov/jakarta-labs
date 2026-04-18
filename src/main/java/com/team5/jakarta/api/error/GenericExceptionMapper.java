package com.team5.jakarta.api.error;

import com.team5.jakarta.api.dto.ErrorResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        response.setError(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
        response.setMessage("Unexpected error");
        response.setPath(uriInfo != null ? uriInfo.getPath() : "");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
