package com.example.demo.Repositories;

import com.example.demo.Models.Task;
import org.springframework.stereotype.Repository;

import java.sql.*;

//Lavet af Thomas Vindelev

@Repository
public class TaskRepository implements CloseHelper {

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

    public boolean newTask(Task task) {
        connection = getConnection();
        query = "INSERT INTO Tasks (fk_customer, fk_employee, name) VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, task.getFk_customer());
            preparedStatement.setInt(2, task.getFk_employee());
            preparedStatement.setString(3, task.getTaskName());
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return true;
    }

    public boolean editTask(Task task) {
        connection = getConnection();
        query = "UPDATE SpectroDB.Tasks SET fk_customer = ?, fk_employee = ?, fk_status = ?, name = ? WHERE id_tasks = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, task.getFk_customer());
            preparedStatement.setInt(2, task.getFk_employee());
            preparedStatement.setInt(3, task.getFk_status());
            preparedStatement.setString(4, task.getTaskName());
            preparedStatement.setInt(5, task.getId());
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteTask(int id) {
        connection = getConnection();
        query = "DELETE FROM SpectroDB.Tasks WHERE id_tasks = ?";
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

    public void deleteTaskByUser(int id) {
        connection = getConnection();
        query = "DELETE FROM SpectroDB.Tasks WHERE fk_customer = ? OR fk_employee = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
            closeConnections(preparedStatement, connection);
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
    }

    public ResultSet getTasks(boolean getNew, boolean isCustomer, int id) {
        connection = getConnection();
        query = "SELECT id_tasks, fk_customer, fk_employee, customer.username, " +
                "employee.username, fk_status, Status.name, Tasks.name FROM Tasks " +
                "INNER JOIN Users customer ON fk_customer = customer.id_users " +
                "INNER JOIN Users employee ON fk_employee = employee.id_users " +
                "INNER JOIN Status ON fk_status = id_status ";
        if (isCustomer) {
            query += "WHERE fk_customer = " + id + " ";
        } else if (getNew) {
            query += "ORDER BY id_tasks DESC LIMIT 5";
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


    public ResultSet getTaskById(int id) {
        connection = getConnection();
        query = "SELECT id_tasks, customer.username, " +
                "employee.username, fk_status, Status.name, Tasks.name FROM Tasks " +
                "INNER JOIN Users customer ON fk_customer = customer.id_users " +
                "INNER JOIN Users employee ON fk_employee = employee.id_users " +
                "INNER JOIN Status ON fk_status = id_status WHERE id_tasks = ?";
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

    public ResultSet getTaskByUser(int id) {
        connection = getConnection();
        query = "SELECT id_tasks From SpectroDB.Tasks WHERE fk_customer = ? OR fk_employee = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getStatus() {
        connection = getConnection();
        query = "SELECT * FROM SpectroDB.Status";
        try {
            preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return null;
    }

    public boolean autoTransferResponsibility(int userId) {
        connection = getConnection();
        query = "UPDATE SpectroDB.Tasks SET fk_employee = ? WHERE fk_employee = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, userId);
            isError = preparedStatement.execute();
            closeConnections(preparedStatement, connection);
            return isError;
        } catch (SQLException e) {
            closeConnections(preparedStatement, connection);
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Tjekker om SQL-elementer er åbne, hvorefter disse lukkes
     *
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