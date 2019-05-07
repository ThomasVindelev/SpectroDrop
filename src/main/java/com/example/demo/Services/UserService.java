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

@Service
public class UserService implements com.example.demo.Services.Service<User> {

    @Autowired
    UserRepository userRepository;

    public List<User> getEmployees() {
        ResultSet resultSet = userRepository.getEmployees();
        List<User> employeeList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id_users"));
                user.setUsername(resultSet.getString("username"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setEmail(resultSet.getString("email"));
                user.setFk_roles(resultSet.getInt("fk_roles"));
                employeeList.add(user);
            }
            return employeeList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String newUser(User user) {
        if (!verify(user)) {
            if (!userRepository.newUser(user)) {
                return "Success!";
            } else {
                return "Failed!";
            }
        } else {
            return "Username or e-mail already exists!";
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
