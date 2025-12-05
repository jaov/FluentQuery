package dev.oveja.jdbc.fluent.interfaces.call;

public interface CallInParam<T> {
    CallOutParam<T> withInputParams(String... params);
}
