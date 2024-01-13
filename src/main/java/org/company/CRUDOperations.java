package org.company;
import java.sql.Connection;


public interface CRUDOperations {
    Connection connectToDB(String dbname, String user, String pass);

    void createTable(Connection conn, String table_name);

    void insertRow(Connection conn, String table_name, String name, String address);

    void readData(Connection conn, String name_table);

    void updateName(Connection conn, String name_table, String old_name, String new_name);

    void searchById(Connection conn, String name_table, int id);

    void searchByName(Connection conn, String name_table, String name);

    void searchByAddress(Connection conn, String name_table, String address);

    void deleteRowByName(Connection conn, String name_table, String name);

    void deleteRowByID(Connection conn, String name_table, int id);

    void deleteTable(Connection conn, String name_table);
}


