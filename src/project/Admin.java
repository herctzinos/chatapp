package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin extends Moderator {

    private static final String DB_URL = "127.0.0.1:3306";
    private static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL + "/chat_db?zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String DB_USER = "herc";
    private static final String DB_PASSWD = "Kalhas!3";
    private String username;
    private char[] password;
    private String role;

    public Admin(String user, char[] pwd, String role) {
        super(user, pwd, role);
    }

    public void deleteall_msg() {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;

        String delallid;

        try {
            readall_msg();

            System.out.println("Enter the id of the message you want to delete: " + System.lineSeparator());
            delallid = System.console().readLine();

            System.out.println("Are you sure you want to delete message " + delallid + "?" + System.lineSeparator());
            if (ConsoleUtils.requestConfirmation()) {
                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                String sql1 = "DELETE FROM `chat_db`.`messages` WHERE (`id` = ?);";
                String sql2 = "DELETE FROM `chat_db`.`user_data` WHERE (`message_number` = ?);";
                pstm = connection.prepareStatement(sql1);
                pstm.setString(1, delallid);
                pstm2 = connection.prepareStatement(sql2);
                pstm2.setString(1, delallid);

                int a = pstm.executeUpdate();
                int b = pstm2.executeUpdate();

                if ((a == 0) && (b == 0)) {
                    System.out.println("The id you entered is not valid...");

                } else {
                    System.out.println("Message deleted!");
                }
            }

        } catch (SQLException ex) {
            System.out.println("Sorry, problems with the database connection!");
            System.out.println(ex.toString());

        } finally {
            try {
                pstm.close();
                connection.close();

            } catch (SQLException ex) {
            }
        }
    }

}
