package TrainClient;

/**
 * Created by oliver on 2017/4/15.
 */
public class TimeBean {
    private String date;
    private String time;
    private int offset;

    public TimeBean(String date, String time) {
        this.date = date;
        this.time = time;
    }
    public TimeBean(int offset,String date, String time) {
        this.date = date;
        this.time = time;
        this.offset = offset;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
