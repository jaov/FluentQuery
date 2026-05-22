package dev.oveja.jdbc.fluent.internal;

import java.util.Iterator;
import java.util.ServiceLoader;


import dev.oveja.jdbc.fluent.ConnectionSupplier;

public class ConnectionSupplierLoader {
    public static ConnectionSupplier load() {
        ServiceLoader<ConnectionSupplier> loader = ServiceLoader.load(ConnectionSupplier.class);
        Iterator<ConnectionSupplier> iterator= loader.iterator();
        if(iterator.hasNext()) {
            return iterator.next();
        } else {
            throw new RuntimeException("No connection provider found");
        }

    }


}
