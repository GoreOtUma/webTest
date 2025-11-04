package app.model;

public class MyUser {
    private long id;
    private String login;
    private String password;

    public MyUser() {}

    public MyUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public MyUser(long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return id + ":" + login + ":" + password;
    }

    public static MyUser fromString(String userString) {
        if (userString == null || userString.trim().isEmpty()) {
            return null;
        }

        String[] parts = userString.split(":");
        if (parts.length == 3) {
            try {
                long id = Long.parseLong(parts[0].trim());
                String login = parts[1].trim();
                String password = parts[2].trim();
                if (!login.isEmpty() && !password.isEmpty()) {
                    return new MyUser(id, login, password);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}