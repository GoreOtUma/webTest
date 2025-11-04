package app.model;

public class MyRoom {
    private long room;
    private float square;
    private int guestsMax;
    private byte[] photo;

    public MyRoom() {}

    public MyRoom(long room, float square, int guestsMax) {
        this.room = room;
        this.square = square;
        this.guestsMax = guestsMax;
    }

    public MyRoom(long room, float square, int guestsMax, byte[] photo) {
        this.room = room;
        this.square = square;
        this.guestsMax = guestsMax;
        this.photo = photo;
    }

    public long getRoom() { return room; }
    public void setRoom(long room) { this.room = room; }

    public float getSquare() { return square; }
    public void setSquare(float square) { this.square = square; }

    public int getGuestsMax() { return guestsMax; }
    public void setGuestsMax(int guestsMax) { this.guestsMax = guestsMax; }

    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    @Override
    public String toString() {
        return room + ":" + square + ":" + guestsMax;
    }
}
