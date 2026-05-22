package DAO;

import Model.PacketData;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;

public class TrafficDAO {

    public static void saveTrafficLive(PacketData data) throws SQLException, ClassNotFoundException {
        String trafficSql = "INSERT INTO network_traffic (security_flag, protocol, source_ip, source_mac, " +
                "dest_ip, dest_mac, dest_service, ttl, src_port, dst_port, tcp_flags) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String cleanFlags = data.getTcpFlags();
        if (cleanFlags != null && cleanFlags.contains("Flags:")) {
            cleanFlags = cleanFlags.substring(cleanFlags.indexOf("Flags:"));
        }

        try (Connection conn = DBConnection.Connection();
            PreparedStatement pstmtTraffic = conn.prepareStatement(trafficSql)) {
            pstmtTraffic.setString(1, data.getSecurityFlag());
            pstmtTraffic.setString(2, data.getProtocol());
            pstmtTraffic.setString(3, data.getSrcIp());
            pstmtTraffic.setString(4, data.getSrcMac());
            pstmtTraffic.setString(5, data.getDstIp());
            pstmtTraffic.setString(6, data.getDstmac());
            pstmtTraffic.setString(7, data.getDstService());
            pstmtTraffic.setInt(8, data.getTtl());
            pstmtTraffic.setInt(9, data.getSrcPort());
            pstmtTraffic.setInt(10, data.getDstPort());
            pstmtTraffic.setString(11, cleanFlags);
            pstmtTraffic.executeUpdate();
        }
    }

    public static ArrayList<PacketData> retrieve_packets() throws SQLException, ClassNotFoundException {
        ArrayList<PacketData> packetDataList = new ArrayList<>();
        String sql = "SELECT * FROM network_traffic";
        try (Connection connection = DBConnection.Connection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                packetDataList.add(mapPacketData(resultSet));
            }
        }
        return packetDataList;
    }

    public static ArrayList<PacketData> search_by_ip(String ip) throws SQLException, ClassNotFoundException {
        ArrayList<PacketData> packetDataList = new ArrayList<>();
        String sql = "SELECT * FROM network_traffic WHERE dest_ip = ?";
        try (Connection connection = DBConnection.Connection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ip);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    packetDataList.add(mapPacketData(resultSet));
                }
            }
        }
        return packetDataList;
    }

    public static void truncate() throws SQLException, ClassNotFoundException {
        String sql = "TRUNCATE TABLE network_traffic";
        try (Connection connection = DBConnection.Connection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Table truncated successfully.");
        }
    }

    private static PacketData mapPacketData(ResultSet resultSet) throws SQLException {
        int traffic_id = resultSet.getInt("traffic_id");
        Timestamp time = resultSet.getTimestamp("timestamp");
        String security_flag = resultSet.getString("security_flag");
        String protocol = resultSet.getString("protocol");
        String source_ip = resultSet.getString("source_ip");
        String source_mac = resultSet.getString("source_mac");
        String dest_ip = resultSet.getString("dest_ip");
        String dst_mac = resultSet.getString("dest_mac");
        String dest_service = resultSet.getString("dest_service");
        int ttl = resultSet.getInt("ttl");
        int src_port = resultSet.getInt("src_port");
        int dst_port = resultSet.getInt("dst_port");
        String tcp_flag = resultSet.getString("tcp_flags");
        PacketData packetData = new PacketData(security_flag, protocol, source_ip, source_mac, dest_ip, dst_mac, dest_service, ttl, src_port, dst_port, tcp_flag);
        packetData.setTraffic_id(traffic_id);
        packetData.setTimestamp(time);
        return packetData;
    }
}
