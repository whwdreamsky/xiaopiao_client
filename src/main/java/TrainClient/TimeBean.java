package TrainClient;

/**
 * Created by oliver on 2017/4/15.
 */
public class TimeBean {
    private String time;
    private String timeperiod;
    private String timeend;
    private int offset;

    public TimeBean(String time) {
        this.time = time;
    }
    public TimeBean(int offset,String time,String timeend) {
        this.time = time;
        this.offset = offset;
        this.timeend = timeend;
    }

    public String getTimeperiod() {
        return timeperiod;
    }

    public void setTimeperiod(String timeperiod) {
        this.timeperiod = timeperiod;
        sloveTimePeriod();
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
       //只取
        this.time = getHourMinute(time);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /*
    public void setDateTime(String datetime)
    {

        String date_time[] = datetime.split("T",2);
        if(date_time.length==2)
        {
            setDate(date_time[0]);
            String timestr = date_time[1].substring(0,date_time[1].length()-1);
            setTime(getHourMinute(timestr));
        }
        else
        {
            System.err.println("datetime 解析时间失败");
        }
    }
    */

    public void sloveTimePeriod()
    {
        //10:00:00/12:00:00
        String[]timelist = timeperiod.split("/");
        if(timelist.length==2)
        {
            time = getHourMinute(timelist[0]);
            timeend = getHourMinute(timelist[1]);
        }
    }

    //输入10:00:00
    private String getHourMinute(String timestr)
    {
        String[]timelist = timestr.split(":");
        String newstr="";
        if(timelist.length==3)
        {
            newstr=timelist[0]+":"+timelist[1];
        }
        else
        {
            System.out.println("time 解析失败");
        }
        return newstr;
    }
}
