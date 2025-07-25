import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class voting {
    public static void main(String arg[]) {
        final String DB_URL = "jdbc:mysql://127.0.0.1:3306/votingback";
        final String DB_USER = "root";
        final String DB_PASSWORD = "5202";
        LocalDate currentDate = LocalDate.now();
        String dateString = currentDate.toString();
        JOptionPane.showMessageDialog(null, "Welcome to Voting app");
        String name, nami;
        int aadhar = 0;
        name = JOptionPane.showInputDialog("Enter your name");
        if (doesNameExist(name)) {
            JOptionPane.showMessageDialog(null, "Enter the name of party you want to vote");
            partytable();
            nami = JOptionPane.showInputDialog("Which party you want to vote");
            // adding party to candidate data
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "INSERT INTO candidate(party) VALUES(?)")) {

                preparedStatement.setString(1, nami);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Voted Sucessfully");
                }

            } catch (SQLException e) {
                System.out.println("Error found");

            }
            // fetching aadhar no

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "SELECT adhno FROM candidate WHERE name=?")) {
                preparedStatement.setString(1, name);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    aadhar = rs.getInt("adhno");

                }
            }

            catch (SQLException e) {
                System.out.println("Aadhar not found");
            }
            // adding name and aadhar no in specified party table
            String query = "INSERT INTO " + nami + "(name,aadhar) VALUES (?,?)";
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            query)) {

                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, aadhar);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Voted Sucessfully");
                }

            } catch (SQLException e) {
                System.out.println("Error found");

            }

            // updating party votes in table parties
            query = "UPDATE parties SET votes = votes + 1 WHERE name = ?";

            try {

                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nami);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Vote count updated successfully for party: " + nami);
                } else {
                    System.out.println("Party not found: " + nami);
                }

                // Close connection
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            int passw, age, passql = 0;
            if (name.equalsIgnoreCase("devlopharsh")) {
                int k = 11;
                k = Integer.parseInt(JOptionPane.showInputDialog("Enter Password"));
                if (k == 5202) {

                    JOptionPane.showMessageDialog(null, "Developer login Sucessfully");
                    String problem;
                    problem = JOptionPane.showInputDialog("Enter Insert/Delete");
                    // adding party to table parties
                    if (problem.equalsIgnoreCase("insert")) {
                        name = JOptionPane.showInputDialog("Enter the name of party you want to add");
                        passw = Integer.parseInt(JOptionPane.showInputDialog("Create Password"));
                        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                                PreparedStatement preparedStatement = connection.prepareStatement(
                                        "INSERT INTO parties(name,votes,pass) VALUES(?, ?,?)")) {

                            preparedStatement.setString(1, name);
                            preparedStatement.setInt(2, 0);
                            preparedStatement.setInt(3, passw);

                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, " Party added Sucessfully");
                            } else {
                                System.out.println("nothing entered");
                            }

                        } catch (SQLException e) {
                            System.out.println("Error found");
                        }
                        // code to create party table

                        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + name
                                + "(name VARCHAR(100) UNIQUE, "
                                + "aadhar INT UNIQUE"
                                + ")";

                        try {

                            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(createTableSQL);
                            JOptionPane.showMessageDialog(null, "Table created!");
                            stmt.close();
                            con.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // deleting party record from table parties
                    if (problem.equalsIgnoreCase("delete")) {
                        name = JOptionPane.showInputDialog("Enter the name of party you want to delete");
                        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                                PreparedStatement preparedStatement = connection.prepareStatement(
                                        "DELETE FROM parties WHERE name=?")) {
                            preparedStatement.setString(1, name);

                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, " Party deleted Sucessfully");
                            } else {
                                System.out.println("nothing entered");
                            }

                        } catch (SQLException e) {
                            System.out.println("Error found");
                        }

                        // deleting that party table
                        String dropTableSQL = "DROP TABLE IF EXISTS " + name;

                        try {

                            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(dropTableSQL);
                            String deleteSQL = "DELETE FROM parties WHERE name = ?";
                            PreparedStatement pstmt = con.prepareStatement(deleteSQL);
                            pstmt.setString(1, name);

                            JOptionPane.showMessageDialog(null, "Party" + name + "' has been deleted successfully!");

                            stmt.close();
                            con.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
                tk();
                System.exit(0);

            } else if (name.equalsIgnoreCase("party")) {
                JOptionPane.showMessageDialog(null, "Welcome to Voting app");
                name = JOptionPane.showInputDialog("Enter your registered Party name");
                passw = Integer.parseInt(JOptionPane.showInputDialog("Enter your password"));
                // checking is password correct or not
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "SELECT pass FROM parties WHERE name=?")) {
                    preparedStatement.setString(1, name);
                    ResultSet rs = preparedStatement.executeQuery();

                    if (rs.next()) {
                        passql = rs.getInt("pass");
                        if (passql == passw) {
                            JOptionPane.showMessageDialog(null, "Party found Sucessfully");

                        }
                    }

                } catch (SQLException e) {
                    System.out.println("Party not found");
                }
                int option = JOptionPane.showConfirmDialog(null, "Do you want to see the Detailed Chart", "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Graph of parties
                    {
                        String query = "SELECT name, votes FROM parties";

                        try {
                            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                            PreparedStatement pst = con.prepareStatement(query);
                            ResultSet rs = pst.executeQuery();

                            DefaultPieDataset dataset = new DefaultPieDataset();

                            while (rs.next()) {
                                String namei = rs.getString("name");
                                int votes = rs.getInt("votes");
                                dataset.setValue(namei, votes);
                            }

                            JFreeChart pieChart = ChartFactory.createPieChart(
                                    "Voting Results",
                                    dataset,
                                    true,
                                    true,
                                    false);

                            ChartFrame frame = new ChartFrame("Voting Pie Chart", pieChart);
                            frame.setSize(600, 400);
                            frame.setVisible(true);
                            frame.setLocationRelativeTo(null);

                            rs.close();
                            pst.close();
                            con.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    tk();

                }

                option = JOptionPane.showConfirmDialog(null, "Do you want to see the Detailed Table", "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // showing party table data
                    partytable();

                    tk();
                    System.exit(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "New User Found");
                JOptionPane.showMessageDialog(null, "Enter required details");
                name = JOptionPane.showInputDialog("Enter your name");
                passw = Integer.parseInt(JOptionPane.showInputDialog("Enter your pass"));
                age = Integer.parseInt(JOptionPane.showInputDialog("Enter your age"));
                aadhar = Integer.parseInt(JOptionPane.showInputDialog("Enter your Aadhar no"));
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        PreparedStatement preparedStatement = connection.prepareStatement(
                                "INSERT INTO candidate(name,age,party,regdat,pass,adhno) VALUES(?, ?, ?, ?, ?,?)")) {

                    preparedStatement.setString(1, name);
                    preparedStatement.setInt(2, age);
                    preparedStatement.setString(3, "");
                    preparedStatement.setString(4, dateString);
                    preparedStatement.setInt(5, passw);
                    preparedStatement.setInt(6, aadhar);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "User details added Sucessfully");
                    } else {
                        System.out.println("nothing entered");
                    }

                } catch (SQLException e) {
                    System.out.println("Error found");

                }
                tk();
                System.exit(0);
            }

        }

    }

    public static void tk() {
        JOptionPane.showMessageDialog(null, "Thanks for visiting Voting app");
    }

    public static boolean doesNameExist(String nameToCheck) {
        String url = "jdbc:mysql://localhost:3306/votingback";
        String user = "root";
        String password = "5202";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT 1 FROM candidate WHERE name = ? LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nameToCheck);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // returns true if a row exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // or rethrow/handle as needed
        }
    }

    public static void partytable() {
        final String DB_URL = "jdbc:mysql://127.0.0.1:3306/votingback";
        final String DB_USER = "root";
        final String DB_PASSWORD = "5202";
        {
            JFrame frame = new JFrame("Available Parties");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Party Name");
            tableModel.addColumn("Votes");
            String koi;
            koi = "SELECT NAME,VOTES FROM parties";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(koi)) {

                while (resultSet.next()) {
                    String Name = resultSet.getString("name");
                    int Votes = resultSet.getInt("votes");
                    tableModel.addRow(new Object[] { Name, String.format("%,d", Votes) });
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error found");
                return;
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane);
            frame.setVisible(true);
        }
    }

}
