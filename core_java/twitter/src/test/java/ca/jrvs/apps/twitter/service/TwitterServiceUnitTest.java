package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

    @Mock
    CrdDao dao;

    @InjectMocks
    TwitterService service;

    @Test
    public void postTweet() {    // test failed request
        String hashTag = "#abc";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        // exception is expected here
        when(dao.create(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            service.postTweet(TweetUtil.buildTweet(text, lon, lat));
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        when(dao.create(any())).thenReturn(new Tweet());
        Tweet tweet = service.postTweet(TweetUtil.buildTweet("test", 50.0, 0.0));
        assertNotNull(tweet);
    }

    @Test
    public void showTweet() {
        String hashTag = "#abc";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet newTweet = TweetUtil.buildTweet(text, lon, lat);
        newTweet.setIdStr("123456");
        try {
            service.showTweet(newTweet.getIdStr(), new String[]{"fail"});
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        when(dao.findById(any())).thenReturn(new Tweet());
        String[] fields = {"id_str", "text", "coordinates"};
        Tweet tweet = service.showTweet(newTweet.getIdStr(), fields);
        assertNotNull(tweet);
    }

    @Test
    public void deleteTweets() {
        String hashTag = "#abc";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet newTweet = TweetUtil.buildTweet(text, lon, lat);
        newTweet.setIdStr("123456");

        when(dao.deleteById(any())).thenReturn(newTweet);
        List<Tweet> tweets = service.deleteTweets(new String[]{newTweet.getIdStr()});
        assertNotNull(tweets);
        assertEquals(1, tweets.size());
    }
}