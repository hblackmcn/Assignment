package my.com.astro.sample.singletones;

import java.util.HashMap;
import java.util.Map;

import my.com.astro.sample.Entities.Channel;

/**
 * Created by hossein on 1/1/17.
 */

public class SharedData {

    private static SharedData instance;

    public static Map<Integer,Channel> mapChannels;

    public static boolean isUserLoggedIn = false;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new SharedData();
            mapChannels = new HashMap<Integer,Channel>();
        }
    }

    public static SharedData getInstance()
    {
        // Return the instance
        return instance;
    }

    private SharedData()
    {
        // Constructor hidden because this is a singleton
    }

}


