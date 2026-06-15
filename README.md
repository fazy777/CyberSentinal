# 🛡️ CyberSentinel: Incident & Network Monitoring System

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-437291?style=for-the-badge&logo=javafx&logoColor=white)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Pcap4j](https://img.shields.io/badge/Pcap4j-1.8.2-blue?style=for-the-badge)](https://www.pcap4j.org/)

**CyberSentinel** is a robust, modern, and user-friendly security operations platform designed for real-time network monitoring and incident management. Built with a focus on high-performance packet analysis and streamlined security workflows, it provides security analysts with the tools they need to detect, investigate, and resolve threats efficiently.

---

## ✨ Key Features

### 📡 Real-time Network Monitoring
- **Live Packet Capture:** Monitor incoming and outgoing traffic in real-time using `Pcap4j`.
- **Traffic Analysis:** Detailed breakdown of protocols (TCP, UDP, etc.), source/destination IPs, ports, and MAC addresses.
- **Suspicious Activity Detection:** Identify potentially malicious traffic patterns and security flags.

### 🛡️ Incident Management Lifecycle
- **End-to-End Tracking:** Create, update, and manage security incidents from detection to resolution.
- **Categorization & Severity:** Classify incidents by type (Phishing, Malware, DDoS, etc.) and priority.
- **Advanced Search:** Quickly find incidents using filters like ID, Title, Severity, or Status.
- **Evidence Logging:** Attach network traffic data directly to incidents as forensic evidence.

### 🔍 Network Exploration Tools
- **Port Scanner:** Identify open ports on target systems and evaluate associated risk levels.
- **Reporting & Export:** Generate detailed incident reports and export data to CSV for external analysis.

### 🎨 Modern Glassmorphism UI
- **Futuristic Design:** A beautiful, responsive interface built with JavaFX featuring glassmorphism aesthetics, gradients, and interactive feedback.
- **Interactive Dashboard:** At-a-glance view of security metrics and quick-access action cards.

### 👥 Role-Based Access Control
- **Admin Role:** Full access to all system features including incident management and advanced tools.
- **Analyst Role:** Access to monitoring and investigation tools with restricted administrative capabilities.

---

## 🚀 Tech Stack

- **Language:** Java 17+
- **UI Framework:** JavaFX 21+ with Custom CSS (Glassmorphism)
- **Database:** MySQL 8.0+
- **Network Library:** Pcap4j (wrapped around Npcap/Libpcap)
- **Utilities:** SLF4J (Logging), JNA, Apache POI (for future excel expansions)

---

## 🛠️ Installation & Setup

### Prerequisites
1. **Java JDK 17 or higher**
2. **MySQL Server**
3. **Npcap** (Required for packet capture on Windows): [Download here](https://nmap.org/npcap/)
4. **JavaFX SDK** (if not using a build tool like Maven/Gradle)

### Database Configuration
1. Import the provided SQL script to set up the database schema:
   ```bash
   mysql -u your_username -p < cyber_incident_db.session.sql
   ```
2. Update the database credentials in `src/db.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/cyber_incident_db
   db.username=your_username
   db.password=your_password
   ```

### Running the Application
#### Via IDE (VS Code / IntelliJ)
- Ensure the libraries in the `lib` folder are added to your project's classpath.
- Run `JavaFXUI/MainApp.java` for the Graphical Interface.
- Run `CyberSentinel_Main/Main.java` for the Command Line Interface.

---

## 📂 Project Structure

```text
CyberSentinel/
├── src/
│   ├── DAO/             # Data Access Objects (Database logic)
│   ├── JavaFXUI/        # GUI Views, Controllers, and CSS Styles
│   ├── Model/           # Data Models (Incident, User, PacketData)
│   ├── Npcap/           # Packet Capture and Port Scanning logic
│   ├── util/            # Shared utilities (DB Connection)
│   └── db.properties    # Configuration file
├── lib/                 # External Jar dependencies
└── README.md            # You are here!
```

---

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 📧 Contact

**Faizan Ali** - [GitHub](https://github.com/fazy777)

Project Link: [https://github.com/fazy777/CyberSentinel](https://github.com/fazy777/CyberSentinel)

<p align="center">
  <i>Developed with ❤️ for the Security Community</i>
</p>
