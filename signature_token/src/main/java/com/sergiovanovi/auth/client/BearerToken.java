package com.sergiovanovi.auth.client;

import com.sergiovanovi.auth.SecurityConstant;
import com.sergiovanovi.auth.SecurityUtil;
import io.grpc.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.Base64;
import java.util.concurrent.Executor;

public class BearerToken implements CallCredentials {

    private PrivateKey privateKey;

    public BearerToken(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public void applyRequestMetadata(MethodDescriptor<?, ?> method, Attributes attrs, Executor executor, MetadataApplier metadataApplier) {
        String value = "missingSign";
        try {
            byte[] sign = SecurityUtil.getSign(privateKey);
            value = Base64.getEncoder().encodeToString(sign);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        String finalValue = value;
        executor.execute(() -> {
            try {
                Metadata headers = new Metadata();
                headers.put(SecurityConstant.AUTHORIZATION_METADATA_KEY, String.format("%s %s", SecurityConstant.BEARER_TYPE, finalValue));
                metadataApplier.apply(headers);
            } catch (Throwable e) {
                metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
            }
        });
    }

    @Override
    public void thisUsesUnstableApi() {
        // noop
    }
}
