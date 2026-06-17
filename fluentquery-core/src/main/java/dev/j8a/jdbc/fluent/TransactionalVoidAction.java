package dev.j8a.jdbc.fluent;

@FunctionalInterface
public interface TransactionalVoidAction {
    void run(BorrowedConnectionSupplier cs) throws Exception;
}
