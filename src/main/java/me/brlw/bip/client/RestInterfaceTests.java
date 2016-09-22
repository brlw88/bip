package me.brlw.bip.client;

import me.brlw.bip.account.AccountCreateResponseDto;
import me.brlw.bip.account.AccountDto;
import me.brlw.bip.redirection.RedirectionCreateResponseDto;
import me.brlw.bip.redirection.RedirectionDto;
import me.brlw.bip.utils.RandomStringGenerator;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Created by ww on 08.09.16.
 */
public class RestInterfaceTests
{
    public static final String BASE_URL = "http://localhost:8080/bip/";
    private static final String URL_ADD_ACCOUNT = BASE_URL.concat("account");
    private static final String URL_ADD_REDIRECTION = BASE_URL.concat("register");
    private static final String URL_GET_STATISTICS = BASE_URL.concat("statistic/");

    private static final String[] URLS_TO_REDIRECT_USER1 = {
            "https://en.wikipedia.org/wiki/Abstract_factory_pattern",
            "https://en.wikipedia.org/wiki/Builder_pattern",
            "https://en.wikipedia.org/wiki/Factory_method_pattern",
            "https://en.wikipedia.org/wiki/Lazy_initialization",
            "https://en.wikipedia.org/wiki/Multiton_pattern"
    };

    private static final String[] URLS_TO_REDIRECT_USER2 = {
            "http://www.javacamp.org/designPattern/abstractfactory.html",
            "http://www.javacamp.org/designPattern/builder.html",
            "http://www.javacamp.org/designPattern/factory.html",
            "https://en.wikipedia.org/wiki/Lazy_initialization",
            "https://en.wikipedia.org/wiki/Multiton_pattern"
    };


    public static final int NUM_THREADS = 30;
    public static final int NUM_REDIRECTS = 100;

    private RestTemplate restTemplate;

    private Map<String, String> shortUrls1 = new HashMap<>();
    private Map<String, String> shortUrls2 = new HashMap<>();
    
