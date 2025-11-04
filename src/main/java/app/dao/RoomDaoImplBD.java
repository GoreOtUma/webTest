package app.dao;

import app.model.MyRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDaoImplBD implements RoomDao {
    private final String url;
    private final String username;
    private final String password;

    public RoomDaoImplBD(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        initializeDatabase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<MyRoom> get(long room) {
        String sql = "SELECT * FROM rooms WHERE room = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, room);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new MyRoom(
                        rs.getLong("room"),
                        rs.getFloat("square"),
                        rs.getInt("guestsMax"),
                        rs.getBytes("photo")
                ));
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка при получении номера: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<MyRoom> getAll() {
        List<MyRoom> rooms = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms ORDER BY room")) {

            while (rs.next()) {
                rooms.add(new MyRoom(
                        rs.getLong("room"),
                        rs.getFloat("square"),
                        rs.getInt("guestsMax"),
                        rs.getBytes("photo")
                ));
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка при получении номеров: " + e.getMessage());
        }
        return rooms;
    }

    @Override
    public void save(MyRoom room) {
        String sql = "INSERT INTO rooms (room, square, guestsMax, photo) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, room.getRoom());
            stmt.setFloat(2, room.getSquare());
            stmt.setInt(3, room.getGuestsMax());
            if (room.getPhoto() != null && room.getPhoto().length > 0) {
                stmt.setBytes(4, room.getPhoto());
            } else {
                stmt.setNull(4, Types.BINARY);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            //System.out.println("Ошибка при сохранении: " + e.getMessage());
            //e.printStackTrace();
        }
    }

    @Override
    public Optional<MyRoom> findByRoom(long room) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE room = ?")) {

            stmt.setLong(1, room);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new MyRoom(
                        rs.getLong("room"),
                        rs.getFloat("square"),
                        rs.getInt("guestsMax"),
                        rs.getBytes("photo")
                ));
            }
        } catch (SQLException e) {
            //System.out.println("Ошибка при поиске по номеру комнаты: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByRoom(long room) {
        return findByRoom(room).isPresent();
    }

    private void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS rooms (
                room BIGINT PRIMARY KEY,
                square FLOAT NOT NULL,
                guestsMax INT NOT NULL,
                photo BYTEA
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
        } catch (SQLException e) {
            //System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @Override
    public void update(MyRoom room, String[] params) {
        throw new UnsupportedOperationException("Update not implemented for rooms");
    }

    @Override
    public void delete(MyRoom room) {
        throw new UnsupportedOperationException("Delete not implemented for rooms");
    }
}