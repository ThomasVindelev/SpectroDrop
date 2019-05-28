package com.example.demo.Services;

import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repositories.FileRepository;
import com.example.demo.Repositories.TaskRepository;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements Users<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashingService hashingService;

    @Autowired
    private TaskService taskService;

    // Henter brugere efter roller

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
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean newUser(User user) {
        if (!verify(user)) {
            user.setPassword(hashingService.hash(user.getPassword()));
            return userRepository.newUser(user);
        } else {
            return true;
        }
    }

    public boolean deleteUserById(int userId, int roleId) {
        if (userId == 1) {
            return true;
        } else if (!taskService.taskResponsibility(userId, roleId)) {
            return userRepository.deleteUserById(userId);
        }
        return true;
    }

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
            return roleList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verifyUpdate(User user) {
        ResultSet oldInformation = userRepository.getUserById(user.getId());
        userRepository.eraseInformation(user.getId());
        if (!verify(user)) {
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
                userRepository.updateUser(oldUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword, String newPasswordValidation, int userId) {
        if (newPassword.equals(newPasswordValidation)) {
            System.out.println(hashingService.hash(oldPassword));
            oldPassword = hashingService.hash(oldPassword);
            ResultSet resultSet = userRepository.verifyPassword(oldPassword, userId);
            System.out.println(hashingService.hash(newPassword));
            try {
                if (resultSet.next()) {
                    return userRepository.updatePassword(hashingService.hash(newPassword), userId);
                } else {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //Til at oprette en ny bruger

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
