package com.mtmd.infrastructure.jaxrs;

import com.mtmd.application.IceService;
import com.mtmd.domain.Ice;
import com.mtmd.infrastructure.jaxrs.gen.V1IceApi;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public class IceResource implements V1IceApi {

    @Inject
    IceService service;
    @Inject
    IceMapper mapper;
    @Context
    UriInfo uriInfo;
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
        URI uri = uriInfo.getBaseUriBuilder().path(V1IceApi.class).path(String.valueOf(iceEntity.getName())).build();
        return Response.created(uri).entity(mapper.toResource(iceEntity)).build();
    }

    @Override
    public Response getAllIceCream() {
        List<com.mtmd.infrastructure.jaxrs.gen.types.Ice> result = mapper.toResources(service.loadAllIceCreams());
        return Response.ok().entity(result).build();
    }

    @Override
    public Response getIceByName(String name) {
        return Response.ok().entity(mapper.toResource(service.loadIce(name))).build();
    }
}