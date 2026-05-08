package dev.oveja.jdbc.fluent;

public interface ThrowingConsumer <I,E extends Throwable>{
    void accept(I input) throws E;
}
