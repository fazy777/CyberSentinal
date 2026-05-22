package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Ports {
    private final IntegerProperty port_number;
    private final SimpleStringProperty protocol;
    private final SimpleStringProperty service_name;
    private final SimpleStringProperty description;
    private final SimpleStringProperty risk_level;
    private final SimpleStringProperty port_status;
    private final SimpleStringProperty target_ip;
    private final SimpleStringProperty scan_time;

    public Ports(String description, int port_number, String protocol, String risk_level, String service_name, String port_status) {
        this.port_number = new SimpleIntegerProperty(port_number);
        this.protocol = new SimpleStringProperty(protocol);
        this.service_name = new SimpleStringProperty(service_name);
        this.description = new SimpleStringProperty(description);
        this.risk_level = new SimpleStringProperty(risk_level);
        this.port_status = new SimpleStringProperty(port_status);
        this.target_ip = new SimpleStringProperty("");
        this.scan_time = new SimpleStringProperty("");
    }

    public Ports(int port_number, String protocol, String service_name, String status, String targetIp) {
        this.port_number = new SimpleIntegerProperty(port_number);
        this.protocol = new SimpleStringProperty(protocol);
        this.service_name = new SimpleStringProperty(service_name);
        this.description = new SimpleStringProperty("");
        this.risk_level = new SimpleStringProperty(calculateRisk(port_number, status));
        this.port_status = new SimpleStringProperty(status);
        this.target_ip = new SimpleStringProperty(targetIp);
        this.scan_time = new SimpleStringProperty(new java.util.Date().toString());
    }

    private String calculateRisk(int port, String status) {
        if (!status.equals("OPEN")) return "None";
        int[] highRiskPorts = {21, 23, 445, 3389, 5900, 1433, 3306, 5432, 27017};
        int[] mediumRiskPorts = {22, 25, 110, 143, 993, 995, 8080};
        for (int p : highRiskPorts) {
            if (port == p) return "High";
        }
        for (int p : mediumRiskPorts) {
            if (port == p) return "Medium";
        }
        return "Low";
    }

    public String getDescription() { return description.get(); }
    public int getPort_number() { return port_number.get(); }
    public String getProtocols() { return protocol.get(); }
    public String getRisk_level() { return risk_level.get(); }
    public String getService_name() { return service_name.get(); }
    public String getPort_status() { return port_status.get(); }
    public String getTarget_ip() { return target_ip.get(); }
    public String getScan_time() { return scan_time.get(); }

    public IntegerProperty portNumberProperty() { return port_number; }
    public SimpleStringProperty protocolProperty() { return protocol; }
    public SimpleStringProperty serviceProperty() { return service_name; }
    public SimpleStringProperty statusProperty() { return port_status; }
    public SimpleStringProperty riskProperty() { return risk_level; }

    @Override
    public String toString() {
        return "Ports{" +
                "port_number=" + port_number.get() +
                ", protocol='" + protocol.get() + '\'' +
                ", service_name='" + service_name.get() + '\'' +
                ", status='" + port_status.get() + '\'' +
                ", risk_level='" + risk_level.get() + '\'' +
                '}';
    }
}