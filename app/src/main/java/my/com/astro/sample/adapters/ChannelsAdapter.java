package my.com.astro.sample.adapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import my.com.astro.sample.Entities.Channel;
import my.com.astro.sample.R;
import my.com.astro.sample.database.DbHelper_Channel;
import my.com.astro.sample.dialog.SignInDialog;
import my.com.astro.sample.listeners.OnMassageRecievedListener;
import my.com.astro.sample.singletones.SharedData;

/**
 * Created by hossein on 12/31/16.
 */

public class ChannelsAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater=null;

    private List<Channel> lstChannels;
    DbHelper_Channel db_Channel;

    public ChannelsAdapter(Activity _activity,List<Channel> _lstChannels) {
        this.lstChannels = _lstChannels;
        this.activity = _activity;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db_Channel = new DbHelper_Channel(this.activity);
    }


    @Override
    public int getCount() {
        if (lstChannels!=null){
            return lstChannels.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return lstChannels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstChannels.get(i).getId();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View vi=view;
        if(view==null)
            vi = inflater.inflate(R.layout.list_item_channel, null);

        TextView textviewChannelName = (TextView)vi.findViewById(R.id.textviewChannelName); // ChannelName
        TextView textviewChannelNo = (TextView)vi.findViewById(R.id.textviewChannelNo); // ChannelNo
        final ImageView imageViewFavorite = (ImageView) vi.findViewById(R.id.imageViewFavorite); // Favorite icon

        // Setting all values in listview
        textviewChannelName.setText( lstChannels.get(i).getName());
        textviewChannelNo.setText( "CH " + String.valueOf(lstChannels.get(i).getNumber()));

        setFavoriteIcon(lstChannels.get(i),imageViewFavorite);

        imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {





                    if (SharedData.getInstance().isUserLoggedIn==true)
                    {
                        lstChannels.get(i).setFavorite(!lstChannels.get(i).isFavorite());
                        db_Channel.updateRow(lstChannels.get(i));
                        setFavoriteIcon(lstChannels.get(i),imageViewFavorite);
                    }else{
                        FragmentManager manager = activity.getFragmentManager();

                        final DialogFragment newFragment = new SignInDialog();
                        newFragment.show( manager, "dialog");

                        ((SignInDialog) newFragment).setOnMassageRecievedListener(new OnMassageRecievedListener() {
                            @Override
                            public void onMassageRecieved(String msg) {
                                if (msg.equals("successful")){
                                    lstChannels.get(i).setFavorite(!lstChannels.get(i).isFavorite());
                                    db_Channel.updateRow(lstChannels.get(i));
                                    setFavoriteIcon(lstChannels.get(i),imageViewFavorite);
                                }
                            }
                        } );


                    }

                } catch (ActivityNotFoundException e) {

                }
            }
        });


        return vi;
    }


    private void setFavoriteIcon(Channel ch, ImageView imageView){
        String favorite_uri = "";
        if (ch.isFavorite()){
            favorite_uri = "drawable/ic_favorite" ;
        }else{
            favorite_uri = "drawable/ic_favorite_gray" ;
        }
        int imageResource = activity.getResources().getIdentifier(favorite_uri, null, activity.getPackageName());
        imageView.setImageResource(imageResource);

    }
}