    public RestInterfaceTests()
    {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        };
        factory.setOutputStreaming(false);
        restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new RestClientErrorHandler());
    }
    
    private HttpHeaders getBasicAuthHeaders(String user, String password)
    {
        String credentials = String.format("%s:%s", user, password);
        byte[] base64Credentials = Base64.encodeBase64(credentials.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic ".concat(new String(base64Credentials)));
        return headers;
    }

    public ResponseEntity<AccountCreateResponseDto> addAccount(String accountId)
    {
        return restTemplate.postForEntity(URL_ADD_ACCOUNT, new AccountDto(accountId), AccountCreateResponseDto.class);
    }

    public ResponseEntity<RedirectionCreateResponseDto> addRedirection(String user, String password, String url, Integer... redirectType)
    {
        ResponseEntity<RedirectionCreateResponseDto> entity = restTemplate.exchange(URL_ADD_REDIRECTION,
                HttpMethod.POST,
                new HttpEntity<RedirectionDto>(new RedirectionDto(url, redirectType), getBasicAuthHeaders(user, password)),
                RedirectionCreateResponseDto.class );
        return entity;
    }
    
    public Map<String, Integer> getStatistics(String user, String password, String id)
    {
        ResponseEntity<Map> entity = restTemplate.exchange(URL_GET_STATISTICS.concat(id),
                HttpMethod.GET,
                new HttpEntity<Void>(getBasicAuthHeaders(user, password)),
                Map.class);
        return entity.getBody();
    }

    public static void printTestResult(boolean result)
    {
        if (result)
            System.out.println("TEST PASSED");
        else
            System.out.println("TEST DIDN'T PASS");
        System.out.println();
    }

    public String testAddingUser(String user, HttpStatus expectedStatus)
    {
        ResponseEntity<AccountCreateResponseDto> e = addAccount(user);
        System.out.println("POST " + URL_ADD_ACCOUNT + ", AccountId = " + user + ", Response = " + e.getBody() );
        System.out.println("HTTP Response Code = " + e.getStatusCode());
        printTestResult(e.getStatusCode() == expectedStatus);
        return e.getBody().password;
    }

    public void testRegistering(String user, String password, String url, Map<String,String> shortUrls, boolean alternateRedirect, HttpStatus expectedStatus)
    {
        ResponseEntity<RedirectionCreateResponseDto> e;
        if (alternateRedirect)
            e = addRedirection(user, password, url, 301 );
        else
            e = addRedirection(user, password, url );
        System.out.println("POST " + URL_ADD_REDIRECTION + ", user = " + user + ", URL = " + url + ", Response = " + e.getBody());
        if (e.getStatusCode() == HttpStatus.CREATED)
            shortUrls.put(url, e.getBody().shortUrl);
        System.out.println("HTTP Response Code = " + e.getStatusCode());
        printTestResult(e.getStatusCode() == expectedStatus);
    }

    public void testInvalidAuthorization(String user, String password, String url, HttpMethod method)
    {
        ResponseEntity e = restTemplate.exchange(url, method,
                new HttpEntity(getBasicAuthHeaders(user, password)),
                Void.class );
        printTestResult(e.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    public void testRedirection(boolean alternativeImplementation)
    {
        List<RedirectingThread> threads = new ArrayList<>();
        for (int i=0; i<NUM_THREADS; i++)
            if (i < NUM_THREADS/2)
                threads.add(new RedirectingThread( i+1, shortUrls1, shortUrls1.get(URLS_TO_REDIRECT_USER1[0]), alternativeImplementation, restTemplate));
            else
                threads.add(new RedirectingThread( i+1, shortUrls2, shortUrls2.get(URLS_TO_REDIRECT_USER2[0]), alternativeImplementation, restTemplate));
        threads.forEach (RedirectingThread::start);

        Instant startTime = Instant.now();
        int invalidRedirectCodesCount = 0;
        try {
            for (int i=0; i<NUM_THREADS; i++) {
                threads.get(i).join();
                invalidRedirectCodesCount += threads.get(i).getInvalidRedirectCodesCount();
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        Instant endTime = Instant.now();
        Duration runTime = Duration.between(startTime, endTime);
        System.out.println(NUM_THREADS*NUM_REDIRECTS + " redirects successfully requested, " + runTime.toMillis() + "ms");
        printTestResult (invalidRedirectCodesCount == 0);
    }
    
    public void testStatistics(String user, String password, String accountId)
    {
        Map<String, Integer> map = getStatistics(user, password, accountId);
        map.forEach( (k,v) -> System.out.println( k + ": " + v ));
        int count = map.values().stream().reduce( (a, v) -> a += v).get();
        System.out.println( count + " redirections in total: "  );
        printTestResult(count == (NUM_THREADS*NUM_REDIRECTS/2)*2);
    }
    
    public void runAllTests()
    {
        String user1 = RandomStringGenerator.generateUsername();
        String user2 = RandomStringGenerator.generateUsername();
        String invalidUser = "0invalid";

        System.out.println("This is test client for URL Shortener");
        System.out.println();
        System.out.println("Adding first account:");
        String password1 = testAddingUser(user1, HttpStatus.CREATED);

        System.out.println("Trying to duplicate first account:");
        testAddingUser(user1, HttpStatus.CONFLICT);

        System.out.println("Trying to add invalid account:");
        testAddingUser(invalidUser, HttpStatus.BAD_REQUEST);

        System.out.println("Adding second account:");
        String password2 = testAddingUser(user2, HttpStatus.CREATED);

        System.out.println();
        System.out.println("Registering URLs for first account:");
        for (String url: URLS_TO_REDIRECT_USER1)
            testRegistering(user1, password1, url, shortUrls1, url.equals(URLS_TO_REDIRECT_USER1[0]), HttpStatus.CREATED );

        System.out.println();
        System.out.println("Registering URLs for second account:");
        for (String url: URLS_TO_REDIRECT_USER2)
            testRegistering(user2, password2, url, shortUrls2, url.equals(URLS_TO_REDIRECT_USER2[0]), HttpStatus.CREATED );

        System.out.println();
        System.out.println("Trying to register duplicate URL:");
        testRegistering(user1, password1, URLS_TO_REDIRECT_USER1[0], shortUrls1, false, HttpStatus.OK );

        System.out.println();
        System.out.println("Trying to register URL with invalid credentials:");
        testInvalidAuthorization(user1, password2, URL_ADD_REDIRECTION, HttpMethod.POST);
        System.out.println("Trying to get statistics with invalid credentials:");
        testInvalidAuthorization(user2, password1, URL_GET_STATISTICS, HttpMethod.GET);

        System.out.println();
        System.out.println(NUM_THREADS + " threads created, started and requesting redirection (ReentrantLock syncronization)...");
        testRedirection(false);

        System.out.println();
        System.out.println(NUM_THREADS + " threads created, started and requesting redirection (Optimistic locking)...");
        testRedirection(true);

        System.out.println();
        System.out.println("Getting statistics for " + user1 + ":");
        testStatistics(user1, password1, user1);

        System.out.println();
        System.out.println("Getting statistics for " + user2 + ":");
        testStatistics(user1, password1, user2);
    }
}
