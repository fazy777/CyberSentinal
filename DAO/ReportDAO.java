package DAO;

import util.DBConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReportDAO {
    public static int getTotalIncidents() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS Count FROM incidents";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("Count");
            }
        }
        return 0;
    }

    public static int getOpenCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS Count FROM incidents WHERE status = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Open");
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Count");
                }
            }
        }
        return 0;
    }

    public static int getResolvedCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS Count FROM incidents WHERE status = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Resolved");
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Count");
                }
            }
        }
        return 0;
    }

    public static int getClosedCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS Count FROM incidents WHERE status = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Closed");
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Count");
                }
            }
        }
        return 0;
    }

    public static int getCriticalCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS Count FROM incidents WHERE severity = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Critical");
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Count");
                }
            }
        }
        return 0;
    }

    public static int getInProgressCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS Count FROM incidents WHERE status IN (?, ?)";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "In Progress");
            ps.setString(2, "Investigating");
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Count");
                }
            }
        }
        return 0;
    }

    public static int getInvestigatingCount() throws SQLException, ClassNotFoundException {
        return getInProgressCount();
    }

    public static ArrayList<Integer> list_maker() throws SQLException, ClassNotFoundException {
        return new ArrayList<>(Arrays.asList(getTotalIncidents(), getOpenCount(), getResolvedCount(), getClosedCount(), getCriticalCount(), getInProgressCount()));
    }

    public static boolean file_generation() throws SQLException, ClassNotFoundException, IOException {
        try {
            File file = new File("Incident_Report.csv");
            boolean writeHeader = !file.exists() || file.length() == 0;
            try (FileWriter fileWriter = new FileWriter(file, true)) {
                if (writeHeader) {
                    fileWriter.write("Total Incidents,Open Incidents,Resolved Incidents,Closed Incidents,Critical Incidents,In Progress Incidents\n");
                }
                fileWriter.write(getTotalIncidents() + "," + getOpenCount() + "," + getResolvedCount() + "," + getClosedCount() + "," + getCriticalCount() + "," + getInProgressCount() + "\n");
            }
            return true;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
        return false;
    }
}
