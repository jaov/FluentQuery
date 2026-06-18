package dev.j8a.jdbc.fluent.transaction;

import java.util.function.Consumer;


import dev.j8a.jdbc.fluent.TransactionalAction;
import dev.j8a.jdbc.fluent.TransactionalVoidAction;

public interface TransactionStarter {
    TransactionActionStep onException(Consumer<Exception> handler);
    <R> TransactionTerminalStep<R> returnUsing(TransactionalAction<R> action);
    TransactionTerminalStep<Void> run(TransactionalVoidAction action);
}
