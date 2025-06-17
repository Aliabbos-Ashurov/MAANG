package com.abbos.maang.core.stream;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-03
 */
@SuppressWarnings("unchecked")
public class MapperStream<S, T> {
    private final List<MapperFunction<Object, Object>> operations = new CopyOnWriteArrayList<>();
    private final Class<S> sourceType;
    private RuntimeException ex;

    private MapperStream(Class<S> sourceType) {
        this.sourceType = sourceType;
    }

    /// the static method for start mapping process
    /// 
    /// @param sourceType is a class type which you will map
    /// @return MapperStream<S, S>
    public static <S> MapperStream<S, S> from(Class<S> sourceType) {
        return new MapperStream<>(sourceType);
    }

    public <V> MapperStream<S, V> map(MapperFunction<? super S, V> mapper) {
        operations.add((MapperFunction<Object, Object>) mapper);
        return (MapperStream<S, V>) this;
    }

    public MapperStream<S, T> onFailure(Supplier<? extends RuntimeException> ex) {
        this.ex = ex.get();
        return this;
    }

    public T execute(S source) {
        try {
            Object result = source;
            for (MapperFunction<Object, Object> op : operations) {
                result = op.map(result);
            }
            return (T) result;
        } catch (RuntimeException ignore) {
            throw this.ex;
        }
    }
}

class main {
    public static void main(String[] args) {
        User user = new User("abbos", 1);

        UserDto execute = MapperStream.from(User.class)
                .map(obj -> new UserDto(obj.name, obj.age))
                //.onFailure(() -> new UserMappingProcessFailure("something went wrong here!"))
                .execute(user);
        System.out.println(execute);
    }
}

class User {
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

record UserDto(String name, int age) {
}
