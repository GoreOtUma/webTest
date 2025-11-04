<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="app.model.MyRoom" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<html>
<head>
    <title>Номера</title>
    <style>
        body {
            margin: 40px;
            background: #f8f9fa;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        .logout {
            color: #666;
            text-decoration: none;
        }
        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr auto;
            gap: 10px;
            margin-bottom: 30px;
            align-items: end;
        }
        .form-group {
            display: flex;
            flex-direction: column;
        }
        label {
            margin-bottom: 5px;
            color: #333;
        }
        input {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .btn {
            padding: 8px 16px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .rooms {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 15px;
        }
        .room {
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 8px;
            background: white;
        }
        .room-img {
            width: 100%;
            height: 150px;
            border-radius: 4px;
            margin-bottom: 10px;
        }
        .no-img {
            width: 100%;
            height: 150px;
            background: #e9ecef;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #6c757d;
            border-radius: 4px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Номера гостиницы</h1>
        <a href="auth?logout=true" class="logout">Выйти</a>
    </div>

    <%
        String error = (String) request.getAttribute("error");
        String message = (String) request.getAttribute("message");
        List<MyRoom> rooms = (List<MyRoom>) request.getAttribute("rooms");
    %>

    <% if (error != null) { %>
        <div><%= error %></div>
    <% } %>

    <% if (message != null) { %>
        <div ><%= message %></div>
    <% } %>

    <form method="post" action="dashboard" enctype="multipart/form-data">
        <input type="hidden" name="action" value="addRoom">

        <div class="form-grid">
            <div class="form-group">
                <label>Номер</label>
                <input type="number" name="room" required>
            </div>

            <div class="form-group">
                <label>Площадь</label>
                <input type="number" step="0.1" name="square" required>
            </div>

            <div class="form-group">
                <label>Гостей</label>
                <input type="number" name="guestsMax" required>
            </div>

            <div class="form-group">
                <label>Фото</label>
                <input type="file" name="photo" accept="image/*">
            </div>

            <button type="submit" class="btn">Добавить</button>
        </div>
    </form>

    <h2>Все номера</h2>

    <% if (rooms != null && !rooms.isEmpty()) { %>
        <div class="rooms">
            <% for (MyRoom room : rooms) {
                byte[] photoData = room.getPhoto();
                String photoBase64 = null;
                if (photoData != null && photoData.length > 0) {
                    photoBase64 = Base64.getEncoder().encodeToString(photoData);
                }
            %>
                <div class="room">
                    <% if (photoBase64 != null) { %>
                        <img src="data:image/jpeg;base64,<%= photoBase64 %>"
                             class="room-img"
                             alt="Номер <%= room.getRoom() %>">
                    <% } else { %>
                        <div class="no-img">Нет фото</div>
                    <% } %>

                    <div><strong>Номер: <%= room.getRoom() %></strong></div>
                    <div>Площадь: <%= room.getSquare() %></div>
                    <div>Гостей: <%= room.getGuestsMax() %></div>
                </div>
            <% } %>
        </div>
    <% } else { %>
        <p>Нет номеров</p>
    <% } %>
</body>
</html>