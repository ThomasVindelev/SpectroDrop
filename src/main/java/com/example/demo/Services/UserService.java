package com.example.demo.Services;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Lavet af Marco Pedersen og Thomas Vindelev

@Service
public class UserService implements Users<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashingService hashingService;

    @Autowired
    private TaskService taskService;

    /**
     * Henter brugere baseret på type
     *
     */

    public List<User> getUsers(String type) {
        ResultSet resultSet = null;
        switch (type) {
            case "Employees":
                resultSet = userRepository.getUsersByRole(1);
                break;
            case "Customers":
                resultSet = userRepository.getUsersByRole(2);
                break;
            case "All":
                resultSet = userRepository.getUsers(true);
                break;
            case "New":
                resultSet = userRepository.getUsers(false);
                break;
        }
        List<User> userList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id_users"));
                user.setUsername(resultSet.getString("username"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("Roles.role"));
                user.setFk_roles(resultSet.getInt("Users.fk_roles"));
                userList.add(user);
            }
            userRepository.closeConnections(resultSet);
            return userList;
        } catch (SQLException e) {
            userRepository.closeConnections(resultSet);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sender bruger til databasen hvis brugeren kan passere verify()
     *
     */

    public boolean newUser(User user) {
        if (!verify(user)) {
            user.setPassword(hashingService.hash(user.getPassword()));
            return userRepository.newUser(user);
        } else {
            return true;
        }
    }

    /**
     * Sletter en bruger, men sorterer efter id = 1, da denne er en super-bruger, som ikke skal slettes
     *
     */

    public boolean deleteUserById(int userId, int roleId) {
        if (userId == 1) {
            return true;
        } else if (!taskService.taskResponsibility(userId, roleId)) {
            return userRepository.deleteUserById(userId);
        }
        return true;
    }

    /**
     * Henter alle roller
     *
     */

    public List<Role> getRoles() {
        ResultSet resultSet = userRepository.getRoles();
        List<Role> roleList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt("id_roles"));
                role.setName(resultSet.getString("role"));
                roleList.add(role);
            }
            userRepository.closeConnections(resultSet);
            return roleList;
        } catch (SQLException e) {
            userRepository.closeConnections(resultSet);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prøver at opdatere en bruger, men reverter til den gamle bruger, hvis brugeren ikke kan oprettes med de nye oplysninger
     *
     */

    public boolean verifyUpdate(User user) {
        ResultSet oldInformation = userRepository.getUserById(user.getId());
        userRepository.eraseInformation(user.getId());
        if (!verify(user)) {
            userRepository.closeConnections(oldInformation);
            return userRepository.updateUser(user);
        } else {
            User oldUser = new User();
            try {
                while (oldInformation.next()) {
                    oldUser.setId(oldInformation.getInt("id_users"));
                    oldUser.setUsername(oldInformation.getString("username"));
                    oldUser.setFirstName(oldInformation.getString("firstname"));
                    oldUser.setLastName(oldInformation.getString("lastname"));
                    oldUser.setEmail(oldInformation.getString("email"));
                    oldUser.setFk_roles(oldInformation.getInt("fk_roles"));
                }
                userRepository.closeConnections(oldInformation);
                userRepository.updateUser(oldUser);
            } catch (SQLException e) {
                userRepository.closeConnections(oldInformation);
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Hasher kodeord og opdaterer det for en bruger
     *
     */

    public boolean changePassword(String oldPassword, String newPassword, String newPasswordValidation, int userId) {
        if (newPassword.equals(newPasswordValidation)) {
            oldPassword = hashingService.hash(oldPassword);
            ResultSet resultSet = userRepository.verifyPassword(oldPassword, userId);
            try {
                if (resultSet.next()) {
                    userRepository.closeConnections(resultSet);
                    return userRepository.updatePassword(hashingService.hash(newPassword), userId);
                } else {
                    userRepository.closeConnections(resultSet);
                    return true;
                }
            } catch (SQLException e) {
                userRepository.closeConnections(resultSet);
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Prøver at finde en lignende bruger i databasen, hvis en bruger bliver oprettet eller opdateret
     *
     */

    @Override
    public boolean verify(User user) {
        ResultSet resultSet = userRepository.verifyUser(user);
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
