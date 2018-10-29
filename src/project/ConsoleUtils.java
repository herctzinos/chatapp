package project;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsoleUtils {

    private static Console console = null;
    private static final String DB_URL = "127.0.0.1:3306";
    private static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL + "/chat_db?zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String DB_USER = "herc";
    private static final String DB_PASSWD = "Kalhas!3";

    public static String role_check(String user, char[] pwd) {
        Connection connection = null;
        ResultSet resultSet = null;
        String role = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;

        try {
            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);

            String sql = "SELECT* FROM users WHERE user_name=?;";

            String sql2 = "SELECT * FROM chat_db.role_desrc\n"
                    + "INNER JOIN users on role_desrc.id=users.role and BINARY users.user_name=?";

            pstm = connection.prepareStatement(sql);
            pstm.setString(1, user);

            pstm2 = connection.prepareStatement(sql2);
            pstm2.setString(1, user);

            resultSet = pstm.executeQuery();
            int flag = 0;

            while (resultSet.next()) {
                if (resultSet.getString("password").equals(new String(pwd))) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                resultSet = pstm2.executeQuery();
                while (resultSet.next()) {
                    role = resultSet.getString("role_type");
                }

            }

        } catch (SQLException ex) {
            System.out.println(ex.toString());
            System.exit(0);
        } finally {
            try {
                pstm.close();
                connection.close();
            } catch (SQLException ex) {
            }
        }

        return role;
    }

    /*
	 * This method will force execution to stop and wait until the user presses enter. It prompts the 
	 * user to press enter to continue. It is irrelevant if the user types any text, execution will 
	 * continue after 'Enter'.
     */
    public static void pauseExecution() {
        if (console == null) {
            console = System.console();
        }
        System.out.print("Press Enter to Continue... ");
        console.readLine();
    }

    /*
	 * This method can be used if a particular operation requires confirmation, it is useful for delete
	 * or irreversible operations. It forces that the uses explicitly enters "y/yes" or "n/no", any 
	 * other input will fail and the confirmation request will be presented again.
     */
    public static boolean requestConfirmation() {
        String in = "";
        if (console == null) {
            console = System.console();
        }

        while (true) {
            System.out.print("Confirm Operation (y/n)... ");
            in = console.readLine().toLowerCase();
            if (in.equals("y") || in.equals("yes")) {
                return true;
            } else if (in.equals("n") || in.equals("no")) {
                return false;
            }
        }
    }
}
