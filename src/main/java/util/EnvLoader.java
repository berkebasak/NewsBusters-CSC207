package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {

    private static final String ENV_FILE = ".env";
    private static final String ENV_EXAMPLE_FILE = ".env_example";

    private static Map<String, String> data;

    public static String get(String key) {
        if (data == null) {
            data = loadEnvVariables();
        }
        return data.get(key);
    }

    private static Map<String, String> loadEnvVariables() {
        Map<String, String> result = tryLoad(ENV_FILE);
        if (!result.isEmpty()) {
            return result;
        }

        result = tryLoad(ENV_EXAMPLE_FILE);
        if (!result.isEmpty()) {
            System.out.println("Loaded environment variables from " + ENV_EXAMPLE_FILE);
        } else {
            System.out.println("WARNING: No .env or .env_example found! No API keys loaded.");
        }

        return result;
    }

    private static Map<String, String> tryLoad(String filename) {
        Map<String, String> map = new HashMap<>();
        File file = new File(filename);

        if (!file.exists()) {
            return map;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int eq = line.indexOf("=");
                if (eq == -1) continue;

                String key = line.substring(0, eq).trim();
                String value = line.substring(eq + 1).trim();

                map.put(key, value);
            }

        } catch (IOException e) {
            System.out.println("Error reading " + filename + ": " + e.getMessage());
        }

        return map;
    }
}
