package services2.models;

public class ModelWrapper<T extends Model> {
    private T model;
    private int port;

    public ModelWrapper(T model, int port) {
        this.model = model;
        this.port = port;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
