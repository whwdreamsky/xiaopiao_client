package TrainClient;

import com.google.gson.JsonElement;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by oliver on 2017/4/18.
 */
public class ComparatorSlotListImp implements Comparator<Map.Entry<String,JsonElement>> {

    @Override
    public int compare(Map.Entry<String,JsonElement> o1, Map.Entry<String,JsonElement> o2) {
        int offset1 =o1.getValue().getAsJsonObject().get("offset").getAsInt();
        int offset2 =o2.getValue().getAsJsonObject().get("offset").getAsInt() ;
        return (offset1-offset2);
    }
}
