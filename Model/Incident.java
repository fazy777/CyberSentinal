package Model;

import java.sql.Timestamp;

public class Incident {
    private int incidentId;
    private String  title;
    private String category;
    private String severity;
    private String status;
    private String  sourceIp;
    private String description;
    private String protocol;
    private String destinationService;
    private int  reportedBy;
    private int  srcPort;
    private int  dst_Port;
    private String  dest_ip;
    private String  source_mac;
    private Timestamp createdAt;
    private Timestamp timestamp_captured;

    public int getIncidentId() {return incidentId;}
    public void setIncidentId(int incidentId) {this.incidentId = incidentId;}
    public String getTitle() {return title;}
    public String getCategory() {return category;}
    public String getSeverity() {return severity;}
    public String getStatus() {return status;}
    public String getSourceIp() {return sourceIp;}
    public String getDescription() {return description;}
    public int getReportedBy() {return reportedBy;}
    public Timestamp getCreatedAt() {return createdAt;}
    public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt;}
    public void setTimestamp_captured(Timestamp timestamp_captured) {this.timestamp_captured = timestamp_captured;}
    public Timestamp getTimestamp_captured() {return timestamp_captured;}
    public String getDestinationService() {return destinationService;}
    public String getProtocol() {return protocol;}
    public int getDst_Port() {return dst_Port;}
    public int getSrcPort() {return srcPort;}
    public String getDest_ip() {return dest_ip;}
    public String getSource_mac() {return source_mac;}


    public Incident(String category, String description, String dest_ip, String destinationService, int dst_Port, String protocol, int reportedBy, String severity, String source_mac, String sourceIp, int srcPort, String status, String title) {
        this.category = category;
        this.description = description;
        this.dest_ip = dest_ip;
        this.destinationService = destinationService;
        this.dst_Port = dst_Port;
        this.protocol = protocol;
        this.reportedBy = reportedBy;
        this.severity = severity;
        this.source_mac = source_mac;
        this.sourceIp = sourceIp;
        this.srcPort = srcPort;
        this.status = status;
        this.title = title;
    }
}
