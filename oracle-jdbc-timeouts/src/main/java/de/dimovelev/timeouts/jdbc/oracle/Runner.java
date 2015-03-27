package de.dimovelev.timeouts.jdbc.oracle;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Runner {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java -jar oracle-jdbc-timeouts*.jar <config.properties>");
            System.exit(1);
        }
        final String filename = args[0];

        final Properties props = new Properties();
        props.load(new FileReader(filename));
        final String url = props.getProperty("url");
        final String sql = props.getProperty("sql");
        final int retries = Integer.parseInt(props.getProperty("retries"));

        final int queryTimeout = Integer.parseInt(props.getProperty("queryTimeout"));

        DriverManager.getConnection(url, props);
        System.out.println("Connecting to " + url);
        try(Connection connection = DriverManager.getConnection(url, props)) {
            System.out.println("Connected");
            try(Statement statement = connection.createStatement()) {
                System.out.println("Created statement");
                if (queryTimeout > 0) {
                    System.out.println("Setting the query timeout to " + queryTimeout);
                    statement.setQueryTimeout(queryTimeout);
                }
                for (int i=0; i<retries; i++) {
                    try {
                        System.out.println("Executing query #" + (i+1));
                        try (ResultSet rs = statement.executeQuery(sql)) {
                            while (rs.next()) {
                                System.out.println("Read from result result " + rs.getInt(1));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Caught exception: " + e.getMessage());
                    }
                }
            }
        }
    }
}
