package dev.oveja.jdbc.fluent.interfaces.throwing;

public interface ThrowingConsumer <I,E extends Throwable>{
    void accept(I input) throws E;
}
