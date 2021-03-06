package games;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constants.DBConstants;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Games {

    public static final Map<String, MetaData> GAME_INFO;

    static {
        Map<String, MetaData> temp;
        Gson g = new Gson();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(DBConstants.RESOURCE_GAME_DB + DBConstants.GAME_INFO);
        if (is != null) {
            temp = g.fromJson(new InputStreamReader(is), new TypeToken<Map<String, MetaData>>(){}.getType());
        } else {
            try (FileReader fr = new FileReader(new File(DBConstants.GAME_DB + DBConstants.GAME_INFO))) {
                temp = g.fromJson(fr, new TypeToken<Map<String, MetaData>>(){}.getType());
            } catch (IOException e) {
                temp = new HashMap<>();
                System.out.println("Failed to load resource.");
            }
        }
        GAME_INFO = temp;
    }

    public static class MetaData {
        String id;
        String name;
        int max;
        String pkg;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public String getPkg() {
            return pkg;
        }

        public void setPkg(String pkg) {
            this.pkg = pkg;
        }
    }

    public static String getGameInfoJson() {
        Gson g = new Gson();
        return g.toJson(GAME_INFO);
    }
}
