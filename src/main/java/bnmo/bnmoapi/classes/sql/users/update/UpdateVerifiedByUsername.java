package bnmo.bnmoapi.classes.sql.users.update;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UpdateVerifiedByUsername implements SQLInterface {
    public String username;

    public UpdateVerifiedByUsername(String username) {
        this.username = username;
    }

    public String query() {
        return "UPDATE users SET verified = 'true' WHERE username = '" + this.username + "'";
    }
}
