package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserDetailByUnverified implements SQLInterface {
    public UserDetailByUnverified() {
        //
    }

    public String query() {
        return "SELECT * FROM users WHERE verified = 'false'";
    }
}
