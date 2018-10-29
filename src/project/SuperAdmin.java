package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuperAdmin extends Admin {

    private static final String DB_URL = "127.0.0.1:3306";
    private static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL + "/chat_db?zeroDateTimeBehavior=convertToNull&useSSL=false";
    private static final String DB_USER = "herc";
    private static final String DB_PASSWD = "Kalhas!3";
    private String username;
    private char[] password;
    private String role;

    public SuperAdmin(String user, char[] pwd, String role) {
        super(user, pwd, role);
    }

    public void view_users() {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            String sql = "SELECT users.id,users.user_name,users.password, role_type FROM users\n"
                    + "JOIN role_desrc desrc on users.role = desrc.id";
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();

            if (rs.first()) {
                while (rs.next()) {
                    System.out.println("USERNAME: " + rs.getString("user_name"));
                    System.out.println("PASSWORD: " + rs.getString("password"));
                    System.out.println("PRIVILEGES: " + rs.getString("role_type") + System.lineSeparator());

                }
            } else {
                System.out.println("The username you entered is not valid...");
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

    public void create_users() {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;

        String new_user;
        String new_pwd;
        String new_role = null;
        try {
            view_users();
            System.out.println("Enter new username: ");
            new_user = System.console().readLine();

            System.out.println("Enter his/her password: ");
            new_pwd = System.console().readLine();
            do {
                System.out.println("Enter his role");
                System.out.println("There can be ONLY 1 Super Admin!");
                System.out.println("Press 2 to assign Admin");
                System.out.println("Press 3 to assign Moderator");
                System.out.println("Press 4 to assign Super User");
                System.out.println("Press 5 to assign User");

                new_role = System.console().readLine();
            } while (new_role.equals("1"));

            connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
            String sql1 = "INSERT INTO `chat_db`.`users` (`user_name`, `password`, `role`) "
                    + "VALUES (?, ?, ?);";
            pstm = connection.prepareStatement(sql1);
            pstm.setString(1, new_user);
            pstm.setString(2, new_pwd);
            pstm.setString(3, new_role);

            pstm.execute();

            System.out.println("User created!");

        } catch (SQLException ex) {
            System.out.println("The data you entered are not valid!");
            MenuItem item = new MenuItem(null);

        } finally {
            try {
                pstm.close();
                connection.close();

            } catch (SQLException ex) {
            }
        }
    }

    public void update_users() {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        String old_user;
        String upd_user;
        String upd_pwd;
        String upd_role;

        try {
            view_users();

            System.out.println("Enter username of the user you want to change: ");
            old_user = System.console().readLine();

            System.out.println("Enter new username: ");
            upd_user = System.console().readLine();

            System.out.println("Enter his password: ");
            upd_pwd = System.console().readLine();

            System.out.println("Enter his role: ");

            System.out.println("There can be ONLY 1 Super Admin!");
            System.out.println("Press 2 to assign  Admin");
            System.out.println("Press 3 to assign Moderator");
            System.out.println("Press 4 to assign Super User");
            System.out.println("Press 5 to assign User");
            upd_role = System.console().readLine();

            System.out.println("Are you sure you want to modify " + old_user + "?");

            if (ConsoleUtils.requestConfirmation()) {
                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                String sql1 = "UPDATE `chat_db`.`users` "
                        + "SET `user_name` = ?, `password` = ?, `role` = ? WHERE  (`user_name` = ?);";
                pstm = connection.prepareStatement(sql1);
                pstm.setString(1, upd_user);
                pstm.setString(2, upd_pwd);
                pstm.setString(3, upd_role);
                pstm.setString(4, old_user);

                int a = pstm.executeUpdate();
                if (a == 0) {
                    System.out.println("The data you entered are not valid...");
                } else {
                    System.out.println("User modified!");
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

    public void delete_users() {
        Connection connection = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm2 = null;

        String del_user;
        try {
            view_users();
            System.out.println("Enter username of the user you want to delete: ");

            del_user = System.console().readLine();

            System.out.println("Are you sure you want to delete user: " + del_user + "? Action is irreversible..." + System.lineSeparator());

            if (ConsoleUtils.requestConfirmation()) {

                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                String sql1 = "DELETE FROM `chat_db`.`users` WHERE  (`user_name` = ?);";
                pstm = connection.prepareStatement(sql1);
                pstm.setString(1, del_user);

                int a = pstm.executeUpdate();

                if (a == 0) {
                    System.out.println("The id you entered is not valid...");
                } else {
                    System.out.println("User deleted!");
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

    public void delete_allusers() {
        Connection connection = null;
        PreparedStatement pstm1 = null;
        PreparedStatement pstm2 = null;
        PreparedStatement pstm3 = null;
        PreparedStatement pstm4 = null;
        PreparedStatement pstm5 = null;

        try {
            view_users();
            System.out.println("Woooooah do you really want to delete all your users and their messages??? " + System.lineSeparator());

            if (ConsoleUtils.requestConfirmation()) {
                System.out.println("Come on, why would you want to do that!?!? Are you really sure? " + System.lineSeparator());

                if (ConsoleUtils.requestConfirmation()) {

                    System.out.println("Ok ok, it's your choice after all. So we are doing this, right? " + System.lineSeparator());

                    if (ConsoleUtils.requestConfirmation()) {

                        System.out.println("Great no problem, just give me now your final confirmation.. " + System.lineSeparator());

                        if (ConsoleUtils.requestConfirmation()) {

                            System.out.println("Jeez you are really serious with this thing, OK THIS IS THE ABSOLUTE FINAL CONFIRMATION...! PRESS THE DAMN BUTTON!!!!!!!!!!! " + System.lineSeparator());
                            if (ConsoleUtils.requestConfirmation()) {

                                System.out.println("Aaaaaaaaaaaaaaand they are gone. ALL GONE.... Seriously..... Shame on you!!!" + System.lineSeparator());

                                connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWD);
                                String sql1 = "truncate messages;";
                                String sql2 = "truncate user_data;";
                                String sql3 = "create temporary table temp select * from users where user_name='admin';";

                                String sql4 = "truncate users;";
                                String sql5 = "insert into users select * from temp;";

                                pstm1 = connection.prepareStatement(sql1);
                                pstm2 = connection.prepareStatement(sql2);
                                pstm3 = connection.prepareStatement(sql3);
                                pstm4 = connection.prepareStatement(sql4);
                                pstm5 = connection.prepareStatement(sql5);

                                pstm1.executeUpdate();
                                pstm2.executeUpdate();
                                pstm3.executeUpdate();
                                pstm4.executeUpdate();
                                pstm5.executeUpdate();

                            }
                        }
                    }
                }
            }

        } catch (SQLException ex) {
            System.out.println("Sorry, problems with the database connection!");
            System.out.println(ex.toString());

        } finally {
            try {
                pstm1.close();
                pstm2.close();
                pstm3.close();
                pstm4.close();
                pstm5.close();
				
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

}
