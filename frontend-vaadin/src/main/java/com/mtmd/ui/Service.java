package com.mtmd.ui;

import com.mtmd.ui.infrastructure.client.ApiException;
import com.mtmd.ui.infrastructure.client.gen.types.IceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    final Apis apis;

    public Service(@Autowired Apis apis) {
        this.apis = apis;
    }

    public List<IceType> loadIce() {
        List<IceType> result;
        try {
            result = apis.getIceApi().getAllIceCream();
        } catch (Exception e) {
            logger.error("Could not load data!", e);
            result = Collections.emptyList();
        }
        return result;
    }
}
