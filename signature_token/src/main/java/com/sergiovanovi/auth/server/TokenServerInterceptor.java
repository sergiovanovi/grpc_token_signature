package com.sergiovanovi.auth.server;

import com.sergiovanovi.auth.SecurityConstant;
import com.sergiovanovi.auth.SecurityUtil;
import io.grpc.*;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class TokenServerInterceptor implements ServerInterceptor {

    private final PublicKey publicKey;

    public TokenServerInterceptor(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        String value = metadata.get(SecurityConstant.AUTHORIZATION_METADATA_KEY);
        Status status;
        if (value == null) {
            status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
        } else if (!value.startsWith(SecurityConstant.BEARER_TYPE)) {
            status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
        } else {
            try {
                String token = value.substring(SecurityConstant.BEARER_TYPE.length()).trim();
                byte[] sign = Base64.getDecoder().decode(token);
                Signature signature = SecurityUtil.getSignatureForVerify(publicKey);
                if (!signature.verify(sign)) {
                    throw new StatusRuntimeException(Status.FAILED_PRECONDITION);
                }
                Context ctx = Context.current().withValue(SecurityConstant.CLIENT_ID_CONTEXT_KEY, token);
                return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
            } catch (Exception e) {
                status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
            }
        }

        serverCall.close(status, metadata);
        return new ServerCall.Listener<>() {
            // noop
        };
    }
}
