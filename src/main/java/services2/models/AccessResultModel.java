package services2.models;

public class AccessResultModel extends Model {
    private boolean accepted;

    public AccessResultModel() {}

    public AccessResultModel(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
