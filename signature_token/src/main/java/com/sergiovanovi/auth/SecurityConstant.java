package com.sergiovanovi.auth;

import io.grpc.Context;
import io.grpc.Metadata;

public class SecurityConstant {

    public static final String BEARER_TYPE = "Bearer";
    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");
    public static String KEY = "YouShallNotPass";
}
