package com.lukaszbaran.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Lukasz Baran (baranl1)
 */

//@Component
@Path("/rest")
@Api(value = "/rest", description = "Wydmuszka RESTful Web Services.")
public class Wydmuszka {

	private static final String SECRET = "{\"id\" : \"mpl\" }";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{secret}")
	@ApiOperation(value = "Get Secret Message", response = Response.class)
	public Response getGroupById(
			@ApiParam(value = "Secret parameter", required = true)
			@PathParam("secretParam") String secretParam) {
		try {
			System.out.println(secretParam);
			return Response.ok(SECRET, MediaType.APPLICATION_JSON).build();
		} catch (IllegalArgumentException iae) {
			return Response.status(Response.Status.BAD_REQUEST).entity("404").type(MediaType.TEXT_PLAIN).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("500").type(MediaType.TEXT_PLAIN).build();
		}
	}
}
