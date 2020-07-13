package com.sergiovanovi.grpc.server;

import com.sergiovanovi.auth.SecurityUtil;
import com.sergiovanovi.auth.server.TokenServerInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class HelloServer {

    public static final int SERVER_PORT = 8081;
    public static final String PUBLIC_KEY_PATH = "D:\\workspace\\grpc_token_signature\\grpc-server\\src\\main\\resources\\public_key.der";

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, SignatureException {

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .intercept(new TokenServerInterceptor(SecurityUtil.getPublicKeyFromFileName(PUBLIC_KEY_PATH)))
                .addService(new HelloServiceImpl()).build();
        server.start();
        System.out.println("Started on port:" + SERVER_PORT);
        server.awaitTermination();
        System.out.println("Finished");
    }
}
