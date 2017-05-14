package TrainClient;

import webservice.TrainToFromData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by oliver on 2017/4/18.
 */
public class ComparatorAdviceIml implements Comparator<TrainToFromData>{


    @Override
    public int compare(TrainToFromData o1, TrainToFromData o2) {
        try {
            Date t1 = new SimpleDateFormat("HH:mm").parse(o1.getStart_time());
            Date t2 = new SimpleDateFormat("HH:mm").parse(o2.getStart_time());
            if(t1.getTime()>t2.getTime())return 1;
            else if(t1.getTime()<t2.getTime()) return -1;
            else {return 0;}
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
