package org.company;
import java.sql.*;


public class DBPostgreFunctions implements CRUDOperations {

    @Override
    public Connection connectToDB(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Not connected");
            }
        } catch (ClassNotFoundException | SQLException ex){
            handleException("Error connecting to the database", ex);
            throw new RuntimeException("Failed to connect to the database");

        }
        return conn;
    }

    /**
     * createTable.
     * Este método crea una tabla dándole nombre por parámetro.
     * La tabla tendrá un "empid", "name" y "address" de empleados.
     * @param conn
     * @param table_name
     */
    @Override
    public void createTable(Connection conn, String table_name) {
        try {
            // Verificar si la tabla ya existe
            if (tableExists(conn, table_name)) {
                System.out.println("Table " + table_name + " already exists");
            } else {
                // Si no existe, crear la tabla
                String createTableQuery = "CREATE TABLE " + table_name +
                        "(empid SERIAL, name varchar(200), address varchar(200), primary key (empid));";
                try (PreparedStatement stst = conn.prepareStatement(createTableQuery)) {
                    stst.execute();
                    System.out.println("Table " + table_name + " created");
                }
            }
        } catch (SQLException ex) {
            handleException("Error, imposible create table ", ex);

        }
    }

    /**
     * tableExists method.
     * Controla que la tabla de la base de datos exista.
     * @param conn
     * @param table_name
     * @return
     */
    public boolean tableExists(Connection conn, String table_name) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, table_name, null)) {
                return tables.next();
            }
        } catch (SQLException ex) {
            handleException("Error in method 'tableExists' ", ex);
            return false;
        }
    }

    /**
     * InsertRow method.
     * Inserta un registro a una tabla de la base de datos.
     * @param conn
     * @param table_name
     * @param name
     * @param address
     */
    @Override
    public void insertRow(Connection conn, String table_name, String name, String address) {
        try {
            String query = "INSERT INTO " + table_name + " (name, address) VALUES (?, ?);";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.executeUpdate();
            System.out.println("Rows added");
        } catch (SQLException ex) {
            handleException("Error inserting rows", ex);

        }
    }

    /**
     * readData method.
     * Lee los datos de una tabla.
     * @param conn
     * @param name_table
     */
    @Override
    public void readData(Connection conn, String name_table) {
        try {
            String query = "SELECT * FROM " + name_table;
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("-------------------------------------------------");
                System.out.println("EMPID: "+rs.getString("empid") + "     "
                        +"NAME: "+rs.getString("name") + "     "
                        +"ADDRESS: "+rs.getString("address") + " ");

            }
        } catch (SQLException ex) {
            handleException("Error reading data ", ex);

        }
    }

    /**
     * UpdateName method.
     * Actualiza el nombre de un registro de la base de datos.
     * @param conn
     * @param name_table
     * @param old_name
     * @param new_name
     */
    @Override
    public void updateName(Connection conn, String name_table, String old_name, String new_name) {
        try {
            String query = "UPDATE " + name_table + " SET name=? WHERE name=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, new_name);
            stmt.setString(2, old_name);
            stmt.executeUpdate();
            System.out.println("Updated");
        } catch (SQLException ex) {
            handleException("Error updating name ", ex);

        }
    }

    /**
     * SearchById method.
     * Busca un registro por el id "empid"
     * @param conn
     * @param name_table
     * @param id
     */
    @Override
    public void searchById(Connection conn, String name_table, int id) {
        try {
            String query = "SELECT * FROM " + name_table + " WHERE empid=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("EMPID: "+rs.getString("empid") + "     "+
                        "NAME: "+rs.getString("name") + "     "+
                        "ADDRESS: "+rs.getString("address") + " ");
            }
        } catch (SQLException ex) {
            handleException("Error in search by id ", ex);

        }
    }

    /**
     * SearchByName method.
     * Busca un registro por el nombre de empleado.
     * @param conn
     * @param name_table
     * @param name
     */
    @Override
    public void searchByName(Connection conn, String name_table, String name) {
        try {
            String query = "SELECT * FROM " + name_table + " WHERE name=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("EMPID: "+rs.getString("empid") + "     "+
                        "NAME: "+rs.getString("name") + "     "+
                        "ADDRESS: "+rs.getString("address") + " ");
            }
        } catch (SQLException ex) {
            handleException("Error in search by name ", ex);

        }
    }

    /**
     * SearchByAddress method.
     * Busca un registro por dirección del empleado.
     * @param conn
     * @param name_table
     * @param address
     */
    @Override
    public void searchByAddress(Connection conn, String name_table, String address) {
        try {
            String query = "SELECT * FROM " + name_table + " WHERE address=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, address);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("EMPID: "+rs.getString("empid") + "     "+
                        "NAME: "+rs.getString("name") + "     "+
                        "ADDRESS: "+rs.getString("address") + " ");
            }
        } catch (SQLException ex) {
            handleException("Error in search by address ", ex);

        }
    }

    /**
     * DeleteRowByName method.
     * Borra un registro por el nombre de empleado.
     * @param conn
     * @param name_table
     * @param name
     */
    @Override
    public void deleteRowByName(Connection conn, String name_table, String name) {
        try {
            // Preparar la consulta de eliminación
            String query = "DELETE FROM " + name_table + " WHERE name=?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Establecer el valor del parámetro en la consulta
            stmt.setString(1, name);

            // Ejecutar la eliminación y obtener el número de filas afectadas
            int rowsDeleted = stmt.executeUpdate();

            // Imprimir un mensaje indicando cuántas filas fueron borradas
            System.out.println("Deleted " + rowsDeleted + " row(s)");
        } catch (SQLException ex) {
            // Manejar cualquier excepción que pueda ocurrir durante la eliminación
            handleException("Error deleting rows by name ", ex);
    }}


    /**
     * DeleteRowById method.
     * Borra un registro que contenga un "empid" dado.
     * @param conn
     * @param name_table
     * @param id
     */
    @Override
    public void deleteRowByID(Connection conn, String name_table, int id) {
        try {
            // Preparar la consulta de eliminación
            String query = "DELETE FROM " + name_table + " WHERE empid=?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Establecer el valor del parámetro en la consulta
            stmt.setInt(1, id);

            // Ejecutar la eliminación y obtener el número de filas afectadas
            int rowsDeleted = stmt.executeUpdate();

            // Imprimir un mensaje indicando cuántas filas fueron borradas
            System.out.println("Deleted " + rowsDeleted + " row(s)");
        } catch (SQLException ex) {
            handleException("Error deleting rows by id ", ex);
        }
    }

    /**
     * RowExists method.
     * Método auxiliar para comprobar que un registro existe.
     * @param conn
     * @param table_name
     * @param columnName
     * @param columnValue
     * @return
     */
    public boolean rowExists(Connection conn, String table_name, String columnName, String columnValue) {
        try {
            String query = "SELECT * FROM " + table_name + " WHERE " + columnName + "=?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, columnValue);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException ex) {
            handleException("Error in method rowExists ", ex);
        }
        return false;
    }


    /**
     * DeleteTable method.
     * borra la tabla de la base de datos.
     * @param conn
     * @param name_table
     */
    @Override
    public void deleteTable(Connection conn, String name_table) {
        try {
            // Preparar la consulta para eliminar la tabla
            String query = "DROP TABLE " + name_table;
            PreparedStatement stmt = conn.prepareStatement(query);

            // Ejecutar la eliminación de la tabla
            stmt.executeUpdate();

            // Si no se produce ninguna excepción, se considera que la tabla fue eliminada
            System.out.println("Table deleted");
        } catch (SQLException ex) {
            // Manejar cualquier excepción que pueda ocurrir durante la eliminación de la tabla
            handleException("Error deleting table ", ex);

        }
    }

    /**
     * handleException.
     * Excepción personalizada para mostrar un mensaje acorde al evento producido.
     * @param message
     * @param ex
     */
    private void handleException(String message, Exception ex) {
        System.err.println(message + ": " + ex.getMessage());
        ex.printStackTrace();
    }
}
