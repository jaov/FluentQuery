package dev.oveja.jdbc.fluent.interfaces.throwing;

public interface ThrowingFunction <I,O,E extends Throwable>{
    O apply(I input) throws E;
}
