package DAO;

import Model.Ports;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class PortsDAO {
    public static ArrayList<Ports> ports_fetching() throws SQLException, ClassNotFoundException {
        ArrayList<Ports> ports_list = new ArrayList<>();
        String sql = "SELECT * FROM ports";
        try (Connection con = DBConnection.Connection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int port_number = rs.getInt("port_number");
                String protocol = rs.getString("protocol");
                String risk_level = rs.getString("risk_level");
                String service_name = rs.getString("service_name");
                String description = rs.getString("description");
                String status = rs.getString("port_status");
                Ports ports = new Ports(description, port_number, protocol, risk_level, service_name, status);
                ports_list.add(ports);
            }
        }
        return ports_list;
    }

    public static ArrayList<Integer> port_number() throws SQLException, ClassNotFoundException {
        ArrayList<Integer> ports_num = new ArrayList<>();
        String sql = "SELECT port_number FROM ports";
        try (Connection con = DBConnection.Connection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ports_num.add(rs.getInt("port_number"));
            }
        }
        return ports_num;
    }

    public static void saveScanResult(Ports port) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO ports (port_number, protocol, service_name, description, risk_level, port_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.Connection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, port.getPort_number());
            pstmt.setString(2, port.getProtocols());
            pstmt.setString(3, port.getService_name());
            pstmt.setString(4, port.getDescription());
            pstmt.setString(5, port.getRisk_level());
            pstmt.setString(6, port.getPort_status());
            pstmt.executeUpdate();
        }
    }

    public static void saveScanResults(ArrayList<Ports> ports) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO ports (port_number, protocol, service_name, description, risk_level, port_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.Connection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            for (Ports port : ports) {
                pstmt.setInt(1, port.getPort_number());
                pstmt.setString(2, port.getProtocols());
                pstmt.setString(3, port.getService_name());
                pstmt.setString(4, port.getDescription());
                pstmt.setString(5, port.getRisk_level());
                pstmt.setString(6, port.getPort_status());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public static void clearScanResults() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ports";
        try (Connection con = DBConnection.Connection();
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
