package ru.ifmo.web.standalone;

import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import ru.ifmo.web.service.UsersService;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

@Slf4j
public class App {
    public static void main(String... args) throws DatatypeConfigurationException, SQLException, IOException {
        String url = "http://0.0.0.0:8080/";

        ClassNamesResourceConfig config = new ClassNamesResourceConfig(UsersService.class);
        log.info("Creating server");
        HttpServer server = GrizzlyServerFactory.createHttpServer(url, config);
        log.info("Starting server");
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        log.info("Application started");
        while (true) {
        }
    }

}
