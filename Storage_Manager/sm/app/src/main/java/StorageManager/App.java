/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package StorageManager;

//implementation("com.sun.net.httpserver:http:20070405")

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import StorageManager.data_structures.*;
import StorageManager.Endpoints.*;
import StorageManager.storage_platforms.*;

public class App {

    public static void main(String[] args) {
        int port = 8080;
        if(args.length >= 1){
            port = Integer.valueOf(args[0]);
        }
        System.out.println("Resource Manager Running on port: " + port);

        HttpServer server;
        //MysqlApi mysqlapi = new MysqlApi();
        MeasurementController measurementController = new MeasurementController();
        SettingsController settingsController = new SettingsController();
        SyncIORequestLinkedList insertOneEntryQueue = new SyncIORequestLinkedList(0);
        SyncStringLinkedList commitAllEntryQueue = new SyncStringLinkedList(0);

        try{
            //initialize server
            server = HttpServer.create(new InetSocketAddress("0.0.0.0",port), 0);

            server.createContext("/insertone", new InsertOne(insertOneEntryQueue,measurementController,settingsController));
            server.createContext("/commitall", new CommitAll(commitAllEntryQueue,measurementController,settingsController));
            server.createContext("/measurements", new Measurements(measurementController,settingsController));
            server.createContext("/settings", new Settings(settingsController));
            server.createContext("/clear", new Clear(insertOneEntryQueue,commitAllEntryQueue,measurementController,settingsController));
            server.createContext("/test", new Test());
            server.setExecutor(null);

            //start Storage Manager
            StorageManager storageManager = new StorageManager(
                    settingsController,
                    insertOneEntryQueue,
                    commitAllEntryQueue,
                    measurementController
            );
            new Thread(storageManager).start();

            //start server
            server.start();

        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
