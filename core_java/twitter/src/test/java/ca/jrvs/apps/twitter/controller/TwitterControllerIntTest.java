package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {

    private TwitterController controller;

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
        Service service = new TwitterService(dao);
        this.controller = new TwitterController(service);
    }

    @Test
    public void postTweet() {
        String hashTag = "#service";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet tweet = controller
                .postTweet(new String[]{"post", text, String.format("%s:%s", lat, lon)});

        assertEquals(text, tweet.getText());
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void showTweet() {
        String hashTag = "#service";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet tmpTweet = controller
                .postTweet(new String[]{"post", text, String.format("%s:%s", lat, lon)});

        Tweet tweet = controller
                .showTweet(new String[]{"show", tmpTweet.getIdStr(), "text,coordinates"});

        assertEquals(text, tweet.getText());
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void deleteTweet() {
        String hashTag = "#service";
        String text = "@williamyll1 sometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;

        Tweet tmpTweet = controller
                .postTweet(new String[]{"post", text, String.format("%s:%s", lat, lon)});

        List<Tweet> tweets = controller
                .deleteTweet(new String[]{"delete", String.format("%s", tmpTweet.getIdStr())});

        assertEquals(1, tweets.size());
        assertEquals(text, tweets.get(0).getText());
        assertEquals(lat, tweets.get(0).getCoordinates().getCoordinates().get(0));
        assertEquals(lon, tweets.get(0).getCoordinates().getCoordinates().get(1));
    }
}