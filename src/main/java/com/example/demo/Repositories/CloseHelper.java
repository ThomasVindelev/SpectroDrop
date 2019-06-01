package com.example.demo.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Et interface som alle repositories implementere med det formål at lukke åbne forbindelser ved preparedStatements og resultSets
 */

//Lavet af Thomas Vindelev

public interface CloseHelper {


    void closeConnections(ResultSet resultSet);

    void closeConnections(PreparedStatement preparedStatement, Connection connection);

}
