package my.com.astro.sample.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import my.com.astro.sample.Entities.Channel;
import my.com.astro.sample.R;
import my.com.astro.sample.database.DbHelper_Channel;

/**
 * Created by hossein on 12/31/16.
 */

public class ChannelEventsAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;

    private List<Channel> lstChannels;
    DbHelper_Channel db_Channel;


    private Date startDate;
    private Date endDate;

    public ChannelEventsAdapter(Activity _activity, List<Channel> _lstChannels) {
        this.lstChannels = _lstChannels;
        this.activity = _activity;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db_Channel = new DbHelper_Channel(this.activity);


        Calendar startT = Calendar.getInstance();
        Calendar endT = Calendar.getInstance();
        startT.add(Calendar.HOUR,-2);
        endT.add(Calendar.HOUR,2);

        startDate = startT.getTime();
        endDate = endT.getTime();

        Log.i("timeline=", "startDate=" + startDate + " endDate=" + endDate );
    }


    @Override
    public int getCount() {
        if (lstChannels != null) {
            return lstChannels.size();
        } else {
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

        View vi = view;
        if (view == null)
            vi = inflater.inflate(R.layout.list_item_channel_event, null);

        TextView textviewChannelName = (TextView) vi.findViewById(R.id.textviewChannelName); // ChannelName
        TextView textviewChannelNo = (TextView) vi.findViewById(R.id.textviewChannelNo); // ChannelNo
        final ImageView imageViewFavorite = (ImageView) vi.findViewById(R.id.imageViewFavorite); // Favorite icon
        LinearLayout linearLayoutEvents = (LinearLayout) vi.findViewById(R.id.linearLayoutEvents); // linearLayout to holds event items
        //NestedScrollView nestedScrollViewEvents = (NestedScrollView) vi.findViewById(R.id.nestedScrollViewEvents); // linearLayout to holds event items


        // Setting all values in listview
        textviewChannelName.setText(lstChannels.get(i).getName());
        textviewChannelNo.setText("CH " + String.valueOf(lstChannels.get(i).getNumber()));

        setFavoriteIcon(lstChannels.get(i), imageViewFavorite);

        imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lstChannels.get(i).setFavorite(!lstChannels.get(i).isFavorite());

                    db_Channel.updateRow(lstChannels.get(i));
                    setFavoriteIcon(lstChannels.get(i), imageViewFavorite);
                } catch (ActivityNotFoundException e) {

                }
            }
        });


        linearLayoutEvents.removeAllViews();
        //nestedScrollViewEvents.removeAllViews();

        int eventsSize = lstChannels.get(i).getChannelEvents().size();


        Log.i("timeline=", "-------------------------------------------------------" );
        Log.i("timeline=","Channel ID=" + lstChannels.get(i).getId() + " eventsSize=" + eventsSize);

        if (eventsSize>0) {
            float weightSum = 0;
            for (int j = 0; j < eventsSize; j++) {


                Date ceStart = lstChannels.get(i).getChannelEvents().get(j).getDisplayStartTime();
                Date ceEnd = lstChannels.get(i).getChannelEvents().get(j).getDisplayEndTime();

                Log.i("timeline=", "start="  + ceStart + " , end=" + ceEnd );

                Long remainTime= 0L;
                if (ceStart.getTime()< startDate.getTime() && ceEnd.getTime()> startDate.getTime() && ceEnd.getTime()< endDate.getTime() ){
                    remainTime = ceEnd.getTime() - startDate.getTime();
                }else if (ceStart.getTime()< startDate.getTime() && ceEnd.getTime()> endDate.getTime()){
                    remainTime = endDate.getTime() - startDate.getTime();
                }else if (ceStart.getTime()> startDate.getTime() && ceEnd.getTime()< endDate.getTime() ){
                    remainTime = ceEnd.getTime() - ceStart.getTime();
                }else if (ceStart.getTime()> startDate.getTime() && ceStart.getTime()< endDate.getTime() && ceEnd.getTime()> endDate.getTime() ) {
                    remainTime = endDate.getTime() - ceStart.getTime();
                }else{
                    continue;
                }

                View viewEvent  = inflater.inflate(R.layout.list_item_event, null);
                TextView textviewEventTitle = (TextView) viewEvent.findViewById(R.id.textviewEventTitle); // ChannelName
                TextView textviewEventTime = (TextView) viewEvent.findViewById(R.id.textviewEventTime); // ChannelNo

                textviewEventTitle.setText(lstChannels.get(i).getChannelEvents().get(j).getProgrammeTitle());
                textviewEventTime.setText(lstChannels.get(i).getChannelEvents().get(j).getDisplayTimeHHMM());

                if (ceStart.getTime()< startDate.getTime() && ceEnd.getTime()> startDate.getTime() && ceEnd.getTime()< endDate.getTime() ){
                    textviewEventTime.setVisibility(View.INVISIBLE);
                }


                LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
                childParam1.weight = (float)remainTime / ((float)(endDate.getTime() - startDate.getTime()));
                weightSum += childParam1.weight;
                viewEvent.setLayoutParams(childParam1);


                Log.i("timeline", "weight="  + childParam1.weight + " - sumWeight = " + weightSum);

//                LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(400,LinearLayout.LayoutParams.MATCH_PARENT);
//                viewEvent.setLayoutParams(childParam1);

                Date now = new Date();
                if (ceStart.getTime()< now.getTime() && ceEnd.getTime()> now.getTime() ){
                    viewEvent.setBackgroundColor(activity.getColor(R.color.colorAstroLight));
                }else{
                    viewEvent.setBackgroundColor(activity.getColor(R.color.colorTransparent));
                }

                //linearLayoutEvents.setWeightSum(weightSum);
                linearLayoutEvents.addView(viewEvent);
                //nestedScrollViewEvents.addView(viewEvent);
                //linearLayoutEvents.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
            }
            linearLayoutEvents.setWeightSum(weightSum);
            Log.i("timeline", "weight Sum=" + weightSum);
        }


        return vi;
    }


    private void setFavoriteIcon(Channel ch, ImageView imageView) {
        String favorite_uri = "";
        if (ch.isFavorite()) {
            favorite_uri = "drawable/ic_favorite";
        } else {
            favorite_uri = "drawable/ic_favorite_gray";
        }
        int imageResource = activity.getResources().getIdentifier(favorite_uri, null, activity.getPackageName());
        imageView.setImageResource(imageResource);
    }
}
