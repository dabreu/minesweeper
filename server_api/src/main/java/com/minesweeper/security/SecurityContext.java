package com.minesweeper.security;

public class SecurityContext {

    private static ThreadLocal<String> user = new ThreadLocal<String>();

    public static void setPrincipal(String username) {
        user.set(username);
    }

    public static String getPrincipal() {
        return user.get();
    }
}
