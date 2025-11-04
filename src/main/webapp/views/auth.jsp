<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Авторизация</title>
    <style>
        body {
            background: #f8f9fa;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .login-container {
            width: 100%;
            max-width: 400px;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
        }
        input {
            width: 100%;
            padding: 10px;
        }
        .btn-group {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
            margin-top: 30px;
        }
        button {
            padding: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            background: #007bff;
            color: white;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Авторизация</h1>

        <%
            String error = (String) request.getAttribute("error");
            String message = (String) request.getAttribute("message");
            String loginValue = request.getParameter("login") != null ? request.getParameter("login") : "";
        %>

        <% if (error != null) { %>
            <div><%= error %></div>
        <% } %>

        <% if (message != null) { %>
            <div><%= message %></div>
        <% } %>

        <form method="post" action="auth">
            <div class="form-group">
                <label for="login">Логин</label>
                <input type="text" id="login" name="login" value="<%= loginValue %>" required>
            </div>

            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div class="btn-group">
                <button type="submit" name="action" value="register">Регистрация</button>
                <button type="submit" name="action" value="login">Войти</button>
            </div>
        </form>
    </div>
</body>
</html>