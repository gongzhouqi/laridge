package POC;

import com.google.gson.Gson;
import models.RoomWaitModel;
import services2.models.Model;
import services2.models.RoomConnectModel;

import java.lang.reflect.Type;

import static constants.CoreConstants.PARAMETERS;

public class GsonPOC {

    public static void main (String[] args) throws ClassNotFoundException {
        Gson g = new Gson();
        RoomConnectModel m = g.fromJson("{\"roomId\":\"14-816158703683200\",\"name\":\"小居\",\"headId\":3}", (Type) Class.forName("services2.models.RoomConnectModel"));
        System.out.println(m.getHeadId());
    }
}
