package my.com.astro.sample.Entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.format;

/**
 * Created by hossein on 1/1/17.
 */

public class ChannelEvent {

    String id;
    String imageURL;
    String displayDateTimeUtc;
    String displayDateTime;
    String displayDuration;
    String programmeTitle;
    String programmId;
    String shortSynopsis;
    String episodeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDisplayDateTimeUtc() {
        return displayDateTimeUtc;
    }

    public void setDisplayDateTimeUtc(String displayDateTimeUtc) {
        this.displayDateTimeUtc = displayDateTimeUtc;
    }

    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public void setDisplayDuration(String displayDuration) {
        this.displayDuration = displayDuration;
    }

    public String getProgrammeTitle() {
        return programmeTitle;
    }

    public void setProgrammeTitle(String programmeTitle) {
        this.programmeTitle = programmeTitle;
    }

    public String getProgrammId() {
        return programmId;
    }

    public void setProgrammId(String programmId) {
        this.programmId = programmId;
    }

    public String getShortSynopsis() {
        return shortSynopsis;
    }

    public void setShortSynopsis(String shortSynopsis) {
        this.shortSynopsis = shortSynopsis;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }


    public int getDisplayDurationSeconds()
    {
        int hh = 0;
        int mm = 0;
        int ss = 0;
        if (displayDuration!=null && displayDuration.length()>0){
            hh = Integer.valueOf(displayDuration.substring(0,2));
        }
        if (displayDuration!=null && displayDuration.length()>0){
            mm = Integer.valueOf(displayDuration.substring(3,5));
        }

        if (displayDuration!=null && displayDuration.length()>0){
            ss = Integer.valueOf(displayDuration.substring(6,8));
        }

        return ss + (mm * 60) + (hh * 60 * 60);
    }

    public Date getDisplayStartTime()
    {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        Date date = null;
        try {
            date = format.parse(displayDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public Date getDisplayEndTime()
    {

        Date start = getDisplayStartTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(Calendar.SECOND,getDisplayDurationSeconds());

        return calendar.getTime();
    }





    public String getDisplayTimeHHMM()
    {
        if (displayDateTime!=null && displayDateTime.length()>0){
            return displayDateTime.substring(11,16);
        }else{
            return "";
        }
    }

}
