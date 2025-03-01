package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import com.tictactoe.InitServlet;
import com.tictactoe.LogicServlet;
import com.tictactoe.RestartServlet;
import java.io.File;

public class EmbeddedTomcat {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);

        String webappDirLocation = new File("src/main/webapp/").getAbsolutePath();
        File webAppDir = new File(webappDirLocation);

        if (!webAppDir.exists()) {
            System.err.println("Error: The webapp folder was not found! Path: " + webAppDir.getAbsolutePath());
            return;
        }

        Context context = tomcat.addWebapp("", webAppDir.getAbsolutePath());

        if (context == null) {
            System.err.println("Error: Tomcat context was not created! There may be an issue with webapp.");
            return;
        }

        Tomcat.addServlet(context, "InitServlet", new InitServlet());
        Tomcat.addServlet(context, "LogicServlet", new LogicServlet());
        Tomcat.addServlet(context, "RestartServlet", new RestartServlet());

        context.addServletMappingDecoded("/start", "InitServlet");
        context.addServletMappingDecoded("/logic", "LogicServlet");
        context.addServletMappingDecoded("/restart", "RestartServlet");

        System.out.println("Starting Tomcat. Open in your browser: http://localhost:8081/start");

        tomcat.getConnector();

        tomcat.start();
        tomcat.getServer().await();
    }
}

