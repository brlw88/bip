package me.brlw.bip.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by vl on 17.09.16.
 */
public class RedirectingThread extends Thread
{
    private ThreadLocalRandom r = ThreadLocalRandom.current();
    private int num;
    private Map<String, String> shortUrls;
    private String alternateRedirectUrl;
    private boolean alternativeImplementation;
    private RestTemplate restTemplate;
    private AtomicInteger invalidRedirectCodesCount = new AtomicInteger(0);

    public RedirectingThread(int num, Map<String, String> shortUrls, String alternateRedirectUrl, boolean alternativeImpementation, RestTemplate restTemplate)
    {
        this.num = num;
        this.shortUrls = shortUrls;
        this.alternateRedirectUrl = alternateRedirectUrl;
        this.alternativeImplementation = alternativeImpementation;
        this.restTemplate = restTemplate;
    }

    public int getInvalidRedirectCodesCount() {
        return invalidRedirectCodesCount.get();
    }

    private ResponseEntity<String> redirect(String shortUrl)
    {
        StringBuilder url = new StringBuilder();
        url.append(RestInterfaceTests.BASE_URL);
        url.append(shortUrl);
        if (alternativeImplementation)
            url.append("/1");
        return restTemplate.getForEntity(url.toString(), String.class);
    }

    @Override
    public void run() {
        List<String> shortUrlsA = shortUrls.values().stream().collect(Collectors.toList());
        r.ints(0, shortUrls.size()).limit(RestInterfaceTests.NUM_REDIRECTS).forEach( i -> {
            ResponseEntity<String> e = redirect(shortUrlsA.get(i));
            int expectedCode = (shortUrlsA.get(i).equals(alternateRedirectUrl) ? 301 : 302);
//                int expectedCode = 302;
            if (e.getStatusCodeValue() != expectedCode)
                System.out.println( invalidRedirectCodesCount.incrementAndGet() + ": - invalid redirection HTTP Status Code for short URL " + shortUrlsA.get(i) + " - got " + e.getStatusCode() + ", expected " + expectedCode);
        } );
    }
}
