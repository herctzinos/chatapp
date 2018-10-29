package project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Core_User {

    private static final String DB_URL = "127.0.0.1:3306";
    private static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL + "/chat_db?zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String DB_USER = "herc";
    private static final String DB_PASSWD = "Kalhas!3";
    private String username;
    private char[] password;
    private String role;

    public Core_User(String user, char[] pwd, String role) {
        this.username = user;
        this.password = pwd;
        this.role = role;
    }

    public void create_msg() throws IOException {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;
        String cmsg;
        String receiver;

        try {

            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);

            String sql1 = "INSERT INTO `chat_db`.`user_data` (`sender`,receiver) \n"
                    + "values ((select users.id from users WHERE  user_name=?),"
                    + "(select users.id from users WHERE user_name=?));";
            String sql2 = "INSERT INTO `chat_db`.`messages` "
                    + "(`message_text`) VALUES (?);";

            System.out.println("Please enter the recepient of your message...");
            receiver = System.console().readLine();

            System.out.println("Your message is: ");
            cmsg = System.console().readLine();

            pstm = connection.prepareStatement(sql2);
            pstm2 = connection.prepareStatement(sql1);
            pstm2.setString(1, username);

            pstm2.setString(2, receiver);
            pstm.setString(1, cmsg);
            pstm2.execute();
            pstm.execute();
            System.out.println(System.lineSeparator() + "Message sent!!");

            String str = (System.lineSeparator() + "SUBMITTED ON: " + get_timestamp() + System.lineSeparator()
                    + "SENDER: " + username + System.lineSeparator()
                    + "RECEIVER: " + receiver + System.lineSeparator()
                    + "MESSAGE: " + cmsg + System.lineSeparator());

            File file = new File("C:\\Users\\Herc\\Desktop\\Individual\\src\\" + username + ".txt");     //Log file for each user will be created in this path

            BufferedWriter br = new BufferedWriter(new FileWriter(file, true));

            br.write(str);

            br.close();

        } catch (SQLException ex) {
            System.out.println("There is no such a user!");

        } finally {
            try {
                pstm.close();
                pstm2.close();
                connection.close();

            } catch (SQLException ex) {
            }
        }
    }

    public void inbox() {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;
        PreparedStatement pstm3 = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);

            String sql = "SELECT a1.user_name as sender_name, a2.user_name as receiver_name, u1.message_text,"
                    + " message_number,substring(message_text,1,10) as sub, submission_date, view_status "
                    + "FROM user_data LEFT JOIN users AS a1 ON user_data.sender=a1.id\n"
                    + "LEFT JOIN users AS a2 ON user_data.receiver=a2.id "
                    + "LEFT JOIN messages as u1 on u1.id=user_data.message_number WHERE a2.user_name=?"
                    + " ORDER BY message_number;";

            pstm = connection.prepareStatement(sql);
            pstm.setString(1, username);

            rs = pstm.executeQuery();

            if (rs.first()) {
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("MESSAGE ID: " + rs.getString("message_number"));
                    System.out.println("SUBMITTED ON: " + rs.getString("submission_date"));
                    System.out.println("SENDER: " + username);
                    System.out.println("RECEIVER: " + rs.getString("receiver_name"));
                    System.out.println("MESSAGE PREVIEW: " + rs.getString("sub") + "...");
                    System.out.println("VIEWED: " + rs.getString("view_status") + "\n");

                }
                System.out.println("Enter the id of the message you want to see:" + System.lineSeparator());
                String id_msg = System.console().readLine();

                String sql3 = "UPDATE `chat_db`.`messages` SET `view_status` = 'yes' WHERE (`id` = ?);";

                String sql2 = "SELECT a1.user_name as sender_name, a2.user_name as receiver_name, u1.message_text,"
                        + " message_number,substring(message_text,1,10) as sub, submission_date, view_status"
                        + " FROM user_data LEFT JOIN users AS a1 ON user_data.sender=a1.id\n"
                        + "LEFT JOIN users AS a2 ON user_data.receiver=a2.id "
                        + "LEFT JOIN messages as u1 on u1.id=user_data.message_number"
                        + " WHERE u1.id=? ORDER BY message_number; ";

                pstm3 = connection.prepareStatement(sql3);
                pstm3.setString(1, id_msg);
                pstm3.executeUpdate();

                pstm2 = connection.prepareStatement(sql2);
                pstm2.setString(1, id_msg);
                rs = pstm2.executeQuery();

                if (rs.first()) {
                    rs.beforeFirst();
                    while (rs.next()) {

                        System.out.println(System.lineSeparator() + "MESSAGE ID: " + rs.getString("message_number"));
                        System.out.println("SUBMITTED ON: " + rs.getString("submission_date"));
                        System.out.println("SENDER: " + username);
                        System.out.println("RECEIVER: " + rs.getString("receiver_name"));
                        System.out.println("MESSAGE: " + rs.getString("message_text"));
                        System.out.println("VIEWED: " + rs.getString("view_status") + System.lineSeparator());

                    }
                } else {
                    System.out.println("The id you entered is not valid...");
                }
            } else {
                System.out.println("Your inbox is empty!");
            }

        } catch (SQLException ex) {
            System.out.println("Sorry, problems with the database connection!");
            System.out.println(ex.toString());

        } finally {
            try {
                pstm.close();
                connection.close();
                rs.close();
            } catch (SQLException ex) {
            }
        }
    }

    public void outbox() {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);

            String sql = "SELECT a1.user_name as sender_name, a2.user_name as receiver_name, u1.message_text,"
                    + " message_number,substring(message_text,1,10) as sub, submission_date, view_status "
                    + "FROM user_data LEFT JOIN users AS a1 ON user_data.sender=a1.id\n"
                    + "LEFT JOIN users AS a2 ON user_data.receiver=a2.id "
                    + "LEFT JOIN messages as u1 on u1.id=user_data.message_number WHERE a1.user_name=?"
                    + " ORDER BY message_number;";

            pstm = connection.prepareStatement(sql);
            pstm.setString(1, username);

            rs = pstm.executeQuery();

            if (rs.first()) {
                rs.beforeFirst();
                while (rs.next()) {
                    System.out.println("MESSAGE ID: " + rs.getString("message_number"));
                    System.out.println("SUBMITTED ON: " + rs.getString("submission_date"));
                    System.out.println("SENDER: " + username);
                    System.out.println("RECEIVER: " + rs.getString("receiver_name"));
                    System.out.println("MESSAGE PREVIEW: " + rs.getString("sub") + "...");
                    System.out.println("VIEWED: " + rs.getString("view_status") + "\n");
                }

                System.out.println("Enter the id of the message you want to see:" + System.lineSeparator());
                String id_msg = System.console().readLine();

                String sql2 = "SELECT a1.user_name as sender_name, a2.user_name as receiver_name, u1.message_text,"
                        + " message_number,substring(message_text,1,10) as sub, submission_date, view_status"
                        + " FROM user_data LEFT JOIN users AS a1 ON user_data.sender=a1.id\n"
                        + "LEFT JOIN users AS a2 ON user_data.receiver=a2.id "
                        + "LEFT JOIN messages as u1 on u1.id=user_data.message_number"
                        + " WHERE u1.id=? ORDER BY message_number; ";

                pstm2 = connection.prepareStatement(sql2);
                pstm2.setString(1, id_msg);
                rs = pstm2.executeQuery();

                if (rs.first()) {
                    rs.beforeFirst();
                    while (rs.next()) {

                        System.out.println(System.lineSeparator() + "MESSAGE ID: " + rs.getString("message_number"));
                        System.out.println("SUBMITTED ON: " + rs.getString("submission_date"));
                        System.out.println("SENDER: " + username);
                        System.out.println("RECEIVER: " + rs.getString("receiver_name"));
                        System.out.println("MESSAGE: " + rs.getString("message_text"));
                        System.out.println("VIEWED: " + rs.getString("view_status") + System.lineSeparator());

                    }
                } else {
                    System.out.println("The id you entered is not valid...");
                }
            } else {
                System.out.println("Your outbox is empty!");
            }

        } catch (SQLException ex) {
            System.out.println("Sorry, problems with the database connection!");
            System.out.println(ex.toString());

        } finally {
            try {
                pstm.close();
                connection.close();
                rs.close();
            } catch (SQLException ex) {
            }
        }
    }

    public void update_msg() {
        Connection connection = null;
        PreparedStatement pstm = null;
        String updid;
        String updmsg;

        try {

            System.out.println("Enter the id of the message you want to modify: ");
            updid = System.console().readLine();

            System.out.println("Are you sure you want to modify message->" + updid + "?");

            if (ConsoleUtils.requestConfirmation()) {

                System.out.println("Write your new message: ");
                updmsg = System.console().readLine();

                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                String sql1 = "UPDATE `chat_db`.`messages` SET `message_text` = ? WHERE (`id` = ?);";
                pstm = connection.prepareStatement(sql1);

                pstm.setString(1, updmsg);
                pstm.setString(2, updid);

                int a = pstm.executeUpdate();
                if (a == 0) {
                    System.out.println("The data you entered are not valid...");
                } else {
                    System.out.println("Message modified!");
                }
            }

        } catch (SQLException ex) {
            System.out.println("There is no message with such id...");

        } finally {
            try {
                pstm.close();
                connection.close();

            } catch (SQLException ex) {
            }
        }
    }

    public void delete_msg() {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;
        String delid;

        try {

            System.out.println("Id of the message you want to Delete?");
            delid = System.console().readLine();
            System.out.println("Are you sure you want to delete message->" + delid + "?");

            if (ConsoleUtils.requestConfirmation()) {
                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);

                String sql1 = "DELETE FROM `chat_db`.`messages` WHERE (`id` = ?);";
                String sql2 = "DELETE FROM `chat_db`.`user_data` WHERE (`message_number` = ?);";
                pstm = connection.prepareStatement(sql1);
                pstm.setString(1, delid);
                pstm2 = connection.prepareStatement(sql2);
                pstm2.setString(1, delid);

                int a = pstm.executeUpdate();
                int b = pstm2.executeUpdate();
                if ((a == 0) && (b == 0)) {
                    System.out.println("The id you entered is not valid...");
                } else {
                    System.out.println("Message deleted!");
                }
            }

        } catch (SQLException ex) {
            System.out.println("The data you entered are not valid!");

        } finally {
            try {
                pstm.close();
                connection.close();

            } catch (SQLException ex) {
            }
        }
    }

    public String get_timestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fdt = now.format(formatter);
        return fdt;
    }
}
