package dev.oveja.jdbc.fluent;

public interface ThrowingFunction <I,O,E extends Throwable>{
    O apply(I input) throws E;
}
