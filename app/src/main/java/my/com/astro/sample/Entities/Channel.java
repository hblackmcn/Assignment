package my.com.astro.sample.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hossein on 12/31/16.
 */

public class Channel {

    private String name;
    private int id;
    private int number;
    private boolean isFavorite = false;
    private List<ChannelEvent> channelEvents;

    public Channel(){
        this.name= "";
        this.id = 0;
        this.number = 0;
        this.isFavorite = false;
        this.channelEvents = new ArrayList<ChannelEvent>();
    }

    public Channel(int _id, String _name, int _number){
        this.name= _name;
        this.id =_id;
        this.number = _number;
        this.channelEvents = new ArrayList<ChannelEvent>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<ChannelEvent> getChannelEvents() {
        return channelEvents;
    }

    public void setChannelEvents(List<ChannelEvent> channelEvents) {
        this.channelEvents = channelEvents;
    }
}
