package com.nuoze.cctower.common.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author JiaShun
 * @date 2019-03-03 08:24
 */
public class MD5Utils {

    private static final String SALT = "^cbb2*2c";

    private static final String ALGORITHM_NAME = "md5";

    private static final int HASH_ITERATIONS = 2;

    public static String encrypt(String password) {
        return new SimpleHash(ALGORITHM_NAME, password, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
    }

    public static String encrypt(String username, String password) {
        return new SimpleHash(ALGORITHM_NAME, password, ByteSource.Util.bytes(username + SALT), HASH_ITERATIONS).toHex();
    }

    public static void main(String[] args) {

    }

}
