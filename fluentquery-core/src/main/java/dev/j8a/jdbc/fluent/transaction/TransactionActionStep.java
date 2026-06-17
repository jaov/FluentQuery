package dev.j8a.jdbc.fluent.transaction;

import dev.j8a.jdbc.fluent.TransactionalAction;
import dev.j8a.jdbc.fluent.TransactionalVoidAction;

public interface TransactionActionStep {
    <R> TransactionTerminalStep<R> returnUsing(TransactionalAction<R> action);
    TransactionTerminalStep<Void> run(TransactionalVoidAction action);
}
