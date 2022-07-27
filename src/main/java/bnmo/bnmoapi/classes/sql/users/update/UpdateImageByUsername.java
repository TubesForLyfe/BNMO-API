package bnmo.bnmoapi.classes.sql.users.update;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UpdateImageByUsername implements SQLInterface {
    public String filename;
    public String username;

    public UpdateImageByUsername(String filename, String username) {
        this.filename = filename;
        this.username = username;
    }

    public String query() {
        return "UPDATE users SET image = '" + this.filename + "' WHERE username = '" + this.username + "'";
    }
}
