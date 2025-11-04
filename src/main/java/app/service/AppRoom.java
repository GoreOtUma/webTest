package app.service;

import app.dao.RoomDao;
import app.model.MyRoom;

import java.util.List;
import java.util.Optional;

public class AppRoom {
    private RoomDao roomDao;

    public AppRoom(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public boolean addRoom(long room, float square, int guestsMax, byte[] photo) {
        if (roomDao.existsByRoom(room)) {
            return false;
        }

        MyRoom newRoom = new MyRoom(room, square, guestsMax, photo);
        roomDao.save(newRoom);
        return true;
    }

    public boolean addRoom(long room, float square, int guestsMax) {
        return addRoom(room, square, guestsMax, null);
    }

    public byte[] getRoomPhoto(long room) {
        Optional<MyRoom> roomOpt = roomDao.findByRoom(room);
        return roomOpt.map(MyRoom::getPhoto).orElse(null);
    }

    public List<MyRoom> getAllRooms() {
        return roomDao.getAll();
    }
}
