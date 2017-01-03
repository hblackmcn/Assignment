package my.com.astro.sample.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import my.com.astro.sample.Entities.Channel;
import my.com.astro.sample.MainActivity;
import my.com.astro.sample.R;
import my.com.astro.sample.adapters.ChannelsAdapter;
import my.com.astro.sample.database.DbHelper_Channel;
/**
 * Created by hossein on 12/30/16.
 */


public class FavoritesFragment extends Fragment {

    ListView listView_Channels = null;

    List<Channel> lstChannels = new ArrayList<>();

    DbHelper_Channel db_Channel;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        lstChannels = new ArrayList<Channel>();

        View fragmentView = inflater.inflate(R.layout.fragment_favorites, container, false);

        listView_Channels = (ListView) fragmentView.findViewById(R.id.listView_Channels);
        Button buttonViewAllChannels = (Button) fragmentView.findViewById(R.id.buttonViewAllChannels);


        buttonViewAllChannels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();

                activity.navigationView.getMenu().getItem(0).setChecked(true);
                activity.loadFragment(R.id.nav_channel);
            }
        });

        db_Channel = new DbHelper_Channel(getActivity());
        lstChannels = db_Channel.getAllData("isFavorite = 1");

        if (lstChannels != null && lstChannels.size() > 0) {
            loadListView(lstChannels);
        }

        // Inflate the layout for this fragment
        return fragmentView;
    }

    private void loadListView(List<Channel> lstChannel) {
        this.lstChannels = lstChannel;

        ChannelsAdapter adapter = new ChannelsAdapter(getActivity(), lstChannel);
        listView_Channels.setAdapter(adapter);
    }

}