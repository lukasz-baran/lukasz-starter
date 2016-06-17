package com.lukaszbaran.starter.rest;

import com.lukaszbaran.starter.services.MonitoringService;
import com.lukaszbaran.starter.watcher.CameraDescription;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/monitoring")
public class MonitoringRestService {

    private final MonitoringService monitoringService;

    public MonitoringRestService(MonitoringService userService) {
        this.monitoringService = userService;
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<CameraDescription> getAllCamerasInJSON() {
        return monitoringService.getAll();
    }

    @GET
    @Path("enable")
    @Produces(MediaType.TEXT_PLAIN)
    public String enable() {
        monitoringService.enable(true);
        return "";
    }

    @GET
    @Path("disable")
    @Produces(MediaType.TEXT_PLAIN)
    public String disable() {
        monitoringService.enable(false);
        return "";
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() {
        return Boolean.toString(monitoringService.getStatus());
    }


//    @GET
//    @Path("{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public User getUserById(@PathParam("id") int id) {
//        return monitoringService.getById(id);
//    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public User create(User user) {
//        return monitoringService.createNewUser(user);
//    }

//    @PUT
//    @Path("{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public User update(User user) {
//        return monitoringService.update(user);
//    }

//    @DELETE
//    @Path("{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public void remove(@PathParam("id") int id) {
//        monitoringService.remove(id);
//    }
}
