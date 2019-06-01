package com.example.demo.Repositories;

import com.example.demo.Models.User;
import org.springframework.stereotype.Repository;

import java.sql.*;

//Lavet af Marco Pedersen og Thomas Vindelev

@Repository
public class UserRepository implements CloseHelper {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private String query;
    private boolean isError;

    /**
     * Tillader reetablering af connection til vores database efter denne bliver lukket.
     */


    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://spectrodb.cbiha888el7r.eu-central-1.rds.amazonaws.com/SpectroDB?useSSL=false&autoReconnect=true",
                    "SpectroDB",
                    "SpectroDB");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean newUser(User user) {
        connection = getConnection();
        query = "INSERT INTO SpectroDB.Users " +
                "(username, password, firstname, lastname, email, fk_roles) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setInt(6, user.getFk_roles());
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return true;
    }

    public ResultSet verifyUserLogin(User user) {
        connection = getConnection();
        query = "SELECT id_users, username, firstname, lastname, email, " +
                "id_roles, is_active FROM SpectroDB.Users " +
                "INNER JOIN Roles ON Users.fk_roles = Roles.id_roles " +
                "WHERE LOWER (username) = LOWER (?) AND password = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getUsers(boolean getAll) {
        connection = getConnection();
        if (getAll) {
            query = "SELECT id_users, username, firstname, lastname, email, " +
                    "Roles.role, Users.fk_roles FROM SpectroDB.Users " +
                    "INNER JOIN Roles ON Users.fk_roles = Roles.id_roles";
        } else {
            query = "SELECT id_users, username, firstname, lastname, email, " +
                    "Roles.role, Users.fk_roles FROM SpectroDB.Users " +
                    "INNER JOIN Roles ON Users.fk_roles = Roles.id_roles " +
                    "ORDER BY id_users DESC LIMIT 3";
        }
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getUserById(int id) {
        connection = getConnection();
        query = "SELECT id_users, username, firstname, lastname, email, fk_roles " +
                "FROM SpectroDB.Users WHERE id_users = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getUsersByRole(int roleId) {
        connection = getConnection();
        query = "SELECT id_users, username, firstname, lastname, email, Roles.role, Users.fk_roles " +
                "FROM SpectroDB.Users INNER JOIN Roles ON Users.fk_roles = Roles.id_roles WHERE fk_roles = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, roleId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getRoles() {
        connection = getConnection();
        query = "SELECT * FROM SpectroDB.Roles";
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user) {
        connection = getConnection();
        query = "UPDATE SpectroDB.Users SET username = ?, firstname = ?, lastname = ?, email = ?, fk_roles = ? WHERE id_users = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getFk_roles());
            preparedStatement.setInt(6, user.getId());
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteUserById(int id) {
        connection = getConnection();
        query = "DELETE FROM SpectroDB.Users WHERE id_users = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return true;
    }

    public ResultSet verifyUser(User user) {
        connection = getConnection();
        query = "SELECT username FROM SpectroDB.Users WHERE LOWER (username) = LOWER (?) OR email = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public boolean eraseInformation(int id) {
        connection = getConnection();
        query = "UPDATE SpectroDB.Users SET username = null, firstname = null, lastname = null, email = null, fk_roles = null WHERE id_users = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return true;
    }

    public boolean activateUser(String password, int id) {
        connection = getConnection();
        query = "UPDATE SpectroDB.Users SET password = ?, is_active = ? WHERE id_users = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, id);
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String newPassword, int userId) {
        connection = getConnection();
        query = "UPDATE SpectroDB.Users SET password = ? WHERE id_users = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet verifyPassword(String oldPassword, int userId) {
        connection = getConnection();
        query = "SELECT username FROM SpectroDB.Users WHERE password = ? AND id_users = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, oldPassword);
            preparedStatement.setInt(2, userId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tjekker om SQL-elementer er åbne, hvorefter disse lukkes
     */

    //https://stackoverflow.com/questions/2225221/closing-database-connections-in-java

    @Override
    public void closeConnections(PreparedStatement preparedStatement, Connection connection) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnections(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.preparedStatement != null) {
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}