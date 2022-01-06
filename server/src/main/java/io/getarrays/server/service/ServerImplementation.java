package io.getarrays.server.service;

import io.getarrays.server.enums.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerImplementation implements ServerService{
    private final ServerRepository serverRepository;

    @Override
    public ArrayList<Server> create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        Server serve = serverRepository.save(server);
        ArrayList<Server> servers = new ArrayList<Server>();
        servers.add(serve);
        return servers;
    }

    @Override
    public ArrayList<Server> ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        ArrayList<Server> servers = new ArrayList<>();
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(1000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        servers.add(server);
        return servers;
    }

    @Override
    public ArrayList<Server> list(int limit) {
        log.info("Fetching all servers");
        List<Server> serverList = serverRepository.findAll(PageRequest.of(0,limit)).toList();
        ArrayList<Server> servers = new ArrayList<Server>(serverList);
        return servers;
    }

    @Override
    public ArrayList<Server> get(Long id) {
        log.info("Fetching server by id: {}", id);
        ArrayList<Server> servers = new ArrayList<>();
        Server server = serverRepository.findById(id).get();
        servers.add(server);
        return servers;
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by id: {}", id);
        serverRepository.deleteById(id);
        return true;
    }

    private String setServerImageUrl() {
        String[] imageNames = {
                "server1.png",
                "server2.png",
                "server3.png",
                "server4.png"
        };
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }

}
