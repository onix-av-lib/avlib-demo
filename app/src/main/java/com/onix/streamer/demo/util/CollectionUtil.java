package com.onix.streamer.demo.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {

    public interface ITypeConverter<T, S> {
        S from(T object);
    }

    public static <T, S> List<S> convertToList(List<T> collection, ITypeConverter<T, S> converter) {
        List<S> converted = new ArrayList<>();
        for (T item : collection) {
            if (item != null)
                converted.add(converter.from(item));
        }
        return converted;
    }
}
