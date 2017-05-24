package nl.choutman.allinn.koos.vertx.cors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choutman on 24/05/2017.
 */
public class CORS {
    private static final List<String> allowedOrigins;

    static {
        allowedOrigins = new ArrayList<>();

        allowedOrigins.add("http://localhost:8000");
        allowedOrigins.add("http://localhost:9000");
    }

    public static boolean isAllowed(String origin) {
        return allowedOrigins.contains(origin);
    }
}
