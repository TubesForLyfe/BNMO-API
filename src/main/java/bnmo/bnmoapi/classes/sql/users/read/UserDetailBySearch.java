package bnmo.bnmoapi.classes.sql.users.read;

import bnmo.bnmoapi.interfaces.sql.SQLInterface;

public class UserDetailBySearch implements SQLInterface {
    public String name_to_search;

    public UserDetailBySearch(String name_to_search) {
        this.name_to_search = name_to_search;
    }

    public String query() {
        return "SELECT * FROM users WHERE verified = 'true' AND (nama LIKE '%" + this.name_to_search + "%' OR username LIKE '%" + this.name_to_search + "%')";
    }
}
