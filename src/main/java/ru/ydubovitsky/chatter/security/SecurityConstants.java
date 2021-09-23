package ru.ydubovitsky.chatter.security;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/api/auth/**";
    public static final String SECRET = "SecretKey";
    public static final String TOKEN_PREFIX = "Bearer "; //! Должен быть пробел
    public static final String HEADER_STRING = "Authorization ";
    public static final String CONTENT_TYPE = "application/json ";
    public static final long EXPIRATION_TIME = 6_000_000; //100 min

}
