package by.bsuir.deliveryservice.entity;

public class User extends Entity{
    private String login;
    private String password;
    private String email;
    private UserRole role;

    public User() {}

    public User(long id) {
        setId(id);
    }

    public User(long id, String login) {
        this.login = login;
        setId(id);
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login,  String password, String email) {
        this(login, password);
        this.email = email;
    }

    public User(String login, String password, String email, UserRole role) {
        this(login, password, email);
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return role != user.role;

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
