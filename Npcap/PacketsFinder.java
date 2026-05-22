package Npcap;

import DAO.TrafficDAO;
import JavaFXUI.PacketScanningView;
import Model.PacketData;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class PacketsFinder {
    private static final Map<String, String> dnsCache = new HashMap<>();
    static PcapHandle handle;
    public static void StartPacketScanning() throws Exception {
        PcapNetworkInterface device = PacketScanningView.device;
        if (device == null) {
            throw new IllegalStateException("Select a network device before starting packet scanning.");
        }
        if (handle != null && handle.isOpen()) {
            System.out.println("Packet scanning is already running.");
            return;
        }
        handle = device.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 1);
        System.out.println("--- Advanced Monitoring Started on: " + device.getDescription() + " ---");
        try {
            handle.loop(-1, (PacketListener) packet -> {
                try {
                    EthernetPacket ethPacket = packet.get(EthernetPacket.class);
                    String srcMac;
                    String DstMac;
                    if ((ethPacket != null)) {
                        srcMac = ethPacket.getHeader().getSrcAddr().toString();
                        DstMac = ethPacket.getHeader().getDstAddr().toString();
                    }else{
                        srcMac = "UNKNOWN_MAC";
                        DstMac = "UNKNOWN_MAC";
                    }
                    IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                    if (ipV4Packet != null) {
                        String srcIp = ipV4Packet.getHeader().getSrcAddr().getHostAddress();
                        String dstIp = ipV4Packet.getHeader().getDstAddr().getHostAddress();
                        String destinationService;
                        if (!dnsCache.containsKey(dstIp)) {
                            try {
                                InetAddress addr = InetAddress.getByName(dstIp);
                                String hostname = addr.getCanonicalHostName().toLowerCase();
                                String service = "";
                                if (hostname.contains("google") || hostname.contains("1e100.net")) {
                                    service = "Google Services";
                                } else if (hostname.contains("youtube") || hostname.contains("googlevideo")) {
                                    service = "YouTube";
                                } else if (hostname.contains("microsoft") || hostname.contains("live.com") || hostname.contains("office")) {
                                    service = "Microsoft / Office 365";
                                } else if (hostname.contains("fbcdn") || hostname.contains("facebook") || hostname.contains("instagram")) {
                                    service = "Meta (FB/Instagram)";
                                } else if (hostname.contains("netflix") || hostname.contains("nflxvideo")) {
                                    service = "Netflix";
                                } else if (hostname.contains("akamai") || hostname.contains("cloudflare") || hostname.contains("fastly")) {
                                    service = "CDN / Infrastructure";
                                } else if (hostname.contains("amazon") || hostname.contains("aws") || hostname.contains("cloudfront")) {
                                    service = "Amazon / AWS";
                                } else if (hostname.contains("github") || hostname.contains("gitlab")) {
                                    service = "Developer Tools (GitHub)";
                                } else if (hostname.contains("uet.edu.pk")) {
                                    service = "UET Lahore Campus Network";
                                } else if (dstIp.startsWith("10.") || dstIp.startsWith("172.16.") || dstIp.startsWith("192.168.")) {
                                    service = "Private Local Network";
                                } else if (dstIp.startsWith("224.") || dstIp.startsWith("239.")) {
                                    service = "Multicast Group";
                                } else {
                                    service = "General Web Traffic";
                                }

                                dnsCache.put(dstIp, service);
                            } catch (Exception e) {
                                dnsCache.put(dstIp, "Unresolved IP");
                            }
                        }
                        destinationService = dnsCache.get(dstIp);
                        int ttl = ipV4Packet.getHeader().getTtlAsInt();
                        String protocol = "OTHER";
                        String securityFlag = "NORMAL";
                        int srcP = 0;
                        int dstP = 0;
                        String flagData = "";

                        if (packet.contains(TcpPacket.class)) {
                            TcpPacket tcp = packet.get(TcpPacket.class);
                            protocol = "TCP";
                            srcP = tcp.getHeader().getSrcPort().valueAsInt();
                            dstP = tcp.getHeader().getDstPort().valueAsInt();

                            boolean isSyn = tcp.getHeader().getSyn();
                            boolean isAck = tcp.getHeader().getAck();
                            flagData = String.format("Flags: [SYN:%b, ACK:%b]", isSyn, isAck);

                            if (isSyn && !isAck) securityFlag = "CONN_REQUEST";
                        } else if (packet.contains(UdpPacket.class)) {
                            UdpPacket udp = packet.get(UdpPacket.class);
                            protocol = "UDP";
                            srcP = udp.getHeader().getSrcPort().valueAsInt();
                            dstP = udp.getHeader().getDstPort().valueAsInt();
                            flagData = "";
                        }
                        PacketData livePacket = new PacketData(securityFlag, protocol, srcIp, srcMac, dstIp, DstMac,destinationService, ttl, srcP, dstP, flagData);
                        TrafficDAO.saveTrafficLive(livePacket);
                    }
                } catch (Exception e) {
                    System.out.println("The Error: " + e);
                }
            });
        } catch (InterruptedException | PcapNativeException | NotOpenException e) {
            System.out.println("Packet scanning stopped: " + e.getMessage());
        } finally {
            if (handle != null && handle.isOpen()) {
                handle.close();
            }
        }
    }
    public static void stopPacketScanning() throws NotOpenException {
        if (handle != null && handle.isOpen()) {
            handle.breakLoop();
            handle.close();
            System.out.println("Packet scanning stopped.");
        }
    }
}
