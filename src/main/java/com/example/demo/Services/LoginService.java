package com.example.demo.Services;

import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class LoginService implements com.example.demo.Services.Service<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean verify(User user) {
        ResultSet resultSet = userRepository.verifyUserLogin(user);
        try {
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id_users"));
                user.setUsername(resultSet.getString("username"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setFk_roles(resultSet.getInt("id_roles"));
                user.setActive(resultSet.getBoolean("is_active"));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean activateUser(String password, String passwordConfirm, int id) {
        if (password.equals(passwordConfirm)) {
            userRepository.activateUser(password, id);
            return true;
        } else {
            return false;
        }
    }

}
