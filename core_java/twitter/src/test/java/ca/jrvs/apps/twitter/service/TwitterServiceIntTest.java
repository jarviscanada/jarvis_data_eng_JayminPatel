package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {

    private TwitterService service;

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
        CrdDao dao = new TwitterDao(httpHelper);
        this.service = new TwitterService(dao);
    }

    @Test
    public void postTweet() {
        String hashTag = "#service";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet tweet = service.postTweet(TweetUtil.buildTweet(text, lon, lat));

        assertEquals(text, tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void showTweet() {
        String hashTag = "#service";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet newTweet = service.postTweet(TweetUtil.buildTweet(text, lon, lat));
        Tweet tweet = service
                .showTweet(newTweet.getIdStr(), new String[]{"id_str", "text", "coordinates"});

        assertEquals(text, tweet.getText());
        assertNotNull(tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void deleteTweets() {
        String hashTag = "#service";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        String[] ids = new String[5];
        for (int i = 0; i < 5; i++) {
            Tweet newTweet = service.postTweet(TweetUtil.buildTweet(text + i, lon, lat));
            ids[i] = newTweet.getIdStr();
        }

        List<Tweet> tweets = service.deleteTweets(ids);

        assertNotNull(tweets.get(0).getText());
        assertNotNull(tweets.get(0).getCoordinates());
        assertEquals(2, tweets.get(0).getCoordinates().getCoordinates().size());
        assertEquals(lon, tweets.get(0).getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweets.get(0).getCoordinates().getCoordinates().get(1));
    }
}