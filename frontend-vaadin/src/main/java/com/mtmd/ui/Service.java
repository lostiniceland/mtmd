package com.mtmd.ui;

import com.mtmd.ui.infrastructure.client.ApiException;
import com.mtmd.ui.infrastructure.client.gen.types.Ice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    final Apis apis;

    public Service(@Autowired Apis apis) {
        this.apis = apis;
    }

    public List<Ice> loadIce() throws BackendException {
        List<Ice> result;
        try {
            result = apis.getIceApi().getAllIceCream();
        } catch (Exception e) {
            logger.error("Could not load data!", e);
            throw new BackendException(e.getMessage());
        }
        return result;
    }

    public Optional<Ice> createIce(Ice ice) throws BackendException {
        Ice created;
        try {
            apis.getIceApi().createIce(ice);
            created = apis.getIceApi().getIceByName(ice.getName());
        } catch(ApiException e){
            logger.error("Could not create new ice!", e);
            throw new BackendException(String.format("Could not create new ice! %s", e.getResponseBody()));
        } catch (Exception e) {
            logger.error("Could not create new ice!", e);
            throw new BackendException(e.getMessage());
        }
        return Optional.ofNullable(created);
    }
}
