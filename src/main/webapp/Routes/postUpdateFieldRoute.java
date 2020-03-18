package main.webapp.Routes;

import main.webapp.Application;
import main.webapp.Model.DataBaseConnection;
import main.webapp.Model.Field;
import main.webapp.Model.Token;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class postUpdateFieldRoute implements Route {
    private static final Logger LOG = Logger.getLogger(postUpdateFieldRoute.class.getName());
    public static FileHandler fh;

    public postUpdateFieldRoute(){
        try{
            fh = new FileHandler("pdfReaderLogFiles/PostUpdateFieldRoute.log");
            LOG.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOG.info("Update logger created");
        }
        catch (Exception e){
            LOG.info("Failed to initialize logger");
        }
        LOG.info("postUpdateFieldRoute initialized");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String key = request.queryParams("key");
        String tableId = request.queryParams("tableId");
        String value = request.queryParams("value");
        String id = request.queryParams("token");
        String fieldName = request.queryParams("fieldName");

        Token token = Application.getToken(id, request);
        LOG.info("trying to get field");
        Field toUpdate = token.getTemplate().getFields().get(fieldName.hashCode() + tableId.hashCode());
        LOG.info("got field: " + toUpdate);

        toUpdate.addTranslation(key, value);
        LOG.info("Added translation from: " + key + " to: " + value);

        try {
            if(DataBaseConnection.checkIfObjExists(token.getTemplate().getType(), token.getInstitutionId())) {
                DataBaseConnection.updateTemplateInDB(token.getInstitutionId(), token.getTemplate());
                LOG.info("DataBase updated");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }
}
