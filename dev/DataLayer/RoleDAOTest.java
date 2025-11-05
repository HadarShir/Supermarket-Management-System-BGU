package DataLayer;

import java.sql.SQLException;
import java.util.ArrayList;

public class RoleDAOTest {
    public static void main(String[] args) {
        RoleDAO dao = new RoleDAO();
        String testRole = "TestRole";
        String testUser = "e_1_b0_ShiftManager";
        try {
            // שליפת כל התפקידים
            System.out.println("=== All roles ===");
            ArrayList<String> roles = new ArrayList<>(dao.getAllRoles().roles());
            for (String r : roles) {
                System.out.println(r);
            }

            // הוספת תפקיד חדש
            System.out.println("\n=== Adding role: " + testRole + " ===");
            dao.addRole(testRole);
            roles = new ArrayList<>(dao.getAllRoles().roles());
            System.out.println("Roles after adding:");
            for (String r : roles) {
                System.out.println(r);
            }

            // שיוך תפקיד לעובד
            System.out.println("\n=== Assigning role to user ===");
            dao.assignRoleToEmployee(testUser, testRole);
            ArrayList<String> userRoles = new ArrayList<>(dao.getRolesForEmployee(testUser).roles());
            System.out.println("Roles for user " + testUser + ":");
            for (String r : userRoles) {
                System.out.println(r);
            }

            // הסרת תפקיד מהעובד
            System.out.println("\n=== Removing role from user ===");
            dao.removeRoleFromEmployee(testUser, testRole);
            userRoles = new ArrayList<>(dao.getRolesForEmployee(testUser).roles());
            System.out.println("Roles for user after removal:");
            for (String r : userRoles) {
                System.out.println(r);
            }

            // מחיקת תפקיד
            System.out.println("\n=== Deleting role: " + testRole + " ===");
            dao.deleteRole(testRole);
            roles = new ArrayList<>(dao.getAllRoles().roles());
            System.out.println("Roles after deletion:");
            for (String r : roles) {
                System.out.println(r);
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error:");
            e.printStackTrace();
        }
    }
} 