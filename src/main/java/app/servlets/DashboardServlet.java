package app.servlets;

import app.model.MyRoom;
import app.service.AppRoom;
import app.dao.RoomDaoImplBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/dashboard")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class DashboardServlet extends HttpServlet {
    private AppRoom appRoom;

    @Override
    public void init() throws ServletException {
        try {
            String url = "jdbc:postgresql://localhost:5432/RPJava";
            String username = "postgres";
            String password = "Justdesserts03";

            RoomDaoImplBD roomDao = new RoomDaoImplBD(url, username, password);
            this.appRoom = new AppRoom(roomDao);
        } catch (Exception e) {
            throw new ServletException("Error initializing DashboardServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("authenticated") == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        String user = (String) session.getAttribute("user");

        List<MyRoom> rooms = appRoom.getAllRooms();
        request.setAttribute("rooms", rooms);
        request.setAttribute("username", user);

        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authenticated") == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        String action = request.getParameter("action");

        if ("addRoom".equals(action)) {
            handleAddRoom(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    private void handleAddRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String roomStr = request.getParameter("room");
            String squareStr = request.getParameter("square");
            String guestsMaxStr = request.getParameter("guestsMax");
            Part photoPart = request.getPart("photo");

            if (roomStr == null || roomStr.trim().isEmpty() ||
                    squareStr == null || squareStr.trim().isEmpty() ||
                    guestsMaxStr == null || guestsMaxStr.trim().isEmpty()) {

                request.setAttribute("error", "Заполните все обязательные поля");
                doGet(request, response);
                return;
            }

            long room = Long.parseLong(roomStr.trim());
            float square = Float.parseFloat(squareStr.trim());
            int guestsMax = Integer.parseInt(guestsMaxStr.trim());

            byte[] photo = null;
            if (photoPart != null && photoPart.getSize() > 0) {
                try (InputStream inputStream = photoPart.getInputStream()) {
                    photo = inputStream.readAllBytes();
                }
            }

            boolean success = appRoom.addRoom(room, square, guestsMax, photo);

            if (success) {
                request.setAttribute("message", "Номер '" + room + "' успешно добавлен");
            } else {
                request.setAttribute("error", "Номер '" + room + "' уже существует");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Проверьте правильность введенных чисел");
        } catch (Exception e) {
            request.setAttribute("error", "Ошибка при добавлении номера: " + e.getMessage());
        }

        doGet(request, response);
    }
}