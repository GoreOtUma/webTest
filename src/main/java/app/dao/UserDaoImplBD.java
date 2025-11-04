package app.dao;

import app.model.MyUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImplBD implements UserDao {
    private final String url;
    private final String username;
    private final String password;

    public UserDaoImplBD(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        initializeDatabase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<MyUser> get(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new MyUser(
                        rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
           // System.out.println("Ошибка при получении пользователя: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<MyUser> getAll() {
        List<MyUser> users = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users ORDER BY id")) {

            while (rs.next()) {
                users.add(new MyUser(
                        rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка при получении пользователей: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void save(MyUser user) {
        String sql = "INSERT INTO users (login, password) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    @Override
    public void update(MyUser user, String[] params) {
        if (params.length < 2) return;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET login = ?, password = ? WHERE id = ?")) {

            stmt.setString(1, params[0]);
            stmt.setString(2, params[1]);
            stmt.setLong(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            //System.out.println("Ошибка при обновлении: " + e.getMessage());
        }
    }

    @Override
    public void delete(MyUser user) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {

            stmt.setLong(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            //System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }

    @Override
    public Optional<MyUser> findByLogin(String login) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE login = ?")) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new MyUser(
                        rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка при поиске по логину: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<MyUser> findByLoginAndPassword(String login, String password) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE login = ? AND password = ?")) {

            stmt.setString(1, login);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new MyUser(
                        rs.getLong("id"),
                        rs.getString("login"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка при авторизации: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByLogin(String login) {
        return findByLogin(login).isPresent();
    }

    private void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGSERIAL PRIMARY KEY,
                login VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
        } catch (SQLException e) {
            //System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }
}