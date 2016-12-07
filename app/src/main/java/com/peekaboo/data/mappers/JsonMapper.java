package com.peekaboo.data.mappers;

/**
 * Created by arkadii on 12/7/16.
 */

public interface JsonMapper {
    String toJson(Object o);
    <T> T fromJson(String o, Class<T> classOfT);
}
