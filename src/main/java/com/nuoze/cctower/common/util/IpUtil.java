package com.nuoze.cctower.common.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiashun
 * IP地址相关工具类
 */
@Slf4j
public class IpUtil {

    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_HOST_IP = "127.0.0.1";
    private static final String THT_COMMA = ",";
    private static final Integer MIN_IP_LENGTH = 15;
    private static final Integer ZERO = 0;

    private IpUtil() {}

    public static String client(HttpServletRequest request) {
        String xff = request.getHeader("x-forwarded-for");
        if (xff == null) {
            xff = request.getRemoteAddr();
        }
        return xff;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals(LOCAL_HOST_IP)) {
                    // 根据网卡取本机配置的IP
                    ipAddress = getLocalHostIp();

                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > MIN_IP_LENGTH && ipAddress.indexOf(THT_COMMA) > ZERO) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(THT_COMMA));
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 获取本机IP
     * @return
     */
    public static String getLocalHostIp() {
        InetAddress inet;
        String ipAddress = null;
        try {
            inet = InetAddress.getLocalHost();
            ipAddress = inet.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("get ip address has exception: {}", e.getMessage());
        }
        return ipAddress;
    }
}
