package com.mamdy.security;

public interface SecurityParams {
    public static final String JWT_HEADER_NAME="Authorization";
    public static final String SECRET="balphamamoudou2013@gmail.com";
    public static final long EXPIRATION=10*24*3600*1000; //dix jours
    public static final String HEADER_PREFIX="Bearer ";
}
