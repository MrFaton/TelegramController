package com.mr_faton.core.dao;

import com.mr_faton.core.entity.DBServer;
import com.mr_faton.core.entity.Telegram;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Calendar;

public class TelegramControllerDAO {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.dao.TelegramControllerDAO");
    private static final String SQL = "SELECT title FROM brizdb.tabl_prin_soob WHERE n_title = ? AND date_time >= ?;";

    public TelegramControllerDAO() throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.warn("JDBC Driver class not found", e);
            throw e;
        }
    }

    public boolean isTelegramExist(Telegram telegram, DBServer dbServer) throws SQLException {
        logger.debug("check telegram " + telegram + " on db server " + dbServer);
        boolean found = false;

        final String jdbcURL = dbServer.getUrl();
        final String user = dbServer.getUser();
        final String password = dbServer.getUserPassword();

        final String header = telegram.getHeader();
        final String digitalHeader = telegram.getDigitalHeader();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -(telegram.getSearchDepth()));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

//        /*TODO remove this in future (because time on server less on 3 hour then time on this machine)*/
//        calendar.add(Calendar.HOUR_OF_DAY, -3);

        final Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        Connection connection = DriverManager.getConnection(jdbcURL, user, password);
        PreparedStatement preparedStatement = connection.prepareStatement(SQL);

        preparedStatement.setString(1, digitalHeader);
        preparedStatement.setTimestamp(2, timestamp);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String extractedHeader = koi8Decoder(resultSet.getBytes("title"));
            if (header.equals(extractedHeader)) {
                found = true;
                break;
            }
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return found;
    }

    private String koi8Decoder(byte[] bytes) {
        try {
            return new String(bytes, "KOI8-R");
//            return new String(bytes, "UTF8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("unsupported encoding!", e);
        }
        return "";
    }
}
