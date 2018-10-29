package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SUser extends Core_User {

    private static final String DB_URL = "127.0.0.1:3306";
    private static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL + "/chat_db?zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String DB_USER = "herc";
    private static final String DB_PASSWD = "Kalhas!3";
    private String username;
    private char[] password;
    private String role;

    public SUser(String user, char[] pwd, String role) {
        super(user, pwd, role);
    }

    public void readall_msg() {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;

        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            String sql = "SELECT a1.user_name as sender_name, a2.user_name as receiver_name, u1.message_text,"
                    + " message_number,substring(message_text,1,10) as sub, submission_date, view_status"
                    + " FROM user_data LEFT JOIN users AS a1 ON user_data.sender=a1.id "
                    + "LEFT JOIN users AS a2 ON user_data.receiver=a2.id"
                    + " LEFT JOIN messages as u1 on u1.id=user_data.message_number ORDER BY message_number;";
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                System.out.println("MESSAGE ID: " + rs.getString("message_number"));
                System.out.println("RECEIVER: " + rs.getString("receiver_name"));
                System.out.println("MESSAGE PREVIEW: " + rs.getString("sub"));
                System.out.println("SUBMITTED ON: " + rs.getString("submission_date"));
                System.out.println("VIEWED: " + rs.getString("view_status") + System.lineSeparator());
            }

            System.out.println("Do you want to see a specific message?");
            if (ConsoleUtils.requestConfirmation()) {
                System.out.println("Enter the id of the message you want to see: " + System.lineSeparator());
                String id_msg = System.console().readLine();

                String sql2 = "SELECT a1.user_name as sender_name, a2.user_name as receiver_name, u1.message_text,"
                        + " message_number,substring(message_text,1,10) as sub, submission_date, view_status"
                        + " FROM user_data LEFT JOIN users AS a1 ON user_data.sender=a1.id\n"
                        + "LEFT JOIN users AS a2 ON user_data.receiver=a2.id "
                        + "LEFT JOIN messages as u1 on u1.id=user_data.message_number "
                        + "WHERE message_number =? ORDER BY message_number; ";

                pstm2 = connection.prepareStatement(sql2);
                pstm2.setString(1, id_msg);

                rs = pstm2.executeQuery();

                if (rs.next()) {
                    rs.beforeFirst();
                    while (rs.next()) {

                        System.out.println(System.lineSeparator() + "MESSAGE ID: " + rs.getString("message_number"));
                        System.out.println("SENDER: " + rs.getString("sender_name"));
                        System.out.println("RECEIVER: " + rs.getString("receiver_name"));
                        System.out.println("MESSAGE: " + rs.getString("message_text"));
                        System.out.println("SUBMITTED ON: " + rs.getString("submission_date"));
                        System.out.println("VIEWED: " + rs.getString("view_status") + System.lineSeparator());
                    }
                } else {
                    System.out.println("The message id you entered is not valid...");
                }
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

}
