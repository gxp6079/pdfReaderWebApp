package main.webapp;

import main.webapp.Model.DataBaseConnection;
import main.webapp.Model.TableFactory;
import main.webapp.Routes.*;
import spark.servlet.SparkApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application implements SparkApplication {

    private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

    public static final Logger START_END_LOGGER = Logger.getLogger(postStartEndRoute.class.getName());
    public static FileHandler start_end_fh;

    public static final Logger MULTIPLE_LOG = Logger.getLogger(postMultipleInstancesRoute.class.getName());
    public static FileHandler multiple_inst_fh;

    public static final String TEMPLATE_URL = "/PDFreader";

    public static final String TABLE_INFO_URL = "/tableInfo";

    public static final String START_END_URL = "/startEnd";

    public static final String MULTIPLE_INSTANCE_URL = "/multi";

    public static final String FINAL_INFO = "/finalInfo";

    public static final String SIGN_IN = "/signIn";

    public static final String EXIT = "/exit";

    public static void main(String[] args) {

        createDatabaseTable();

        LOG.config("Initialization Complete");
    }

    @Override
    public void destroy() {

        multiple_inst_fh.close();
        LOG.info("Closed multiple instance route file handler");
        start_end_fh.close();
        LOG.info("Closed start end route file handler");
        getFinalInfoRoute.fh.close();
        LOG.info("Closed finalInfo route file handler");
        getTableInfoRoute.fh.close();
        LOG.info("Closed getTableInfoRoute route file handler");
        getUserExitRoute.fh.close();
        LOG.info("Closed getUserExitRoute route file handler");
        postTableInfoRoute.fh.close();
        LOG.info("Closed postTableInfoRoute route file handler");
        postTemplateRoute.fh.close();
        LOG.info("Closed postTemplateRoute route file handler");
        TableFactory.fh.close();
        LOG.info("Closed TableFactory file handler");


    }

    @Override
    public void init() {
        initLogs();

        get(FINAL_INFO, new getFinalInfoRoute());

        get(MULTIPLE_INSTANCE_URL, new getMultipleInstancesRoute());

        get(TABLE_INFO_URL, new getTableInfoRoute());

        get(EXIT, new getUserExitRoute());

        post(MULTIPLE_INSTANCE_URL, new postMultipleInstancesRoute(MULTIPLE_LOG));

        post(START_END_URL, new postStartEndRoute(START_END_LOGGER));

        post(TABLE_INFO_URL, new postTableInfoRoute());

        post(TEMPLATE_URL, "multipart/form-data", new postTemplateRoute());

        post(SIGN_IN, new postSignInRoute());

        LOG.finer("WebServer Initialized");
    }

    public static void createDatabaseTable() {
        String databaseUrl = DataBaseConnection.DATABASE_IP;

        try {
            Connection connectionSource = DriverManager.getConnection(databaseUrl, "brit", "x0EspnYA8JaqCPT9");
            Statement s = connectionSource.createStatement();
            //int Result=s.executeUpdate("CREATE DATABASE PDFreader");
            String table = "CREATE TABLE IF NOT EXISTS `TEMPLATES` (\n" +
                    "`template_type` varchar(50) NOT NULL,\n" +
                    "`template_object` blob,\n" +
                    "PRIMARY KEY (`template_type`)\n" +
                    ")";
            int Result=s.executeUpdate(table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initLogs() {


        try{
            start_end_fh = new FileHandler("pdfReaderLogFiles/PostStartEndRouteLog.log");
            START_END_LOGGER.addHandler(start_end_fh);
            SimpleFormatter formatter = new SimpleFormatter();
            start_end_fh.setFormatter(formatter);
            START_END_LOGGER.info("Created");
        } catch (Exception e) {}


        try{
            multiple_inst_fh = new FileHandler("pdfReaderLogFiles/PostMultipleInstance.log");
            MULTIPLE_LOG.addHandler(multiple_inst_fh);
            SimpleFormatter formatter = new SimpleFormatter();
            multiple_inst_fh.setFormatter(formatter);
            MULTIPLE_LOG.info("Created");
        } catch (Exception e) {}


    }
}
