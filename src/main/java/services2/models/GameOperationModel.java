package services2.models;

public class GameOperationModel extends Model {
    private String operation;

    public GameOperationModel() {
    }

    public GameOperationModel(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
