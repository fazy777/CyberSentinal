package Model;

import java.sql.Timestamp;

public class PacketData {
    private int  traffic_id;
    private Timestamp timestamp;
    private String securityFlag;
    private String protocol;
    private String srcIp;
    private String srcMac;
    private String dstIp;
    private String Dstmac;
    private String dstService;
    private int ttl;
    private int srcPort;
    private int dstPort;
    private String tcpFlags;

    public PacketData(String securityFlag, String protocol, String srcIp, String srcMac, String dstIp, String Dstmac,String dstService, int ttl, int srcPort, int dstPort, String tcpFlags) {
        this.securityFlag = securityFlag;
        this.protocol = protocol;
        this.srcIp = srcIp;
        this.srcMac = srcMac;
        this.dstIp = dstIp;
        this.Dstmac = Dstmac;
        this.dstService = dstService;
        this.ttl = ttl;
        this.srcPort = srcPort;
        this.dstPort = dstPort;
        this.tcpFlags = tcpFlags;
    }




    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTraffic_id(int traffic_id) {
        this.traffic_id = traffic_id;
    }

    // Getters (Required for Database Insertion)
    public Timestamp getTimestamp() {return timestamp;}
    public int getTraffic_id() {return traffic_id;}
    public String getSecurityFlag() { return securityFlag; }
    public String getProtocol() { return protocol; }
    public String getSrcIp() { return srcIp; }
    public String getSrcMac() { return srcMac; }
    public String getDstIp() { return dstIp; }
    public String getDstmac() { return Dstmac; }
    public String getDstService() { return dstService; }
    public int getTtl() { return ttl; }
    public int getSrcPort() { return srcPort; }
    public int getDstPort() { return dstPort; }
    public String getTcpFlags() { return tcpFlags; }
}

