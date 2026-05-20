package dev.oveja.jdbc.fluent.transaction;

import dev.oveja.jdbc.fluent.TransactionalAction;
import dev.oveja.jdbc.fluent.TransactionalVoidAction;

public interface TransactionActionStep {
    <R> TransactionTerminalStep<R> returnUsing(TransactionalAction<R> action);
    TransactionTerminalStep<Void> run(TransactionalVoidAction action);
}
