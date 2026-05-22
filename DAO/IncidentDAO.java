package DAO;

import Model.Incident;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class IncidentDAO {
    public static Incident addIncident(String title, String category, String severity, String status, String sourceIp, String description, int logged_in_id, String protocol, String destinationService, int srcPort, int dst_Port, String dest_ip, String source_mac) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO incidents (title, category, severity, status, source_ip, description, reported_by, timestamp_captured, protocol, source_mac, dest_ip, dest_service, src_port, dst_port) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, category);
            ps.setString(3, severity);
            ps.setString(4, status);
            ps.setString(5, sourceIp);
            ps.setString(6, description);
            ps.setInt(7, logged_in_id);
            ps.setString(8, protocol);
            ps.setString(9, source_mac);
            ps.setString(10, dest_ip);
            ps.setString(11, destinationService);
            ps.setInt(12, srcPort);
            ps.setInt(13, dst_Port);
            if (ps.executeUpdate() > 0) {
                return new Incident(category, description, dest_ip, destinationService, dst_Port, protocol, logged_in_id, severity, source_mac, sourceIp, srcPort, status, title);
            }
        }
        System.out.println("Failed to add incident.");
        return null;
    }

    public static ArrayList<Incident> getAllIncidents() throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents";
        try (Connection connection = DBConnection.Connection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                incidents.add(mapIncident(resultSet));
            }
        }
        return incidents;
    }

    public static boolean updateIncident(int ID, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE incidents SET status = ? WHERE incident_id = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, ID);
            if (ps.executeUpdate() > 0) {
                System.out.println("Incident updated successfully.");
                return true;
            }
        }
        System.out.println("Failed to update incident.");
        return false;
    }

    public static ArrayList<Incident> searchById(int id) throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE incident_id = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Incident inc = mapIncident(resultSet);
                    inc.setIncidentId(id);
                    incidents.add(inc);
                }
            }
        }
        return incidents;
    }

    public static ArrayList<Incident> searchByTitle(String title) throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE title = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, title);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Incident inc = mapIncident(resultSet);
                    inc.setIncidentId(resultSet.getInt("incident_id"));
                    incidents.add(inc);
                }
            }
        }
        return incidents;
    }

    public static ArrayList<Incident> searchByCategory(String category) throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE category = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    incidents.add(mapIncident(resultSet));
                }
            }
        }
        return incidents;
    }

    public static ArrayList<Incident> searchBySeverity(String severity) throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE severity = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, severity);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    incidents.add(mapIncident(resultSet));
                }
            }
        }
        return incidents;
    }

    public static ArrayList<Incident> searchByStatus(String status) throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE status = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    incidents.add(mapIncident(resultSet));
                }
            }
        }
        return incidents;
    }

    public static ArrayList<Incident> searchBySourceIp(String sourceIp) throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE source_ip = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sourceIp);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    incidents.add(mapIncident(resultSet));
                }
            }
        }
        return incidents;
    }

    public static ArrayList<Incident> searchByReported_BY(int reportedBy) throws SQLException, ClassNotFoundException {
        ArrayList<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE reported_by = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reportedBy);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    incidents.add(mapIncident(resultSet));
                }
            }
        }
        return incidents;
    }

    private static Incident mapIncident(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("incident_id");
        String title = resultSet.getString("title");
        String category = resultSet.getString("category");
        String severity = resultSet.getString("severity");
        String status = resultSet.getString("status");
        String sourceIp = resultSet.getString("source_ip");
        String description = resultSet.getString("description");
        int logged_in_id = resultSet.getInt("reported_by");
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        Timestamp timestamp = resultSet.getTimestamp("timestamp_captured");
        String protocol = resultSet.getString("protocol");
        String destinationService = resultSet.getString("dest_service");
        int srcPort = resultSet.getInt("src_port");
        int dst_Port = resultSet.getInt("dst_port");
        String dest_ip = resultSet.getString("dest_ip");
        String source_mac = resultSet.getString("source_mac");
        Incident incident = new Incident(category, description, dest_ip, destinationService, dst_Port, protocol, logged_in_id, severity, source_mac, sourceIp, srcPort, status, title);
        incident.setTimestamp_captured(timestamp);
        incident.setIncidentId(id);
        incident.setCreatedAt(createdAt);
        return incident;
    }
}
