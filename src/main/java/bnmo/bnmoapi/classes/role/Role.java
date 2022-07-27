package bnmo.bnmoapi.classes.role;

import bnmo.bnmoapi.classes.token.Token;
import bnmo.bnmoapi.interfaces.role.RolePermissionInterface;

public class Role implements RolePermissionInterface {
    public Token token;
    public String permission;

    public Role(Token token) {
        this.token = token;
    }

    public void setPermission(String role) {
        this.permission = role;
    }

    public boolean isCustomer() {
        return this.permission.equals("customer");
    }

    public boolean isAdmin() {
        return this.permission.equals("admin");
    }
}
