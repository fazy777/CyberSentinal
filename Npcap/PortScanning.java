package Npcap;

import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class PortScanning {
    private static volatile boolean scanning = false;
    private static PcapNetworkInterface selectedDevice;
    private static InetAddress selectedLocalAddress;
    private static Consumer<ScanResult> resultConsumer;
    private static Consumer<Integer> progressConsumer;

    public static class ScanResult {
        public int port;
        public String status;
        public String service;
        public String protocol;

        public ScanResult(int port, String status, String service, String protocol) {
            this.port = port;
            this.status = status;
            this.service = service;
            this.protocol = protocol;
        }
    }

    private static final Map<Integer, String> port_names = new HashMap<>();
    static {
        port_names.put(21, "FTP");
        port_names.put(22, "SSH");
        port_names.put(23, "Telnet");
        port_names.put(25, "SMTP");
        port_names.put(53, "DNS");
        port_names.put(80, "HTTP");
        port_names.put(110, "POP3");
        port_names.put(143, "IMAP");
        port_names.put(443, "HTTPS");
        port_names.put(445, "SMB");
        port_names.put(993, "IMAPS");
        port_names.put(995, "POP3S");
        port_names.put(1433, "MSSQL");
        port_names.put(1521, "Oracle");
        port_names.put(3306, "MySQL");
        port_names.put(3389, "RDP");
        port_names.put(5432, "PostgreSQL");
        port_names.put(5900, "VNC");
        port_names.put(6379, "Redis");
        port_names.put(8080, "HTTP-Alt");
        port_names.put(8443, "HTTPS-Alt");
        port_names.put(27017, "MongoDB");
    }

    public static void setResultConsumer(Consumer<ScanResult> consumer) {
        resultConsumer = consumer;
    }

    public static void setProgressConsumer(Consumer<Integer> consumer) {
        progressConsumer = consumer;
    }

    public static void setSelectedDevice(PcapNetworkInterface device) {
        selectedDevice = device;
        selectedLocalAddress = findDeviceIpv4(device);
    }

    // Kept for older callers; this scanner now uses the OS TCP stack instead of raw Npcap packets.
    public static boolean initNpcap() {
        return true;
    }

    public static void scanPorts(String targetIp, int startPort, int endPort, int timeout) {
        if (scanning) {
            return;
        }
        scanning = true;

        new Thread(() -> {
            try {
                int totalPorts = endPort - startPort + 1;
                int threadCount = Math.min(64, totalPorts);
                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                CompletionService<ScanResult> completionService = new ExecutorCompletionService<>(executor);
                int submitted = 0;

                try {
                    for (int port = startPort; port <= endPort && scanning; port++) {
                        final int currentPort = port;
                        completionService.submit(() -> scanWithSocket(targetIp, currentPort, timeout));
                        submitted++;
                    }

                    int scanned = 0;
                    while (scanned < submitted && scanning) {
                        Future<ScanResult> future = completionService.poll(100, TimeUnit.MILLISECONDS);
                        if (future == null) {
                            continue;
                        }

                        ScanResult result = future.get();
                        if (resultConsumer != null) {
                            resultConsumer.accept(result);
                        }
                        scanned++;
                        if (progressConsumer != null) {
                            progressConsumer.accept((int) ((scanned * 100.0) / totalPorts));
                        }
                    }
                } finally {
                    executor.shutdownNow();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                scanning = false;
            }
        }).start();
    }

    private static ScanResult scanWithSocket(String targetIp, int port, int timeout) {
        String service = port_names.getOrDefault(port, "Port To Be Classified");

        try (Socket socket = new Socket()) {
            if (selectedLocalAddress != null) {
                socket.bind(new InetSocketAddress(selectedLocalAddress, 0));
            }
            socket.connect(new InetSocketAddress(targetIp, port), timeout);
            return new ScanResult(port, "OPEN", service, "TCP");
        } catch (SocketTimeoutException e) {
            return new ScanResult(port, "FILTERED", service, "TCP");
        } catch (ConnectException e) {
            return new ScanResult(port, "CLOSED", service, "TCP");
        } catch (NoRouteToHostException e) {
            return new ScanResult(port, "UNREACHABLE", service, "TCP");
        } catch (UnknownHostException e) {
            return new ScanResult(port, "INVALID HOST", service, "TCP");
        } catch (SocketException e) {
            String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
            if (message.contains("unreachable") || message.contains("cannot assign requested address")) {
                return new ScanResult(port, "UNREACHABLE", service, "TCP");
            }
            return new ScanResult(port, "CLOSED", service, "TCP");
        } catch (IOException e) {
            return new ScanResult(port, "CLOSED", service, "TCP");
        }
    }

    private static InetAddress findDeviceIpv4(PcapNetworkInterface device) {
        if (device == null) {
            return null;
        }

        for (PcapAddress address : device.getAddresses()) {
            InetAddress inetAddress = address.getAddress();
            if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                return inetAddress;
            }
        }

        return null;
    }

    public static void quickScan(String targetIp, Consumer<ScanResult> callback) {
        setResultConsumer(callback);
        scanPorts(targetIp, 1, 1024, 1000);
    }

    public static void stopScanning() {
        scanning = false;
    }

    public static boolean isScanning() {
        return scanning;
    }

    public static String getDeviceName() {
        if (selectedDevice == null) {
            return "OS default route";
        }
        return selectedDevice.getDescription() != null ? selectedDevice.getDescription() : selectedDevice.getName();
    }
}
