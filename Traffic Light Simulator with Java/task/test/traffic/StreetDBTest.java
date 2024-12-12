package traffic;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreetDBTest {

    @Test
    void connect() throws SQLException {
        StreetDB streetDB = new StreetDB();
        streetDB.connect();
        streetDB.disconnect();
    }

    @Test
    void getParam() throws SQLException {
        StreetDB streetDB = new StreetDB();
        streetDB.connect();
        ParamDao paramDao = streetDB.selectParameters();
        streetDB.disconnect();
        assertEquals(10, paramDao.maxStreet);
        assertEquals(20, paramDao.Interval);
    }

    @Test
    void getStreet() throws SQLException {
        StreetDB streetDB = new StreetDB();
        streetDB.connect();
        List<StreetDao> street = streetDB.selectStreet();
        streetDB.disconnect();
        assertEquals(3, street.size());
    }

    @Test
    void insertStreet() throws SQLException {
        StreetDB streetDB = new StreetDB();
        streetDB.connect();
        int cnt = streetDB.insertStreet("A");
        streetDB.disconnect();
        assertEquals(1, cnt);
    }

    @Test
    void deleteStreet() throws SQLException {
        StreetDB streetDB = new StreetDB();
        streetDB.connect();
        streetDB.deleteStreet("A");
        streetDB.disconnect();
    }
}