package dev.oveja.jdbc.fluent.transaction;

public interface TransactionTerminalStep<R> {
    R execute() throws Exception;

    default R justRuntimeIt() {
        try {
            return execute();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default R rethrow(RuntimeException wrapper) {
        try {
            return execute();
        } catch (Exception e) {
            wrapper.initCause(e);
            throw wrapper;
        }
    }
}
