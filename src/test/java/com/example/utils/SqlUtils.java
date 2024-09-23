package com.example.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqlUtils {
    public static void executeSql(String resourcePath, Connection connection) throws URISyntaxException, IOException, SQLException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourcePath);
        Path path = Paths.get(url.toURI());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        try (Statement statement
                     = connection.createStatement();) {

            System.out.println("Executing commands at : "
                               + path);

            StringBuilder builder = new StringBuilder();

            int lineNumber = 0;
            int count = 0;

            // Read lines from the SQL file until the end of the
            // file is reached.
            for (String line : lines) {
                lineNumber += 1;
                line = line.trim();

                // Skip empty lines and single-line comments.
                if (line.isEmpty() || line.startsWith("--"))
                    continue;

                builder.append(line);
                // If the line ends with a semicolon, it
                // indicates the end of an SQL command.
                if (line.endsWith(";"))
                    try {
                        // Execute the SQL command
                        statement.execute(builder.toString());
                        // Print a success message along with
                        // the first 15 characters of the
                        // executed command.
                        System.out.println(
                                ++count
                                + " Command successfully executed : "
                                + builder.substring(
                                        0,
                                        Math.min(builder.length(), 15))
                                + "...");
                        builder.setLength(0);
                    } catch (SQLException e) {
                        // If an SQLException occurs during
                        // execution, print an error message and
                        // stop further execution.
                        System.err.println(
                                "At line " + lineNumber + " : "
                                + e.getMessage() + "\n");
                        return;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, URISyntaxException, IOException {
        final String DB_SCRIPT_PATH = "db_script.sql";
        executeSql(DB_SCRIPT_PATH, null);
    }
}
