package top.huzhurong.test.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author chenshun00@gmail.com
 * @since 2019/2/23
 */
public class JvmUtil {
    public static String jvmName(String javaName) {
        return javaName.replaceAll("\\.", "/");
    }

    public static String createTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createSpanId() {
        return createTraceId();
    }

    public static String getEn0Ip() {
        if (IP != null) return IP;
        InetAddress localAddress0 = getLocalAddress0();
        if (localAddress0 == null) {
            IP = "127.0.0.1";
            return IP;
        }
        IP = localAddress0.getHostAddress();
        return IP;
    }

    private static String IP = null;

    private static final String LOCALHOST = "127.0.0.1";
    private static final String ANYHOST = "0.0.0.0";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    /**
     * copy from dubbo
     */
    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (isValidAddress(address)) {
                            return address;
                        }
                    }
                }
            }
        } catch (Throwable ignore) {

        }
        return localAddress;
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null
                && !ANYHOST.equals(name)
                && !LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }
}
