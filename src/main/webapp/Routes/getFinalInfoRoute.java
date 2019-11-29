package main.webapp.Routes;

import main.webapp.Model.Template;
import main.webapp.Model.TemplateReader;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * retrieves completed info, very last route called
 */
public class getFinalInfoRoute implements Route {
    private static final Logger LOG = Logger.getLogger(getFinalInfoRoute.class.getName());
    private FileHandler fh;


    public getFinalInfoRoute() {

        try{
            fh = new FileHandler("GetFinalInfoRouteLog.log");
            LOG.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            LOG.info("Created");
        } catch (Exception e) {

        }

        LOG.info("getFinalInfoRoute initialized");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        Template currentTemplate = request.session().attribute("template");

        LOG.info("Checking if exists template type: " + currentTemplate.getType());
        try {
            if (!TemplateReader.checkIfExists(currentTemplate.getType()) && currentTemplate.shouldSave()) {
                TemplateReader.addToDB(currentTemplate);
                LOG.info("Adding template \'" + currentTemplate.getType() + "\' to database");
            } else if(TemplateReader.checkIfExists(currentTemplate.getType())) {
                LOG.info("Templat: " + currentTemplate.getType() + " already exists in database");
            }
            else {
                return "Not all required fields were set";
            }
        } catch (Exception e) {
            LOG.info("Exception thrown when adding template to database");
        }

        try {
            LOG.info("Reading data from template: " + currentTemplate.getType());
            TemplateReader.readExistingTemplate(request.session().attribute("path").toString(),
                    currentTemplate.getType(),
                    response.raw().getOutputStream());
        } catch (Exception e) {
            LOG.info("Exception thrown when reading template");
        }

        LOG.info("GetFinalInfo completed successfully");

        return 1;
    }
}
