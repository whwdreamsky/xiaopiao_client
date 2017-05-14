package TrainClient;

import java.util.HashMap;

/**
 * Created by oliver on 2017/4/18.
 */
public class SlotBean {
    private int offset;
    private String value;

    public SlotBean(int offset, String value) {
        this.offset = offset;
        this.value = value;
    }

    public SlotBean(String value) {
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
