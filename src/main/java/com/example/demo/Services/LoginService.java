package com.example.demo.Services;

import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

//Lavet af Marco Pedersen og Thomas Vindelev

@Service
public class LoginService implements Users<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashingService hashingService;

    /**
     * Bekræfter login-oplysninger og sætter brugerens attributter
     *
     */

    @Override
    public boolean verify(User user) {
        user.setPassword(hashingService.hash(user.getPassword()));
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
            }
            userRepository.closeConnections(resultSet);
            return true;
        } catch (SQLException e) {
            userRepository.closeConnections(resultSet);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Aktiverer en bruger og giver vedkommende et nyt kodeord
     *
     */

    public boolean activateUser(String password, String passwordConfirm, int id) {
        if (password.equals(passwordConfirm)) {
            userRepository.activateUser(hashingService.hash(password), id);
            return true;
        } else {
            return false;
        }
    }

}
