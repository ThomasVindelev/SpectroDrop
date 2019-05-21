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
public class UserService implements com.example.demo.Services.Service<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EncryptionService encryptionService;

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

    public User getUserById(int id) {
        ResultSet resultSet = userRepository.getUserById(id);
        User user = new User();
        try {
            while (resultSet.next()) {
                user.setId(id);
                user.setUsername(resultSet.getString("username"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String newUser(User user) {
        if (!verify(user)) {
            user.setPassword(encryptionService.encrypt(user.getPassword()));
            if (!userRepository.newUser(user)) {
                return "Success!";
            } else {
                return "Failed!";
            }
        } else {
            return "Username or e-mail already exists!";
        }
    }

    public String updateUser(User user) {
        if (!userRepository.updateUser(user)) {
            return "Success!";
        } else {
            return "Failed!";
        }
    }

    public String deleteUserById(int id) {

        if (!userRepository.deleteUserById(id)) {
            return "Success!";
        } else {
            return "Failed!";
        }
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
