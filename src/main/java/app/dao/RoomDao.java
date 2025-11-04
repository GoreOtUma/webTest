package app.dao;
import app.model.MyRoom;

import java.util.Optional;

public interface RoomDao extends Dao<MyRoom>{
    Optional<MyRoom> findByRoom(long room);
    boolean existsByRoom(long room);
}
