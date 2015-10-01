/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.marketplace;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import craterdog.smart.SmartObject;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This class defines the attributes that are returned from a request to a service.
 *
 * @author Derk Norton
 * @param <T> The type of service response.
 */
public abstract class ServiceResponse<T extends SmartObject<T>> extends SmartObject<T> {

    /**
     * Whether or not the request succeeded.
     */
    public RequestStatus status;

    /**
     * An optional reason to be included on pending or failure.
     */
    public String reason;


    /*
     * This map is used to hold any JSON attributes that are not mappable to the existing attributes.
     */
    private final Map<String, Object> additional = new LinkedHashMap<>();


    /**
     * This method returns the value of the additional attribute associated with the specified
     * name, or null if none exists.
     *
     * @param name The name of the additional attribute to be returned.
     * @return The value of the attribute.
     */
    public Object get(String name) {
        return additional.get(name);
    }


    /**
     * This method allows the setting of additional attributes that are not explicitly defined.
     *
     * @param name The name of the additional attribute.
     * @param value The value to be associated with this attribute name.
     * @return Any previous attribute value associated with this attribute name.
     */
    @JsonAnySetter
    public Object put(String name, Object value) {
        return additional.put(name, value);
    }


    /**
     * This method returns a map of the additional attributes that are not explicitly defined.  It
     * is primarily used by the Jackson parser during deserialization of the corresponding JSON.
     *
     * @return A map containing the additional attributes.
     */
    @JsonAnyGetter
    public Map<String, Object> any() {
        return additional;
    }


    /**
     * This method allows the setting of multiple additional attributes that are not explicitly
     * defined.
     *
     * @param attributes
     */
    public void putAll(Map<String, Object> attributes) {
        for (Map.Entry<String, Object> attribute : attributes.entrySet()) {
            additional.put(attribute.getKey(), attribute.getValue());
        }
    }

}
