package services2.models;

import java.util.List;

public class RoomArrangeModel extends Model {
    private List<String> names;
    private List<Integer> headIds;

    public RoomArrangeModel() {}

    public RoomArrangeModel(List<String> names, List<Integer> headIds) {
        this.names = names;
        this.headIds = headIds;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Integer> getHeadIds() {
        return headIds;
    }

    public void setHeadIds(List<Integer> headIds) {
        this.headIds = headIds;
    }
}
