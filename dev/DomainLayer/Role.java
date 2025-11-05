package DomainLayer;

import java.util.ArrayList;
import java.util.Arrays;

//This department aims to represent different roles in the store that will be associated with each employee.
public class Role {
    private ArrayList<String> roles;
    private static Role instance;

    private Role(){
        roles = new ArrayList<>();
        initRoles();
    }

    private void initRoles(){
        String[] _roles = {"ShiftManager", "Cashier", "StoreKeeper", "Driver_A", "Driver_B", "Driver_C", "Driver_D", "DriverManager", "HrManager"};
        this.roles.addAll(Arrays.asList(_roles));
    }

    public static Role getInstance(){
        if (instance == null){
            instance = new Role();
        }
        return instance;
    }

    public ArrayList<String> getRoles() {
        return this.roles;
    }

    public void setRoles(ArrayList<String> validRoles) {
        this.roles = validRoles;
    }

    public void addRole(String role){
        if (role != null){
            if (!this.roles.contains(role)){
                this.roles.add(role);
            }
        }
    }

    public boolean containsRole(String role){
        return this.roles.contains(role);
    }

    public void deleteRole(String role){
        if (!role.equals("HrManager") && containsRole(role)){
            roles.remove(role);
        }
    }

    @Override
    public String toString() {
        return String.join(", ",roles);
    }

}