package com.sergiovanovi;

import com.sergiovanovi.grpc.api.hello.HelloRequest;
import com.sergiovanovi.grpc.api.hello.HelloResponse;
import com.sergiovanovi.grpc.api.hello.HelloServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

    @GetMapping("/hello")
    @Secured("ADMIN")
    public ResponseEntity<String> get() {
        HelloResponse helloResponse = helloServiceBlockingStub.hello(
                HelloRequest.newBuilder()
                        .setFirstName("Serg")
                        .setLastName("Vanovi")
                        .build()
        );
        return ResponseEntity.ok(helloResponse.getGreeting());
    }
}
