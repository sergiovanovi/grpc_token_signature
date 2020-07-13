package com.sergiovanovi.grpc.server;


import com.sergiovanovi.auth.SecurityConstant;
import com.sergiovanovi.auth.SecurityUtil;
import com.sergiovanovi.grpc.api.hello.HelloRequest;
import com.sergiovanovi.grpc.api.hello.HelloResponse;
import com.sergiovanovi.grpc.api.hello.HelloServiceGrpc;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest request, io.grpc.stub.StreamObserver<HelloResponse> responseObserver) {
        String result = request.getFirstName() + " " + request.getLastName() + " hello from " + SecurityConstant.CLIENT_ID_CONTEXT_KEY.get();
        HelloResponse response = HelloResponse.newBuilder().setGreeting(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
