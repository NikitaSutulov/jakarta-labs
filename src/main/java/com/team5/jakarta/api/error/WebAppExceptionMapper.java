package com.team5.jakarta.api.error;

import com.team5.jakarta.api.dto.ErrorResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse() != null ? exception.getResponse().getStatus() : 500;
        Response.StatusType statusType = exception.getResponse() != null
                ? exception.getResponse().getStatusInfo()
                : Response.Status.INTERNAL_SERVER_ERROR;

        ErrorResponse response = new ErrorResponse();
        response.setStatus(status);
        response.setError(statusType.getReasonPhrase());
        response.setMessage(exception.getMessage());
        response.setPath(uriInfo != null ? uriInfo.getPath() : "");

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
