package app.service;

import app.dao.UserDao;
import app.model.MyUser;

import java.util.List;
import java.util.Optional;

public class AppRegAuth {
    private UserDao userDao;

    public AppRegAuth(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean register(String login, String password) {
        if (userDao.existsByLogin(login)) {
            //System.out.println("Пользователь с таким логином уже существует!");
            return false;
        }

        MyUser newUser = new MyUser(login, password);
        userDao.save(newUser);
        //System.out.println("Регистрация прошла успешно! ID: " + newUser.getId());
        return true;
    }

    public boolean auth(String login, String password) {
        Optional<MyUser> user = userDao.findByLoginAndPassword(login, password);
        if (user.isPresent()) {
            //System.out.println("Авторизация прошла успешно! Добро пожаловать, " + login);
            return true;
        }

        //System.out.println("Неверный логин или пароль!");
        return false;
    }

    public void displayAllUsers() {
        List<MyUser> users = userDao.getAll();

        //System.out.println("\nСписок всех пользователей");
        for (MyUser user : users) {
            //System.out.println("ID: " + user.getId() + " | Логин: " + user.getLogin() + " | Пароль: " + user.getPassword());
        }
        //System.out.println("\n");
    }

    public void updateUser(long id, String newLogin, String newPassword) {
        Optional<MyUser> userOpt = userDao.get(id);
        if (userOpt.isEmpty()) {
            //System.out.println("Пользователь с ID " + id + " не найден!");
            return;
        }

        MyUser user = userOpt.get();
        userDao.update(user, new String[]{newLogin, newPassword});
        //System.out.println("Пользователь обновлен!");
    }

    public void deleteUser(long id) {
        Optional<MyUser> userOpt = userDao.get(id);
        if (userOpt.isEmpty()) {
            //System.out.println("Пользователь с ID " + id + " не найден!");
            return;
        }

        userDao.delete(userOpt.get());
        //System.out.println("Пользователь удален!");
    }
}