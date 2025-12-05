package dev.oveja.jdbc.fluent.loader;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;

import java.util.Iterator;
import java.util.ServiceLoader;

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
