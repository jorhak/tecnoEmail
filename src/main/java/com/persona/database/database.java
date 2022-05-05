package com.persona.database;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class database {

    private static final String host = "mail.tecnoweb.org.bo";
    private static final String database = "db_agenda";
    private static final String user = "agenda";
    private static final String password = "agendaagenda";
    private static final int port = 5432;

    private Connection con;

    private static database instance;

    private database() {
        connect();
    }

    public static database getInstance() {
        if (instance == null) {
            instance = new database();
        }

        return instance;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + host + ":" + port + "/" +
                    database;
            con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(true);
            System.out.println("conexion realizada con exito");
        } catch (ClassNotFoundException | SQLException e) {
            con = null;
            System.out.println("conexion fallida idiota::: " + e);
        }
    }

    public Connection getConnection() {
        if (con == null) {
            connect();
        }
        return con;
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
        }
        con = null;
    }

    public boolean executeSQL(String sql) {
        try (Statement consulta = con.createStatement();) {
            consulta.executeUpdate(sql);
        } catch (SQLException ex) {
            con = null;
            connect();
            return false;
        }
        return true;
    }

    public List<Map<String, String>> executeSQLResultList(String sql) {
        List<Map<String, String>> data = new LinkedList<>();
        try (PreparedStatement consulta = con.prepareStatement(sql);
                ResultSet resultado = consulta.executeQuery();) {
            String columnNames[] = columnName(resultado);
            while (resultado.next()) {
                Map<String, String> map = new HashMap<>();
                for (int index = 0; index < columnNames.length; index++) {
                    map.put(columnNames[index], resultado.getObject(index + 1).
                            toString());
                }
                data.add(map);
            }
        } catch (Exception e) {
            con = null;
            connect();
        }
        return data;
    }

    public boolean executeSQL(PreparedStatement preparedStatement) {
        int state = 0;
        try {
            state = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            con = null;
            connect();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
            }
        }
        return state != 0;
    }

    public boolean delete(String sql, Object... primaryKey) {
        boolean proccessed = false;
        try (PreparedStatement statement = getInstance().getConnection().
                prepareStatement(sql);) {
            for (int index = 0; index < primaryKey.length; index++) {
                statement.setString(index + 1, primaryKey[index].toString());
            }
            proccessed = getInstance().executeSQL(statement);
        } catch (SQLException e) {
            con = null;
        }
        return proccessed;
    }

    public List<Map<String, Object>> executeSQLResultList(
            PreparedStatement preparedStatement) {
        List<Map<String, Object>> data = new LinkedList<>();
        try (ResultSet resultado = preparedStatement.executeQuery();) {
            String columnNames[] = columnName(resultado);
            while (resultado.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int index = 0; index < columnNames.length; index++) {
                    map.put(columnNames[index], resultado.getObject(index + 1));
                }
                data.add(map);
            }
        } catch (Exception e) {
            con = null;
            connect();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
            }
        }
        return data;
    }

    public String[] columnName(ResultSet resultSet) {
        String columns[] = null;
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            columns = new String[columnCount];
            for (int index = 0; index < columnCount; index++) {
                columns[index] = metaData.getColumnName(index + 1);
            }
        } catch (SQLException e) {
            con = null;
            connect();
        }
        return columns;
    }

    public static void main(String[] args) {
        String sql = "select per_nom from persona where per_nom like '%E%'";
        List<Map<String,String>> r = getInstance().executeSQLResultList(sql);
        for (int i = 0; i < r.size(); i++) {
            Map<String,String> row = r.get(i);
            System.out.println(row.getOrDefault("per_nom", ""));
        }
    }

}
