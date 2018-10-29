package project;

import java.util.Arrays;

public class ConsoleApp {

    private String user;
    private char[] pwd;
    private String role;

    public void console_init() throws NullPointerException {
        System.out.println((System.lineSeparator() + ConsoleApp.star("*", 20) + "Welcome to Message administration menu" + ConsoleApp.star("*", 20)));
        identification();
    }

    public static String star(String c, int n) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public void identification() throws NullPointerException {
        Menu menu = new Menu();

        do {
            System.out.println(System.lineSeparator() + "Please enter your username:" + System.lineSeparator());
            user = System.console().readLine();
            //      ConsoleUtils.pauseExecution();
            System.out.println(System.lineSeparator() + "Please enter your password:" + System.lineSeparator());
            pwd = System.console().readPassword();

            role = ConsoleUtils.role_check(user, pwd);

            if (role == null) {
                System.out.println("You entered an invalid combination of username and password!" + System.lineSeparator());
                System.out.println("Do you want to try again?" + System.lineSeparator());

                if (ConsoleUtils.requestConfirmation() == false) {
                    System.exit(0);
                }
            }
        } while (role == null);

        menu.setTitle(ConsoleApp.star("*", 30) + "Identification Menu" + ConsoleApp.star("*", 29));
        menu.addItem(new MenuItem("You logged in as " + role, this, role + "_menu"));          //Based on the role type, MenuItem redirects to the appropriate Menu 
        menu.execute();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.user = role;
    }

    public String getUser() {
        return user;
    }

    public char[] getPwd() {
        return pwd;
    }

    public void setPwd(char[] pwd) {
        this.pwd = pwd;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void user_menu() {
        Menu menu = new Menu();
        Core_User u = new Core_User(user, pwd, role);

        menu.setTitle(ConsoleApp.star("*", 31) + "Simple User Menu" + ConsoleApp.star("*", 31));
        menu.addItem(new MenuItem("Create a new message", u, "create_msg"));
        menu.addItem(new MenuItem("Check your inbox", u, "inbox"));
        menu.addItem(new MenuItem("Check your outbox", u, "outbox"));
        menu.addItem(new MenuItem("Update one of your messages", u, "update_msg"));
        menu.addItem(new MenuItem("Delete one of your messages", u, "delete_msg"));

        menu.execute();

    }

    public void super_user_menu() {
        Menu menu = new Menu();
        SUser su = new SUser(user, pwd, role);

        menu.setTitle(ConsoleApp.star("*", 32) + "Super User Menu" + ConsoleApp.star("*", 31));
        menu.addItem(new MenuItem("Create a new message", su, "create_msg"));
        menu.addItem(new MenuItem("Check your inbox", su, "inbox"));
        menu.addItem(new MenuItem("Check your outbox", su, "outbox"));
        menu.addItem(new MenuItem("Update one of your messages", su, "update_msg"));
        menu.addItem(new MenuItem("Delete one of your messages", su, "delete_msg"));
        menu.addItem(new MenuItem("Super User privilege *** Read messages from ALL users***", su, "readall_msg"));

        menu.execute();
    }

    public void moderator_menu() {
        Menu menu = new Menu();
        Moderator modr = new Moderator(user, pwd, role);

        menu.setTitle(ConsoleApp.star("*", 32) + "Moderator Menu" + ConsoleApp.star("*", 31));
        menu.addItem(new MenuItem("Create a new message", modr, "create_msg"));
        menu.addItem(new MenuItem("Check your inbox", modr, "inbox"));
        menu.addItem(new MenuItem("Check your outbox", modr, "outbox"));
        menu.addItem(new MenuItem("Update one of your messages", modr, "update_msg"));
        menu.addItem(new MenuItem("Delete one of your messages", modr, "delete_msg"));
        menu.addItem(new MenuItem("Moderator privilege *** Read messages from ALL users***", modr, "readall_msg"));
        menu.addItem(new MenuItem("Moderator privilege *** Update messages from ALL users***", modr, "updateall_msg"));

        menu.execute();

    }

    public void admin_menu() {
        Menu menu = new Menu();
        Admin adm = new Admin(user, pwd, role);

        menu.setTitle(ConsoleApp.star("*", 34) + "Admin Menu" + ConsoleApp.star("*", 34));
        menu.addItem(new MenuItem("Create a new message", adm, "create_msg"));
        menu.addItem(new MenuItem("Check your inbox", adm, "inbox"));
        menu.addItem(new MenuItem("Check your outbox", adm, "outbox"));
        menu.addItem(new MenuItem("Update one of your messages", adm, "update_msg"));
        menu.addItem(new MenuItem("Delete one of your messages", adm, "delete_msg"));
        menu.addItem(new MenuItem("Admin privilege *** Read messages from ALL users***", adm, "readall_msg"));
        menu.addItem(new MenuItem("Admin privilege *** Update messages from ALL users***", adm, "updateall_msg"));
        menu.addItem(new MenuItem("Admin privilege *** Delete messages from ALL users***", adm, "deleteall_msg"));

        menu.execute();

    }

    public void super_admin_menu() {
        Menu menu = new Menu();
        SuperAdmin sadm = new SuperAdmin(user, pwd, role);

        menu.setTitle(ConsoleApp.star("*", 31) + "Super Admin Menu" + ConsoleApp.star("*", 31));
        menu.addItem(new MenuItem("Create a new message", sadm, "create_msg"));
        menu.addItem(new MenuItem("Check your inbox", sadm, "inbox"));
        menu.addItem(new MenuItem("Check your outbox", sadm, "outbox"));
        menu.addItem(new MenuItem("Update one of your messages", sadm, "update_msg"));
        menu.addItem(new MenuItem("Delete one of your messages", sadm, "delete_msg"));
        menu.addItem(new MenuItem("Super Admin privilege *** Read messages from ALL users***", sadm, "readall_msg"));
        menu.addItem(new MenuItem("Super Admin privilege *** Update messages from ALL users***", sadm, "updateall_msg"));
        menu.addItem(new MenuItem("Super Admin privilege *** Delete messages from ALL users***", sadm, "deleteall_msg"));
        menu.addItem(new MenuItem("Super Admin privilege *** Create new user***", sadm, "create_users"));
        menu.addItem(new MenuItem("Super Admin privilege *** View user***", sadm, "view_users"));
        menu.addItem(new MenuItem("Super Admin privilege *** Update user***", sadm, "update_users"));
        menu.addItem(new MenuItem("Super Admin privilege *** Delete user***", sadm, "delete_users"));
        menu.addItem(new MenuItem("Super Admin privilege *** DELETE EVERYTHING***", sadm, "delete_allusers"));

        menu.execute();
    }
}
