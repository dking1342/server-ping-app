package io.getarrays.server.resource;

import io.getarrays.server.enums.Status;
import io.getarrays.server.model.Response;
import io.getarrays.server.model.Server;
import io.getarrays.server.service.ServerImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {

    private final ServerImplementation serverImplementation;

    @GetMapping("/list")
    public ResponseEntity<Response> getServers(){
        return ResponseEntity.ok(
                    Response.builder()
                            .timestamp(LocalDateTime.now())
                            .data(serverImplementation.list(30))
                            .message("Servers retrieved")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
        );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServers(@PathVariable("ipAddress") String ipAddress) throws IOException {
        ArrayList<Server> server = serverImplementation.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .data((ArrayList<Server>) server)
                        .message(server.get(0).getStatus() == Status.SERVER_UP ? "Ping success" : "Ping failed")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server){
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .data((ArrayList<Server>) serverImplementation.create(server))
                        .message("Server created")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServers(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .data((ArrayList<Server>) serverImplementation.get(id))
                        .message("Server retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServers(@PathVariable("id") Long id) throws IOException {
        ArrayList<Server> dataServer = serverImplementation.get(id);
        serverImplementation.delete(id);
        return ResponseEntity.ok(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .data( dataServer )
                        .message("Server deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping(path = "/image/{fileName}",produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/coding/practice/angular/images/" + fileName));
    }



}
