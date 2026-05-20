package dev.oveja.jdbc.fluent;

@FunctionalInterface
public interface TransactionalAction<R> {
    R run(BorrowedConnectionSupplier cs) throws Exception;
}
