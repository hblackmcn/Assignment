package my.com.astro.sample.fragments;

import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import my.com.astro.sample.Entities.Channel;
import my.com.astro.sample.Entities.ChannelEvent;
import my.com.astro.sample.R;
import my.com.astro.sample.Tools.Utils;
import my.com.astro.sample.adapters.ChannelEventsAdapter;
import my.com.astro.sample.database.DbHelper_Channel;
import my.com.astro.sample.singletones.SharedData;

import static my.com.astro.sample.R.id.imageViewSort19;
import static my.com.astro.sample.R.id.imageViewSortAZ;
import static my.com.astro.sample.R.id.listView_Channels;
import static my.com.astro.sample.R.id.textview_Time_0;

/**
 * Created by hossein on 12/30/16.
 */


public class TvGuidFragment extends Fragment {

    ListView listView_Channels = null;


    List<Channel> lstChannelsAll = new ArrayList<>();
    List<Channel> lstChannelsDisplay = new ArrayList<>();
    int next;
    boolean isLoading = false;

    DbHelper_Channel db_Channel;


    ChannelEventsAdapter adapter;

    private String startDate;
    private String endDate;

    LinearLayout layoutLoading;
    LinearLayout linearLayoutTimeLineHeader;

    View vertical_bar;

    TextView textview_Time_0;
    TextView textview_Time_1;
    TextView textview_Time_2;
    TextView textview_Time_3;
    TextView textview_Time_4;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_tv_guid, container, false);
        listView_Channels = (ListView) fragmentView.findViewById(R.id.listView_Channels);
        layoutLoading = (LinearLayout) fragmentView.findViewById(R.id.layoutLoading);
        linearLayoutTimeLineHeader = (LinearLayout) fragmentView.findViewById(R.id.LinearLayoutTimeLineHeader);
        linearLayoutTimeLineHeader.setVisibility(View.INVISIBLE);

        vertical_bar = (View) fragmentView.findViewById(R.id.vertical_bar);

        textview_Time_0 = (TextView) fragmentView.findViewById(R.id.textview_Time_0);
        textview_Time_1 = (TextView) fragmentView.findViewById(R.id.textview_Time_1);
        textview_Time_2 = (TextView) fragmentView.findViewById(R.id.textview_Time_2);
        textview_Time_3 = (TextView) fragmentView.findViewById(R.id.textview_Time_3);
        textview_Time_4 = (TextView) fragmentView.findViewById(R.id.textview_Time_4);


        final ImageView imageViewSortAZ = (ImageView) fragmentView.findViewById(R.id.imageViewSortAZ);
        final ImageButton imageViewSort19 = (ImageButton) fragmentView.findViewById(R.id.imageViewSort19);



        imageViewSortAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(getActivity().getColor(R.color.colorAstro));
                imageViewSort19.setBackgroundColor(getActivity().getColor(R.color.colorTransparent));
                sortChannelsByName();
            }
        });

        imageViewSort19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(getActivity().getColor(R.color.colorAstro));
                imageViewSortAZ.setBackgroundColor(getActivity().getColor(R.color.colorTransparent));
                sortChannelsByNumber();
            }
        });




        intTimeSetting();

        db_Channel = new DbHelper_Channel(getActivity());

        //retrieve list of channels from local database
        lstChannelsAll = db_Channel.getAllData("");

        //fill the map of channels with existing data came from db
        SharedData.getInstance().mapChannels.clear();
        for (Channel ch : lstChannelsAll) {
            SharedData.getInstance().mapChannels.put(ch.getId(), ch);
        }

        initList();
        loadMore();


        listView_Channels.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Ignore this method
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.i("Main", totalItemCount + "");
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;

                if (lastIndexInScreen >= totalItemCount && !isLoading) {
                    // It is time to load more items
                    isLoading = true;
                    loadMore();
                }
            }

        });


        //db_Channel = new DbHelper_Channel(getActivity());
        //lstChannels = db_Channel.getAllData("isFavorite = 1");


        // Inflate the layout for this fragment
        return fragmentView;
    }


    private void intTimeSetting(){
        Calendar startT = Calendar.getInstance();
        Calendar endT = Calendar.getInstance();
        startT.add(Calendar.HOUR, -6);
        endT.add(Calendar.HOUR, 3);

        //SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdfStart = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        startDate = sdfStart.format(startT.getTime());
        endDate = sdfEnd.format(endT.getTime());


        Calendar t0 = Calendar.getInstance();
        Calendar t1 = Calendar.getInstance();
        Calendar t2 = Calendar.getInstance();
        Calendar t3 = Calendar.getInstance();
        Calendar t4 = Calendar.getInstance();

        t0.add(Calendar.HOUR, -2);
        t1.add(Calendar.HOUR, -1);
        t3.add(Calendar.HOUR, +1);
        t4.add(Calendar.HOUR, +2);

        textview_Time_0.setText(Utils.getFormattedHHMM(t0.getTime()));
        textview_Time_1.setText(Utils.getFormattedHHMM(t1.getTime()));
        textview_Time_2.setText(Utils.getFormattedHHMM(t2.getTime()));
        textview_Time_3.setText(Utils.getFormattedHHMM(t3.getTime()));
        textview_Time_4.setText(Utils.getFormattedHHMM(t4.getTime()));

    }

    public void initList() {
        next = 0;
        // Create ArrayList object
        lstChannelsDisplay = new ArrayList<Channel>();

        adapter = new ChannelEventsAdapter(getActivity(), lstChannelsDisplay);
        listView_Channels.setAdapter(adapter);
    }

    private void retrieveChannelEvents(final List<Integer> lstChannelIDs) {

        loadingStarted();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://ams-api.astro.com.my/ams/v3/getEvents?channelId=";


        int channelCount = 0;
        for (int i = 0; i < lstChannelIDs.size(); i++) {
            Channel ch = SharedData.getInstance().mapChannels.get(lstChannelIDs.get(i));
            //retrieve information only if there is no data cached in the system
            if (ch != null && ch.getChannelEvents().size() == 0) {
                if (channelCount == 0) {
                    url += lstChannelIDs.get(i);
                } else {
                    url += "," + lstChannelIDs.get(i);
                }
                channelCount++;
            }
        }

        url += "&periodStart=" + startDate;
        url += "&periodEnd=" + endDate;

        url = url.replace(" ", "%20");

        System.out.println("Request Sent:" + url);


        if (channelCount>0){
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //mTextView.setText(response);
                            try {
                                JSONObject obj = new JSONObject(response);

                                if (obj.has("responseCode")) {
                                    if (obj.get("responseCode").toString().equals("200")) {

                                        try {
                                            JSONArray result = obj.getJSONArray("getevent");
                                            for (int i = 0; i < result.length(); i++) {
                                                JSONObject chJson = result.getJSONObject(i);

                                                int chID = Integer.valueOf(chJson.get("channelId").toString());

                                                Channel ch = SharedData.getInstance().mapChannels.get(chID);
                                                if (ch != null) {
                                                    ChannelEvent ce = new ChannelEvent();
                                                    ce.setId(chJson.get("eventID").toString());

                                                    ce.setDisplayDateTimeUtc(chJson.get("displayDateTimeUtc").toString());
                                                    ce.setDisplayDateTime(chJson.get("displayDateTime").toString());
                                                    ce.setDisplayDuration(chJson.get("displayDuration").toString());
                                                    ce.setProgrammeTitle(chJson.get("programmeTitle").toString());

                                                    ch.getChannelEvents().add(ce);
                                                    SharedData.getInstance().mapChannels.put(chID, ch);
                                                }

                                            }

                                            for (int i = 0; i < lstChannelIDs.size(); i++) {
                                                Channel ch = SharedData.getInstance().mapChannels.get(lstChannelIDs.get(i));
                                                sortChannelEventsByTime(ch);

                                                lstChannelsDisplay.add(ch);
                                                next++;
                                            }

                                            // Notify the ListView of data changed
                                            adapter.notifyDataSetChanged();

                                            loadingFinished();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", "Error in Volly onErrorResponse");
                    if (error != null && error.getMessage() != null) {
                        Log.e("Error", error.getMessage());
                    }

                    loadingFinished();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }else{
            for (int i = 0; i < lstChannelIDs.size(); i++) {
                Channel ch = SharedData.getInstance().mapChannels.get(lstChannelIDs.get(i));
                sortChannelEventsByTime(ch);

                lstChannelsDisplay.add(ch);
                next++;
            }

            // Notify the ListView of data changed
            adapter.notifyDataSetChanged();
            loadingFinished();
        }

    }


    public void loadMore() {

        isLoading = true;

        if (next < lstChannelsAll.size()) {
            List<Integer> lstIDs = new ArrayList<Integer>();
            for (int i = next; i < next + 15; i++) {
                if (i < lstChannelsAll.size()) {
                    lstIDs.add(lstChannelsAll.get(i).getId());
                }
            }
            retrieveChannelEvents(lstIDs);
        }
    }


    private void sortChannelEventsByTime(Channel channel) {
        if (channel.getChannelEvents() != null) {
            Collections.sort(channel.getChannelEvents(), new Comparator<ChannelEvent>() {
                public int compare(ChannelEvent ce1, ChannelEvent ce2) {
                    // ## Ascending order
                    return ce1.getDisplayDateTimeUtc().compareToIgnoreCase(ce2.getDisplayDateTimeUtc()); // To compare string values
                }
            });
        }
    }


    private void sortChannelsByName() {
        if (lstChannelsAll != null) {
            Collections.sort(lstChannelsAll, new Comparator<Channel>() {
                public int compare(Channel ch1, Channel ch2) {
                    // ## Ascending order
                    return ch1.getName().compareToIgnoreCase(ch2.getName()); // To compare string values

                    // ## Descending order
                    //return ch2.getName().compareToIgnoreCase(ch1.getName()); // To compare string values
                }
            });
        }

        sortChanged();
    }

    private void sortChannelsByNumber() {
        if (lstChannelsAll != null) {
            Collections.sort(lstChannelsAll, new Comparator<Channel>() {
                public int compare(Channel ch1, Channel ch2) {
                    // ## Ascending order
                    return Integer.valueOf(ch1.getNumber()).compareTo(ch2.getNumber());

                    // ## Descending order
                    //return Integer.valueOf(ch2.getNumber()).compareTo(ch1.getNumber());
                }
            });
        }

        sortChanged();
    }

    private void sortChanged() {
        loadingStarted();
        next = 0;
        lstChannelsDisplay.clear();
        // Notify the ListView of data changed
        adapter.notifyDataSetChanged();

        loadMore();
    }

    private void loadingStarted(){
        isLoading = true;
        layoutLoading.setVisibility(View.VISIBLE);
        vertical_bar.setVisibility(View.INVISIBLE);
    }

    private void loadingFinished(){
        isLoading = false;
        layoutLoading.setVisibility(View.INVISIBLE);
        vertical_bar.setVisibility(View.VISIBLE);
        linearLayoutTimeLineHeader.setVisibility(View.VISIBLE);
    }


}