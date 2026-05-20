package dev.oveja.jdbc.fluent;

@FunctionalInterface
public interface TransactionalVoidAction {
    void run(BorrowedConnectionSupplier cs) throws Exception;
}
