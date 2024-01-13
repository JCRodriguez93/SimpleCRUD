import static org.junit.jupiter.api.Assertions.*;

import org.company.DBPostgreFunctions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

public class DBPostgreFunctionsTest { //TODO: quedan tests por realizar

    private DBPostgreFunctions dbFunctions;
    private Connection conn;

    @BeforeEach
    public void setUp() {
        // Crea una nueva instancia de DBPostgreFunctions y una conexión antes de cada prueba
        dbFunctions = new DBPostgreFunctions();
        conn = dbFunctions.connectToDB("test", "userdb", "passdb");
        assertNotNull(conn, "Connection should not be null");
    }
    @AfterEach
    public void tearDown() {
        // Cierra la conexión después de cada prueba
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            fail("Exception thrown during teardown: " + e.getMessage());
        }
    }
    @DisplayName("Connection with DB Test")
    @Test
    public void testConnectToDB() {
        assertNotNull(conn, "Connection should not be null");
    }

    @DisplayName("Create Table Test")
    @Test
    public void testCreateTable() {

        dbFunctions.createTable(conn, "test_table");
        // Verificar si la tabla se ha creado exitosamente
        assertTrue(dbFunctions.tableExists(conn, "test_table"), "Table should exist");
        // Verificar que la tabla no existía previamente
        assertFalse(dbFunctions.tableExists(conn, "non_existing_table"),
                "Table should not exist");
    }

    @DisplayName("Insert Row Test")
    @Test
    public void testInsertRow() {
            // Crear la tabla
        dbFunctions.createTable(conn, "test_table");
            // Insertar una fila
        dbFunctions.insertRow(conn, "test_table", "John Doe", "123 Main St");
        // Verificar que la fila se ha insertado correctamente
        assertTrue(dbFunctions.rowExists(conn, "test_table", "name", "John Doe"), "Row should exist");
        assertTrue(dbFunctions.rowExists(conn, "test_table", "address", "123 Main St"), "Row should exist");

    }


    @DisplayName("Search by ID Test")
    @Test
    public void testSearchById() {
        // Crear la tabla
        dbFunctions.createTable(conn, "test_table");

        // Insertar algunas filas con diferentes identificadores
        dbFunctions.insertRow(conn, "test_table", "John Doe", "123 Main St");
        dbFunctions.insertRow(conn, "test_table", "Jane Doe", "456 Oak St");
        dbFunctions.insertRow(conn, "test_table", "Bob Smith", "789 Elm St");

        // Buscar por ID
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        dbFunctions.searchById(conn, "test_table", 2); // Buscar por ID 2
        System.setOut(System.out); // Restaurar la salida estándar

        // Verificar que la búsqueda por ID devuelve los resultados esperados
        String expectedOutput = "2";
        assertTrue(outputStream.toString().trim().contains(expectedOutput), "Search by ID should return the expected result");
    }

    @DisplayName("Search by Address Test")
    @Test
    public void testSearchByAddress() {
        // Crear la tabla
        dbFunctions.createTable(conn, "test_table");

        // Insertar algunas filas con diferentes direcciones
        dbFunctions.insertRow(conn, "test_table", "John Doe", "123 Main St");
        dbFunctions.insertRow(conn, "test_table", "Jane Doe", "456 Oak St");
        dbFunctions.insertRow(conn, "test_table", "Bob Smith", "789 Elm St");

        // Buscar por dirección
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        dbFunctions.searchByAddress(conn, "test_table", "123 Main St"); // Buscar por dirección "123 Main St"
        System.setOut(System.out); // Restaurar la salida estándar

        // Verificar que la búsqueda por dirección devuelve los resultados esperados
        String expectedOutput = "123 Main St";
        assertTrue(outputStream.toString().trim().contains(expectedOutput), "Search by address should return the expected result");
    }


    @DisplayName("Delete Table Test")
    @Test
    public void testDeleteTable() {
        // Crear la tabla
        dbFunctions.createTable(conn, "test_table");

        // Eliminar la tabla
        dbFunctions.deleteTable(conn, "test_table");

        // Verificar que la tabla ha sido eliminada correctamente
        assertFalse(dbFunctions.tableExists(conn, "test_table"), "Table should not exist");

        // Intentar eliminar la tabla nuevamente y verificar que no afecta
        dbFunctions.deleteTable(conn, "test_table");

        // Verificar que la tabla aún no existe después del intento de eliminación adicional
        assertFalse(dbFunctions.tableExists(conn, "test_table"), "Table should not exist after additional deletion attempt");
    }
}
