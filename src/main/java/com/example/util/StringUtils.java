package com.example.util;

import java.util.Collection;
import java.util.stream.Collectors;

public final class StringUtils {
    private StringUtils() {

    }

    /**
     * Converts a collection of objects into a single string, separated by a specified delimiter.
     *
     * @param objects   the collection of objects to be converted
     * @param separator the string to be used as a delimiter between objects
     * @return a single string representation of the collection, separated by the specified delimiter
     */
    public static String collectionToSingleString(Collection<?> objects, String separator) {
        return objects.stream().map(Object::toString).collect(Collectors.joining(separator));
    }
}
