package com.sergiovanovi;

import com.sergiovanovi.auth.SecurityUtil;
import com.sergiovanovi.auth.client.BearerToken;
import com.sergiovanovi.grpc.api.hello.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class GrpcConfig {

    public static final String PRIVATE_KEY_PATH = "D:\\workspace\\grpc_token_signature\\grpc-client\\src\\main\\resources\\private_key.der";

    @Bean
    public HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8081)
                .usePlaintext()
                .build();

        BearerToken bearerToken = new BearerToken(SecurityUtil.getPrivateKeyFromFileName(PRIVATE_KEY_PATH));

        return HelloServiceGrpc
                .newBlockingStub(channel)
                .withCallCredentials(bearerToken);
    }
}
