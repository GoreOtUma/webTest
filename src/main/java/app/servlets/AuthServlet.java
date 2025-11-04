package app.servlets;

import app.service.AppRegAuth;
import app.dao.UserDaoImplBD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private AppRegAuth appRegAuth;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/RPJava";
            String username = "postgres";
            String password = "Justdesserts03";

            UserDaoImplBD userDao = new UserDaoImplBD(url, username, password);
            this.appRegAuth = new AppRegAuth(userDao);
        } catch (ClassNotFoundException e) {
            throw new ServletException("PostgreSQL Driver not found", e);
        } catch (Exception e) {
            throw new ServletException("Error initializing AuthServlet", e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("true".equals(request.getParameter("logout"))) {
            request.getSession().invalidate();
        }

        request.getRequestDispatcher("/views/auth.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if ("register".equals(action)) {
            handleRegistration(request, response, login, password);
        } else if ("login".equals(action)) {
            handleLogin(request, response, login, password);
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response,
                                    String login, String password) throws IOException, ServletException {
        if (login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Логин и пароль не могут быть пустыми");
            request.getRequestDispatcher("/views/auth.jsp").forward(request, response);
            return;
        }

        boolean success = appRegAuth.register(login.trim(), password.trim());

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("user", login);
            session.setAttribute("authenticated", true);
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Логин '" + login + "' уже занят");
            request.getRequestDispatcher("/views/auth.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response,
                             String login, String password) throws IOException, ServletException {
        if (login == null || login.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Логин и пароль не могут быть пустыми");
            request.getRequestDispatcher("/views/auth.jsp").forward(request, response);
            return;
        }

        boolean success = appRegAuth.auth(login.trim(), password.trim());

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("user", login);
            session.setAttribute("authenticated", true);

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Неверный логин или пароль");
            request.getRequestDispatcher("/views/auth.jsp").forward(request, response);
        }
    }
}