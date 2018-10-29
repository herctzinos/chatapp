package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Moderator extends SUser {

    private static final String DB_URL = "127.0.0.1:3306";
    private static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL + "/chat_db?zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String DB_USER = "herc";
    private static final String DB_PASSWD = "Kalhas!3";
    private String username;
    private char[] password;
    private String role;

    public Moderator(String user, char[] pwd, String role) {
        super(user, pwd, role);
    }

    public void updateall_msg() {
        Connection connection = null;
        PreparedStatement pstm = null;
        String updallmsg;
        String updallid;
        try {

            readall_msg();
            System.out.println("Enter the id of the message you want to modify: " + System.lineSeparator());
            updallid = System.console().readLine();

            System.out.println("Are you sure you want to modify message " + updallid + "?" + System.lineSeparator());

            if (ConsoleUtils.requestConfirmation()) {
                System.out.println("Write your new message: ");
                updallmsg = System.console().readLine();
                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                String sql1 = "UPDATE `chat_db`.`messages` SET `message_text` = ? WHERE (`id` = ?);";
                pstm = connection.prepareStatement(sql1);
                pstm.setString(1, updallmsg);
                pstm.setString(2, updallid);

                int a = pstm.executeUpdate();
                if (a == 0) {
                    System.out.println("The data you entered are not valid...");
                } else {
                    System.out.println("Message modified!");
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
