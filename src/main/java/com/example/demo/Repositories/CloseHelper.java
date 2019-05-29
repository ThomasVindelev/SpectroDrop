package com.example.demo.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface CloseHelper {

    void closeConnections(ResultSet resultSet);

    void closeConnections(PreparedStatement preparedStatement, Connection connection);

}
