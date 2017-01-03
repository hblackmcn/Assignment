package my.com.astro.sample.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import my.com.astro.sample.Entities.Channel;
import my.com.astro.sample.R;
import my.com.astro.sample.adapters.ChannelsAdapter;
import my.com.astro.sample.database.DbHelper_Channel;

import static my.com.astro.sample.R.id.layoutChannels;
import static my.com.astro.sample.R.id.layoutLoading;

/**
 * Created by hossein on 12/30/16.
 */


public class ChannelsFragment extends Fragment {

    ListView listView_Channels = null;
    TextView textViewChannelCount;

    List<Channel> lstChannels = new ArrayList<Channel>();

    DbHelper_Channel db_Channel;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        lstChannels = new ArrayList<Channel>();

        View fragmentView = inflater.inflate(R.layout.fragment_channels, container, false);


        final LinearLayout layoutLoading = (LinearLayout) fragmentView.findViewById(R.id.layoutLoading);
        final LinearLayout layoutChannels = (LinearLayout) fragmentView.findViewById(R.id.layoutChannels);
        layoutChannels.setVisibility(View.INVISIBLE);

        listView_Channels = (ListView)fragmentView.findViewById(R.id.listView_Channels);
        textViewChannelCount = (TextView)fragmentView.findViewById(R.id.textViewChannelCount);

        final ImageView imageViewSortAZ =  (ImageView) fragmentView.findViewById(R.id.imageViewSortAZ);
        final ImageButton imageViewSort19 =  (ImageButton) fragmentView.findViewById(R.id.imageViewSort19);


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

        layoutLoading.setVisibility(View.VISIBLE);

        db_Channel = new DbHelper_Channel(getActivity());
        lstChannels = db_Channel.getAllData("");

        if (lstChannels!=null && lstChannels.size()>0){
            loadListView(lstChannels);
            layoutChannels.setVisibility(View.VISIBLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }else{
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url ="http://ams-api.astro.com.my/ams/v3/getChannelList";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //mTextView.setText(response);
                            try {
                                JSONObject obj = new JSONObject(response);

                                if (obj.has("responseCode")){
                                    if (obj.get("responseCode").toString().equals("200")){

                                        List<Channel> lst = new ArrayList<Channel>();
                                        try {
                                            JSONArray result = obj.getJSONArray("channels");
                                            for(int i=0;i<result.length();i++)
                                            {
                                                JSONObject chJson = result.getJSONObject(i);

                                                int chID = Integer.valueOf(chJson.get("channelId").toString());
                                                int chNo = Integer.valueOf(chJson.get("channelStbNumber").toString());
                                                String chName = chJson.get("channelTitle").toString();

                                                Channel channel = new Channel(chID,chName,chNo);

                                                lst.add(channel);

                                                db_Channel.insertRow(channel);
                                            }

                                            layoutChannels.setVisibility(View.VISIBLE);
                                            layoutLoading.setVisibility(View.INVISIBLE);
                                            loadListView(lst);
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
                    Log.e("Error",error.getMessage());
                    layoutLoading.setVisibility(View.INVISIBLE);
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }

        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void loadListView(List<Channel> lstChannel){
        this.lstChannels = lstChannel;

        ChannelsAdapter adapter = new ChannelsAdapter(getActivity(), lstChannel);
        listView_Channels.setAdapter(adapter);

        textViewChannelCount.setText(lstChannel.size() + " " + getActivity().getString(R.string.channels));
    }

    private void sortChannelsByName(){
        if (lstChannels!=null){
            Collections.sort(lstChannels, new Comparator<Channel>(){
                public int compare(Channel ch1, Channel ch2) {
                    // ## Ascending order
                    return ch1.getName().compareToIgnoreCase(ch2.getName()); // To compare string values

                    // ## Descending order
                    //return ch2.getName().compareToIgnoreCase(ch1.getName()); // To compare string values
                }
            });
        }

        loadListView(lstChannels);
    }

    private void sortChannelsByNumber(){
        if (lstChannels!=null){
            Collections.sort(lstChannels, new Comparator<Channel>(){
                public int compare(Channel ch1, Channel ch2) {
                    // ## Ascending order
                    return Integer.valueOf(ch1.getNumber()).compareTo(ch2.getNumber());

                    // ## Descending order
                    //return Integer.valueOf(ch2.getNumber()).compareTo(ch1.getNumber());
                }
            });
        }

        loadListView(lstChannels);
    }


}