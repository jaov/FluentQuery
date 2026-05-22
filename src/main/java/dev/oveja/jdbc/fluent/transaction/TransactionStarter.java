package dev.oveja.jdbc.fluent.transaction;

import java.util.function.Consumer;


import dev.oveja.jdbc.fluent.TransactionalAction;
import dev.oveja.jdbc.fluent.TransactionalVoidAction;

public interface TransactionStarter {
    TransactionActionStep onException(Consumer<Exception> handler);
    <R> TransactionTerminalStep<R> returnUsing(TransactionalAction<R> action);
    TransactionTerminalStep<Void> run(TransactionalVoidAction action);
}
