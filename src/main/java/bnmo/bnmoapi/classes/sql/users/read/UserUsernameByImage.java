package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserUsernameByImage implements SQLInterface {
    public String filename;

    public UserUsernameByImage(String filename) {
        this.filename = filename;
    }

    public String query() {
        return "SELECT username FROM users WHERE image = '" + this.filename + "'";
    }
}
