package by.bsuir.deliveryservice.entity;

import java.io.Serializable;

public class User extends Entity implements Serializable {
    private String login;
    private String password;
    private String phone;
    private UserRole role;
    private String fullName;
    private String passport;

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

    public User(String login,  String password, String phone) {
        this(login, password);
        this.phone = phone;
    }

    public User(String login, String password, String phone, UserRole role) {
        this(login, password, phone);
        this.role = role;
    }

    public User(String login, String password, String phone, String fullName, String passport) {
        this.login = login;
        this.password = password;
        this.phone = phone;
        this.fullName = fullName;
        this.passport = passport;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null) return false;
        return !(passport != null ? !passport.equals(user.passport) : user.passport != null);

    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (passport != null ? passport.hashCode() : 0);
        return result;
    }
}
