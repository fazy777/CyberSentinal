package CyberSentinel_Main;
import DAO.TrafficDAO;
import Model.Incident;
import Model.PacketData;
import Model.User;
import DAO.IncidentDAO;
import DAO.ReportDAO;
import DAO.UserDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static User object;

    public static void main(String [] args) throws Exception {
        System.out.println("Welcome to CyberShield! Please log in.");
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();
        object = UserDAO.login(username, password);
        if (object != null) {
            System.out.println("Welcome " + username);
            System.out.println("Your Role is: " + object.getRole());
            incidents();

            System.out.println("Thank you for using Packet Scanner Tools!");
        }
        sc.close();
    }

    static void incidents() throws Exception {
        System.out.println("1. Add Incident");
        System.out.println("2. View Incidents");
        System.out.println("3. Update Incidents");
        System.out.println("4. Search Incidents");
        System.out.println("5. Report Incidents");
        System.out.println("6. Packets Scanner Tool");
        System.out.print("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        int logged_in_id = object.getUserId();
        switch (choice) {
            case 1:
                Add_incidents(sc,logged_in_id);
                break;
            case 2:
                View_Incidents();
                break;
            case 3:
                Update_Incident(sc);
                break;
            case 4:
                Search_Incident(sc);
                break;
            case 5:
                Report_Incident(sc);
                break;
            case 6:
                Packet_Scanning(sc,logged_in_id);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                incidents();
                break;
        }
    }
    public static void Add_incidents(Scanner sc, int logged_in_id){
        try {
            System.out.println("Add Incident");
            System.out.print("Title: ");
            String title = sc.next();
            System.out.print("Category>>> ");
            String catagory = "";
            System.out.println("Select Category:");
            System.out.println("1. Phishing");
            System.out.println("2. Malware");
            System.out.println("3. DDoS");
            System.out.println("4. Ransomware");
            System.out.println("5. Suspicious Traffic");
            System.out.println("6. Data Breach");
            System.out.println("7. Insider Threat");
            System.out.println("8. Policy Violation");
            System.out.print("What Is Actually The Category!!: ");
            switch (sc.next()) {
                case "1":
                    catagory = "Phishing";
                    break;
                case "2":
                    catagory = "Malware";
                    break;
                case "3":
                    catagory = "DDoS";
                    break;
                case "4":
                    catagory = "Ransomware";
                    break;
                case "5":
                    catagory = "Suspicious Traffic";
                    break;
                case "6":
                    catagory = "Data Breach";
                    break;
                case "7":
                    catagory = "Insider Threat";
                    break;
                case "8":
                    catagory = "Policy Violation";
                    break;
                default:
                    System.out.println("Invalid category choice. Please try again.");
                    incidents();
                    break;
                }
            System.out.print("Severity>>> ");
            String severity = "";
                System.out.println("Select Severity:");
                System.out.println("1. High");
                System.out.println("2. Medium");
                System.out.println("3. Low");
                System.out.println("4. Critical");
                System.out.print("What Is Actually The Severity!!: ");
                switch (sc.next()) {
                    case "1":
                        severity = "High";
                        break;
                    case "2":
                        severity = "Medium";
                        break;
                    case "3":
                        severity = "Low";
                        break;
                    case "4":
                        severity = "Critical";
                        break;
                    default:
                        System.out.println("Invalid severity choice. Please try again.");
                        incidents();
                        break;
                }
            System.out.print("Source IP: ");
            String sourceIp = sc.next();
            System.out.print("Description: ");
            String description = sc.next();
            System.out.print("Protocol: ");
            String protocol = sc.next();
            System.out.print("destinationService: ");
            String destinationService = sc.next();
            System.out.print("srcPort: ");
            int srcPort = sc.nextInt();
            System.out.print("dst_Port: ");
            int dst_Port = sc.nextInt();
            System.out.print("dest_ip: ");
            String dest_ip = sc.next();
            System.out.print("source_mac: ");
            String source_mac = sc.next();
            String status = "Open";
            Incident object = IncidentDAO.addIncident(title, catagory, severity, status, sourceIp, description, logged_in_id, protocol, destinationService, srcPort, dst_Port, dest_ip, source_mac);
            if (object != null) {
                System.out.println("Incident added successfully!");
            }
        } catch (Exception e) {
            System.out.println("Failed to add incident: " + e.getMessage());
        }
    }
    public static void View_Incidents() throws SQLException, ClassNotFoundException {
            ArrayList<Incident> incidents = IncidentDAO.getAllIncidents();
            try {
                draw(incidents);
            } catch (Exception e) {
                System.out.println("Failed to view incidents: " + e.getMessage());
            }
    }
    public static void Update_Incident(Scanner sc) throws SQLException, ClassNotFoundException {
        try {
                String status;
                System.out.print("Update Incident Status For ID: ");
                int ID = sc.nextInt();
                System.out.println("1. Update Status To Investigating");
                System.out.println("2. Update Status To Resolved");
                System.out.println("3. Update Status To Rejected");
                System.out.println("4. Update Status To Closed");
                System.out.print("Select Update Option: ");
                switch (sc.next()) {
                    case "1":
                        status = "Investigating";
                        break;
                    case "2":
                        status = "Resolved";
                        break;
                    case "3":
                        status = "Rejected";
                        break;
                    case "4":
                        status = "Closed";
                        break;
                    default:
                        status = "Open";
                        break;
                }
                boolean updated = IncidentDAO.updateIncident(ID, status);
                if (updated) {
                    System.out.println("Incident updated successfully!");
                }
        } catch (Exception e) {
            System.out.println("Failed to update incident: " + e.getMessage());
        }
    }
    public static void Search_Incident(Scanner sc) {
        System.out.println("1. Search By ID");
        System.out.println("2. Search By Title");
        System.out.println("3. Search By Category");
        System.out.println("4. Search By Severity");
        System.out.println("5. Search By Status");
        System.out.println("6. Search By Source IP");
        System.out.println("7. Search By Reported_BY");
        System.out.print("Select Search Option: ");
        switch (sc.next()) {
            case "1":
                try {

                    System.out.print("Enter ID To Search: ");
                    int ID = sc.nextInt();
                    ArrayList<Incident> IDIncidents = IncidentDAO.searchById(ID);
                    draw(IDIncidents);
                } catch (Exception e) {
                    System.out.println("Failed to search incident by ID: " + e.getMessage());
                }
                break;
            case "2":
                try {
                    System.out.print("Enter Title To Search: ");
                    String title = sc.next();
                    ArrayList<Incident> titleIncidents = IncidentDAO.searchByTitle(title);
                    draw(titleIncidents);
                } catch (Exception e) {
                    System.out.println("Failed to search incident by Title: " + e.getMessage());
                }
                break;
            case "3":
                try {
                    System.out.print("Enter Category To Search: ");
                    String category = sc.next();
                    ArrayList<Incident> categoryIncidents = IncidentDAO.searchByCategory(category);
                    draw(categoryIncidents);
                } catch (Exception e) {
                    System.out.println("Failed to search incident by Category: " + e.getMessage());
                }
                break;
            case "4":
                try {
                    System.out.print("Enter Severity To Search: ");
                    String Severity = sc.next();
                    ArrayList<Incident> SeverityIncidents = IncidentDAO.searchBySeverity(Severity);
                    draw(SeverityIncidents);
                } catch (Exception e) {
                    System.out.println("Failed to search incident by Severity: " + e.getMessage());
                }
                break;
            case "5":
                try {
                    System.out.print("Enter Status To Search: ");
                    String Status = sc.next();
                    ArrayList<Incident> StatusIncidents = IncidentDAO.searchByStatus(Status);
                    draw(StatusIncidents);
                } catch (Exception e) {
                    System.out.println("Failed to search incident by Status: " + e.getMessage());
                }
                break;
            case "6":
                try {
                    System.out.print("Enter Source IP To Search: ");
                    String sourceIp = sc.next();
                    ArrayList<Incident> sourceIpIncidents = IncidentDAO.searchBySourceIp(sourceIp);
                    draw(sourceIpIncidents);
                } catch (Exception e) {
                    System.out.println("Failed to search incident by Source IP: " + e.getMessage());
                }
                break;
            case "7":
                try {
                    System.out.print("Enter Reported_BY To Search: ");
                    int Reported_BY = sc.nextInt();
                    ArrayList<Incident> Reported_BYIncidents = IncidentDAO.searchByReported_BY(Reported_BY);
                    draw(Reported_BYIncidents);
                } catch (Exception e) {
                    System.out.println("Failed to search incident by Reported_BY: " + e.getMessage());
                }
                break;
        }
    }
    public static void Report_Incident(Scanner sc){
        System.out.println("1. Console Summary");
        System.out.println("2. Export Incident To Excel (CSV) ");
        switch (sc.nextInt()) {
            case 1:
                try {
                    int TotalIncidents = ReportDAO.getTotalIncidents();
                    int OpenCount = ReportDAO.getOpenCount();
                    int ResolvedCount = ReportDAO.getResolvedCount();
                    int ClosedCount = ReportDAO.getClosedCount();
                    int CriticalCount = ReportDAO.getCriticalCount();
                    int InvestigatingCount = ReportDAO.getInvestigatingCount();
                    System.out.println("===== INCIDENT REPORT =====");
                    System.out.println("Total Incidents       :" + TotalIncidents);
                    System.out.println("Open Incidents        :" + OpenCount);
                    System.out.println("Investigating         :" + InvestigatingCount);
                    System.out.println("Resolved Incidents    :" + ResolvedCount);
                    System.out.println("Closed Incidents      :" + ClosedCount);
                    System.out.println("Critical Incidents    :" + CriticalCount);
                    System.out.println("===========================");
                } catch (Exception e) {
                    System.out.println("Failed to generate report: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.println("Report Generation By File Handling!!");
                    boolean report_generation = ReportDAO.file_generation();
                    if (report_generation) {
                        System.out.println("Report generated successfully and saved as Incident_Report.csv");
                    }
                }catch (IOException | SQLException | ClassNotFoundException e){
                    System.out.println("Failed to generate report: " + e.getMessage());
                }
                break;
        }
    }
    public static void Packet_Scanning(Scanner sc,int logged_in_id) throws Exception {
                System.out.print("3. Report An IP\n4.Logout \n What Your Choice: ");
                switch(sc.nextInt()) {
                    case 3:
                        System.out.print("Which IP Is Looking Suspicious?: ");
                        String ip = sc.next();
                        ArrayList<PacketData> packetdata = TrafficDAO.search_by_ip(ip);
                        String title = "";
                        String protocol = "";
                        int srcPort = 0;
                        String dest_ip = "";
                        int dst_Port = 0;
                        String source_mac = "";
                        for (PacketData packetData : packetdata) {
                            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                            System.out.println("Traffic ID: " + packetData.getTraffic_id() + "    |  timestamp: " + packetData.getTimestamp() + "    |  securityFlag: " + packetData.getSecurityFlag() + "    |  protocol: " + packetData.getProtocol() + "    |  srcIp: " + packetData.getSrcIp() + "    |  srcMac: " + packetData.getSrcMac() + "    |  dstIp: " + packetData.getDstIp() + "    |  dstService: " + packetData.getDstService() + "    |  ttl: " + packetData.getTtl() + "    |  srcPort: " + packetData.getSrcPort() + "    |    dstPort: " + packetData.getDstPort() + "    |  tcpFlags: " + packetData.getTcpFlags());
                            System.out.print("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                            title = packetData.getDstService();
                            protocol = packetData.getProtocol();
                            srcPort = packetData.getSrcPort();
                            dest_ip = packetData.getDstIp();
                            dst_Port = packetData.getDstPort();
                            source_mac = packetData.getSrcMac();
                        }
                        System.out.println();
                        System.out.print("Do You Think This IP Is Suspicious??: ");
                        switch (sc.next().toLowerCase()) {
                            case "yes":
                                System.out.println(">>>>>>>>>>>>Let's Add An Incident For This IP!!<<<<<<<<<<<<<<<");
                                String catagory = "";
                                try {
                                    System.out.println("1. Phishing");
                                    System.out.println("2. Malware");
                                    System.out.println("3. DDoS");
                                    System.out.println("4. Ransomware");
                                    System.out.println("5. Suspicious Traffic");
                                    System.out.println("6. Data Breach");
                                    System.out.println("7. Insider Threat");
                                    System.out.println("8. Policy Violation");
                                    System.out.print("**Select Category:");
                                    sc.nextLine();
                                    String categoryChoice = sc.next();
                                    switch (categoryChoice) {
                                        case "1":
                                            catagory = "Phishing";
                                            break;
                                        case "2":
                                            catagory = "Malware";
                                            break;
                                        case "3":
                                            catagory = "DDoS";
                                            break;
                                        case "4":
                                            catagory = "Ransomware";
                                            break;
                                        case "5":
                                            catagory = "Suspicious Traffic";
                                            break;
                                        case "6":
                                            catagory = "Data Breach";
                                            break;
                                        case "7":
                                            catagory = "Insider Threat";
                                            break;
                                        case "8":
                                            catagory = "Policy Violation";
                                            break;
                                        default:
                                            System.out.println("Invalid category choice. Please try again.");
                                            break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Failed to add incident: " + e.getMessage());
                                }
                                System.out.print("Enter Description: ");
                                String description = sc.next();
                                String severity = "";
                                System.out.println("Select Severity:");
                                System.out.println("1. High");
                                System.out.println("2. Medium");
                                System.out.println("3. Low");
                                System.out.println("4. Critical");
                                System.out.print("What Is Actually The Severity!!: ");
                                sc.nextLine();
                                String SeverityChoice = sc.next();
                                switch (SeverityChoice) {
                                    case "1":
                                        severity = "High";
                                        break;
                                    case "2":
                                        severity = "Medium";
                                        break;
                                    case "3":
                                        severity = "Low";
                                        break;
                                    case "4":
                                        severity = "Critical";
                                        break;
                                    default:
                                        System.out.println("Invalid severity choice. Please try again.");
                                        break;
                                }
                                String status = "Open";
                                Incident object = IncidentDAO.addIncident(title, catagory, severity, status, ip, description, logged_in_id, protocol, title, srcPort, dst_Port, dest_ip, source_mac);
                                if (object != null) {
                                    System.out.println("Incident added successfully!");
                                    System.out.println("====================================================");
                                    System.out.println("Welcome to the Incident Report!");
                                    System.out.println("====================================================");
                                    incidents();
                                }
                                break;
                            case "no" :
                                Packet_Scanning(sc,logged_in_id);
                                break;
                        }

                        break;
                    case 4:
                        System.out.println("Logging out...");
                        System.out.println("Before Logging Out Want To Clear The Memory!!!!");
                        System.out.print("1. Yes\n2. No\n What Your Choice: ");
                        switch (sc.next().toLowerCase()) {
                            case "yes" , "1":
                                TrafficDAO.truncate();
                                System.out.println("Memory Cleared Successfully!!");
                                break;
                            case "no" , "2":
                                System.out.println("Memory Not Cleared!!");
                                break;
                        }
                }return;
    }
    public static void draw(ArrayList<Incident> incidents) {
        for (Incident incident : incidents) {
            System.out.println("=".repeat(200));
            System.out.printf("INCIDENT ID: %-5d | TITLE: %-25s | CATEGORY: %-15s | SEVERITY: %-10s | STATUS: %-10s%n", incident.getIncidentId(), incident.getTitle(), incident.getCategory(), incident.getSeverity(), incident.getStatus());
            System.out.println("-".repeat(200));
            System.out.println("NETWORK EVIDENCE:");
            System.out.printf("  Timestamp: %-20s | Protocol: %-5s | Source MAC: %-18s | Destination Service: %s%n", incident.getTimestamp_captured(), incident.getProtocol(), incident.getSource_mac(), incident.getDestinationService());
            System.out.printf("  Source IP: %-20s | Dest IP: %-20s | Src Port: %-6d | Dst Port: %-6d%n", incident.getSourceIp(), incident.getDest_ip(), incident.getSrcPort(), incident.getDst_Port());
            System.out.println("-".repeat(200));
            System.out.println("DESCRIPTION: " + incident.getDescription());
            System.out.printf("REPORTED BY: %-10d | RECORD CREATED: %s%n", incident.getReportedBy(), incident.getCreatedAt());
            System.out.println("=".repeat(200));
        }
    }
}
