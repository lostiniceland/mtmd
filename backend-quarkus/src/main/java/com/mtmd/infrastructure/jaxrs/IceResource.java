package com.mtmd.infrastructure.jaxrs;

import com.mtmd.application.IceNotFoundException;
import com.mtmd.application.IceService;
import com.mtmd.domain.Ice;
import com.mtmd.infrastructure.jaxrs.gen.V1IceApi;
import com.mtmd.infrastructure.jaxrs.gen.types.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/ice")
public class IceResource implements V1IceApi {

    @Inject
    IceService service;
    @Inject
    IceMapper mapper;
    @Context
    UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok().entity(mapper.toResources(service.loadAllIceCreams())).build();
    }


    @Override
    public Response createIce(com.mtmd.infrastructure.jaxrs.gen.types.Ice ice) {
        Ice iceEntity = service.addIce(
                ice.getName(),
                mapper.toCategory(ice),
                ice.getIngredients(),
                Optional.ofNullable(ice.getNutrients()).orElse(0),
                mapper.mapStringToMoney(ice.getPurchasePrice()),
                mapper.mapStringToMoney(ice.getRetailPrice()),
                Optional.ofNullable(ice.getFoodIntolerances()));
        URI uri = uriInfo.getBaseUriBuilder().path(IceResource.class).path(String.valueOf(iceEntity.getName())).build();
        return Response.created(uri).entity(mapper.toResource(iceEntity)).build();
    }

    @Override
    public Response getAllIceCream() {
        List<com.mtmd.infrastructure.jaxrs.gen.types.Ice> result = mapper.toResources(service.loadAllIceCreams());
        return Response.ok().entity(result).build();
    }

    @Override
    public Response getIceByName(String name) {
        try {
            return Response.ok().entity(mapper.toResource(service.loadIce(name))).build();
        }catch(IceNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


//    @GET
//    @Path("{name}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getByName(@PathParam("name") String name) {
//        try {
//            return Response.ok().entity(mapper.toResource(service.loadIce(name))).build();
//        }catch(IceNotFoundException e){
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }

//    @POST
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createIce(IceType ice){
//        Ice iceEntity = service.addIce(
//                ice.name,
//                mapper.toCategory(ice.category),
//                ice.ingredients,
//                ice.nutrients,
//                mapper.mapStringToMoney(ice.purchasePrice),
//                mapper.mapStringToMoney(ice.retailPrice),
//                Optional.ofNullable(ice.foodIntolerances));
//        URI uri = uriInfo.getBaseUriBuilder().path(IceResource.class).path(String.valueOf(iceEntity.getName())).build();
//        return Response.created(uri).entity(mapper.toResource(iceEntity)).build();
//    }
}