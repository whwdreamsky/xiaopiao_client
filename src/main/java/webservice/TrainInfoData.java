package webservice;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oliver on 2017/4/15.
 */
public class TrainInfoData {
    private Train_info train_info;
    private List<Station_list> station_list;

    public Train_info getTrain_info() {
        return train_info;
    }

    public void setTrain_info(Train_info train_info) {
        this.train_info = train_info;
    }

    public List<Station_list> getStation_list() {
        return station_list;
    }

    public void setStation_list(List<Station_list> station_list) {
        this.station_list = station_list;
    }
}
