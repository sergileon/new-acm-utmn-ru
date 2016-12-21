package ru.sibint.olymp.util;

import java.util.Random;

public class Utils {

    public static String genToken() {
        String ret = "";
        Random r = new Random(System.currentTimeMillis());
        for(int i = 0; i < 8; i++) {
            int x = r.nextInt(); if(x < 0) x = -x; x = x % 26;
            char c = (char)(x + 65);
            ret += c;
        }
        return ret;
    }

}
