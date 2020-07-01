package services2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import services2.models.Model;
import services2.models.ModelWrapper;

import java.lang.reflect.Type;

import static constants.CoreConstants.*;

public abstract class Core {

    private Sender sender;

    private Receiver receiver;

    public Core(int receiverPort) {
        receiver = new Receiver(this, receiverPort);
        sender = new Sender();
    }

    public Core () {
        receiver = new Receiver(this);
        sender = new Sender();
    }

    public void send (Model model, int toIP, int toPort) {
        Gson g = new Gson();
        JsonObject obj = new JsonObject();
        obj.addProperty(TYPE, model.getClass().getName());
        obj.addProperty(REPLY_ME_AT_PORT, receiver.getPort());
        obj.add(PARAMETERS, g.toJsonTree(model));
        String toSend = g.toJson(obj);
        sender.send(toSend, toIP, toPort);
    }

    public void process(String data, int from) {
        Gson g = new Gson();
        JsonObject obj = g.fromJson(data, JsonObject.class);
        String type = obj.get(TYPE).getAsString();
        int replyPort = obj.get(REPLY_ME_AT_PORT).getAsInt();
        Model model = new Model();
        try {
            model = g.fromJson(obj.getAsJsonObject(PARAMETERS), (Type) Class.forName(type));
        } catch (ClassNotFoundException ignored) {}
        process(model, from, replyPort);
    }

    public void shutDown() {
        sender.shutDown();
        receiver.shutDown();
    }

    public int getPort() {
        return receiver.getPort();
    }

    public abstract void process(Model params, int replyIP, int replyPort);
}
