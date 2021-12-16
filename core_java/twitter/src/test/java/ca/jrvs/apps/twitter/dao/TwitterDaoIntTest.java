package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

    private TwitterDao dao;

    @Before
    public void setup() {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
        // set up dependency
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
                tokenSecret);
        // pass dependency
        this.dao = new TwitterDao(httpHelper);
    }

    @Test
    public void create() {
        String hashTag = "#abc";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet tweet = dao.create(TweetUtil.buildTweet(text, lon, lat));

        assertEquals(text, tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void findById() {
        String hashTag = "#abc";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet newTweet = dao.create(TweetUtil.buildTweet(text, lon, lat));
        Tweet tweet = dao.findById(newTweet.getIdStr());

        assertEquals(text, tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void deleteById() {
        String hashTag = "#abc";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet newTweet = dao.create(TweetUtil.buildTweet(text, lon, lat));
        Tweet tweet = dao.deleteById(newTweet.getIdStr());

        assertEquals(text, tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    }
}