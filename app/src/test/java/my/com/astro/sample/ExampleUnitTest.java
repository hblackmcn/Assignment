package my.com.astro.sample;

import org.junit.Test;

import java.util.List;

import my.com.astro.sample.Entities.Channel;
import my.com.astro.sample.database.DbHelper_Channel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);

        DbHelper_Channel db_Channel = new DbHelper_Channel(new MainActivity());
        List<Channel> lst = db_Channel.getAllData("");

        System.out.println(lst.size());

    }
}