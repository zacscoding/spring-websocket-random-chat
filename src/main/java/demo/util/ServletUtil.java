package demo.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class ServletUtil {

    // =================================
    // HttpServletRequest
    // =================================
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();

        if (ra == null) {
            return null;
        }

        return ((ServletRequestAttributes) ra).getRequest();
    }

    public static String getParameter(String param) {
        HttpServletRequest req = getHttpServletRequest();

        if (req != null) {
            return req.getParameter(param);
        }

        return null;
    }

    public static String getHeader(String name) {
        HttpServletRequest req = getHttpServletRequest();

        if (req != null) {
            return req.getHeader(name);
        }

        return null;
    }

    public static String getIpAddr() {
        return getRealIp(getHttpServletRequest());
    }

    public static String getIpAddr(HttpServletRequest req) {
        return getRealIp(req);
    }

    // =================================
    // HttpServletResponse
    // =================================
    public static HttpServletResponse getHttpServletResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();

        if (ra == null) {
            return null;
        }

        return ((ServletRequestAttributes) ra).getResponse();
    }

    // =================================
    // HttpSession
    // =================================
    public static HttpSession getSession() {
        HttpServletRequest req = getHttpServletRequest();
        if (req != null) {
            return req.getSession();
        }

        return null;
    }

    public static Object getSessionAttribute(String name) {
        HttpSession session = getSession();
        if (session != null) {
            return session.getAttribute(name);
        }

        return null;
    }

    private static String getRealIp(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        // 체크 할 헤더 이름
        final String[] headerNames = new String[] {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

        String ip = null;

        for (String headerName : headerNames) {
            ip = request.getHeader(headerName);
            if (isValidIp(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private static boolean isValidIp(String ip) {
        return ip != null && ip.length() != 0 && !(ip.length() == 7 && "unknown".equalsIgnoreCase(ip));
    }
}