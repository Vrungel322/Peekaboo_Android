package com.peekaboo.data.mappers;


/**
 * Created by Arkadiy on 05.06.2016.
 */
public interface Mapper<A, B> {
    B transform(A obj) throws RuntimeException;
}
