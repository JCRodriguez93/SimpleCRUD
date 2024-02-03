package org.company;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        DBPostgreFunctions dbFunctions = new DBPostgreFunctions();
        // Intentar establecer la conexión
        Connection conn = dbFunctions.connectToDB("test", "userdb", "passdb");
        // Verificar si la conexión se ha realizado con éxito
        if (conn != null) {
            System.out.println("Connection established");
        // Hacer otras cosas si fuese necesario
        } else {
            System.out.println("Connection failed. Unable to proceed.");
        }


    }
}