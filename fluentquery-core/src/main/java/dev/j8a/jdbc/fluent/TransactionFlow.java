package dev.j8a.jdbc.fluent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

import dev.j8a.jdbc.fluent.transaction.TransactionActionStep;
import dev.j8a.jdbc.fluent.transaction.TransactionStarter;
import dev.j8a.jdbc.fluent.transaction.TransactionTerminalStep;

public class TransactionFlow<R> implements TransactionStarter, TransactionActionStep, TransactionTerminalStep<R> {

    private final ConnectionSupplier supplier;
    private TransactionalAction<R> action;
    private Consumer<Exception> exceptionHandler;

    public TransactionFlow(ConnectionSupplier supplier) {
        this.supplier = supplier;
    }

    public TransactionFlow(ConnectionSupplier supplier, TransactionalAction<R> action) {
        this.supplier = supplier;
        this.action = action;
    }

    @Override
    public TransactionActionStep onException(Consumer<Exception> handler) {
        this.exceptionHandler = handler;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TransactionTerminalStep<T> returnUsing(TransactionalAction<T> action) {
        this.action = (TransactionalAction<R>) action;
        return (TransactionTerminalStep<T>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TransactionTerminalStep<Void> run(TransactionalVoidAction action) {
        this.action = (TransactionalAction<R>) (cs -> {
            action.run(cs);
            return null;
        });
        return (TransactionTerminalStep<Void>) this;
    }

    @Override
    public R execute() throws Exception {
        try (Connection con = supplier.get()) {
            con.setAutoCommit(false);
            try {
                R result = action.run(ConnectionSupplier.borrowed(con));
                con.commit();
                return result;
            } catch (Exception e) {
                con.rollback();
                if (exceptionHandler != null) {
                    exceptionHandler.accept(e);
                }
                throw e;
            }
        }
    }
}
