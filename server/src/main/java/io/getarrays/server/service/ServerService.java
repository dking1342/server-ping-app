package io.getarrays.server.service;

import io.getarrays.server.model.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public interface ServerService {

    ArrayList<Server> create(Server server);
    ArrayList<Server> ping(String ipAddress) throws IOException;
    Collection<Server> list(int limit);
    ArrayList<Server> get(Long id);
    Server update(Server server);
    Boolean delete(Long id);
}
