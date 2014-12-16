import javax.json.*;
import java.io.*;
import java.net.URL;

public class GoEuroTest {

    private static final String JSON_URL = "http://api.goeuro.com/api/v2/position/suggest/en/";
    private static final String CSV_FILENAME = "cities.csv";

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java -jar GoEuroTest \"STRING\"");
            return;
        }

        String arg = args[0].trim().replaceAll(" ", "");

        InputStream fis = null;
        try {
            fis = new URL(JSON_URL + arg).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JsonReader jsonReader = Json.createReader(fis);
        JsonArray jsonData = jsonReader.readArray();
        jsonReader.close();

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(CSV_FILENAME);

            writer.append("_id");
            writer.append(';');
            writer.append("name");
            writer.append(';');
            writer.append("type");
            writer.append(';');
            writer.append("latitude");
            writer.append(';');
            writer.append("longitude");
            writer.append('\n');

            for (JsonValue jsonValue : jsonData) {
                JsonNumber _id = ((JsonObject) jsonValue).getJsonNumber("_id");
                if (_id != null) writer.append(_id.toString());
                writer.append(';');
                JsonString name = ((JsonObject) jsonValue).getJsonString("name");
                if (name != null) writer.append(name.toString());
                writer.append(';');
                JsonString type = ((JsonObject) jsonValue).getJsonString("type");
                if (type != null) writer.append(type.toString());
                writer.append(';');
                JsonObject position = ((JsonObject) jsonValue).getJsonObject("geo_position");
                if (position != null) {
                    JsonNumber latitude = position.getJsonNumber("latitude");
                    if (latitude != null) writer.append(latitude.toString());
                    writer.append(';');
                    JsonNumber longitude = position.getJsonNumber("longitude");
                    if (longitude != null) writer.append(longitude.toString());
                } else {
                    writer.append(';');
                }
                writer.append('\n');
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
