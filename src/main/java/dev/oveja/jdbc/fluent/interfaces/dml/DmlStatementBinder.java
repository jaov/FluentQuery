package dev.oveja.jdbc.fluent.interfaces.dml;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;

public interface DmlStatementBinder {
    DmlStatementExecutor bind(ParameterBinder binder);

    default DmlStatementExecutor noBind() {
        return bind(ps->{});
    }
}
