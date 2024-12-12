package traffic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StreetDB {
    private Connection con = null;
    private Statement statement = null;
    private static StreetDB INSTANCE = null;

    public static StreetDB getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new StreetDB();
        }
        return INSTANCE;
    }

    private StreetDB() {
        try {
            con = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/Roads", "postgres", "password");
            statement = con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }

    }

    public ParamDao selectParameters() {
        ParamDao paramDao = new ParamDao();
        try {
            String sql = "select * from param";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (id == 1) {
                    paramDao.maxStreet = resultSet.getInt("value");
                } else {
                    paramDao.Interval = resultSet.getInt("value");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }

        return paramDao;
    }

    public List<StreetDao> selectStreet()  {
        List<StreetDao> streetList = new ArrayList<>();
        try {
            String sql = "select s_name from street";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                StreetDao street = new StreetDao();
                street.name = resultSet.getString("s_name");
                streetList.add(street);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }

        return streetList;
    }

    public int insertStreet(String name) {
        try {
            String insertSql = "insert into street(s_name) values (?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertSql);
            preparedStatement.setString(1, name);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }
    }

    public void deleteStreet(String name) {
        try {
            String deleteSql = "delete from street where s_name = ?";
            PreparedStatement preparedStatement = con.prepareStatement(deleteSql);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }
    }

    public int countRoad() {
        try {
            String countSql = "select count(*) cnt from street s";
            ResultSet resultSet = statement.executeQuery(countSql);
            resultSet.next();
            return resultSet.getInt("cnt");
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }

    }

    public void disconnect() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception");
        }
    }
}

