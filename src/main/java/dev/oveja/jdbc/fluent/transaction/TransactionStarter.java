package dev.oveja.jdbc.fluent.transaction;

import dev.oveja.jdbc.fluent.TransactionalAction;
import dev.oveja.jdbc.fluent.TransactionalVoidAction;
import java.util.function.Consumer;

public interface TransactionStarter {
    TransactionActionStep onException(Consumer<Exception> handler);
    <R> TransactionTerminalStep<R> returnUsing(TransactionalAction<R> action);
    TransactionTerminalStep<Void> run(TransactionalVoidAction action);
}
