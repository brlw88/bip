# 
URL Shortener REST service
    <h1>URL Shortener REST service</h1>
    <h2>About</h2>
    <p>This is <b>URL Shortener</b> - an HTTP service that serves to shorten URLs, with the following functionalties:</p>
    <ul>
        <li>Registration of web addresses (URLs) using API;</li>
        <li>Redirect clients using the shortened URLs;</li>
        <li>Getting service usage statistics.</li>
    </ul>
    <p>The service has two parts: configuration and user.</p>
    <p>The configuration part is invoked using REST calls with JSON payload and is used for:</p>
    <ul>
        <li>Opening of accounts</li>
        <li>Registration of URLs in the <b>URL Shortener</b> service</li>
        <li>Getting usage statistics</li>
    </ul>
    <p>The redirecting part redirecting the client on the configured address with the configured http status.</p>
    <p>To get the help page with more detailed information, run the <b>URL Shortener</b> service and open it's home page in your browser.</p>
    <h2>Application created using:</h2>
    <ul>
        <li>Java 8</li>
        <li>Tomcat 8.5</li>
        <li>Spring, Spring JPA, Spring MVC, Spring Security</li>
        <li>Hibernate</li>
        <li>H2 Database</li>
        <li>Jackson</li>
        <li>Log4j</li>
        <li>Maven</li>
    </ul>
    <h2>How to run</h2>
    <p>To build project, execute command</p>
    <code>mvn clean package</code>
    <p>This command builds both <b>bip.war</b> and <b>bip.jar</b> and puts them in /target folder</p>
    <p>Automatic deploy to tomcat server implemented using <b>cargo-maven2-plugin</b>, username & password for tomcat manager
    located in <b>pom.xml</b> and are set to tomcat/tomcat. To deploy, execute command</p>
    <code>mvn cargo:redeploy</code>
    <p>Log file is located in <b>${catalina.home}/logs/bip.log</b>, where ${catalina.home} is the root of tomcat installation.
    Log4j is set up for automatic rotation of log file.</p>
    <p>To run test client, from root of project execute command</p>
    <code>java -jar target/bip.jar</code>
    <p>The test client:</p>
    <ul>
        <li>Adds 2 accounts;</li>
        <li>From each account registers 5 redirections;</li>
        <li>Creates and runs 30 threads, each thread than makes 100 random requests for redirection;</li>
        <li>Gets statistics for both accounts, for each account there should be registered 3000 redirects.</li>
    </ul>
    <p>There are two implementations of redirect statistic collection:</p>
    <ul>
        <li>Using ReentrantLock syncronization, it's faster, but suitable only for single-node configuration;</li>
        <li>Using Hibernate optimistic locking mechanism, it's slower, but can be used with cluster.</li>
    </ul>
    <p>The test client tests both implementation and shows runtime in ms. For accurate results, run test client 5-10 times for warm-up.</p>
