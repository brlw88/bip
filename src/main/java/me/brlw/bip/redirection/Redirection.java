package me.brlw.bip.redirection;

import me.brlw.bip.utils.RandomStringGenerator;
import me.brlw.bip.account.Account;
import me.brlw.bip.statistics.Statistics;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by ww on 09.09.16.
 */

@Entity
@Table(name = "redirections")
public class Redirection
{
    public static final int DEFAULT_REDIRECTION_TYPE = 302;
    public static final int MIN_REDIRECTION_TYPE = 301;
    public static final int MAX_REDIRECTION_TYPE = 302;

    public static final int MIN_URL_LENGTH = 7;
    public static final int MAX_URL_LENGTH = 1024;

    private Long recno;
    private int version;

    private String url;
    private String shortUrl;
    private Integer redirectType = DEFAULT_REDIRECTION_TYPE;
    private Statistics statistics;
    private Account account;

    public Redirection()
    {
    }

    public Redirection(RedirectionDto redirectionDto)
    {
        this.url = redirectionDto.url;
        this.redirectType = redirectionDto.redirectType;
        this.shortUrl = RandomStringGenerator.generateShortUrl();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recno")
    public Long getRecno() {
        return recno;
    }

    public void setRecno(Long recno) {
        this.recno = recno;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @NotEmpty(message="URL can't be empty")
    @URL(message = "URL is not valid")
    @Size(min=MIN_URL_LENGTH,
            max=MAX_URL_LENGTH,
            message="Length of URL must be between {min} and {max}")
    @Column(name = "url", nullable = false)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NaturalId
    @NotEmpty(message="Short URL can't be empty")
    @Pattern(regexp = "^[A-Za-z0-9]{" + RandomStringGenerator.SHORT_URL_LENGTH + "}$",
             message = "Short URL must be " + RandomStringGenerator.SHORT_URL_LENGTH + " letters")
    @Column(name = "shorturl", unique = true, nullable = false)
    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @Column(name = "type", nullable = false)
    @Range(min=MIN_REDIRECTION_TYPE,
            max=MAX_REDIRECTION_TYPE,
            message="Redirection type must be between {min} and {max}")
    public int getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(int redirectType) {
        this.redirectType = redirectType;
    }

    @OneToOne(mappedBy = "redirection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void addStatistics(Statistics statistics) {
        statistics.setRedirection(this);
        this.statistics = statistics;
    }

    public void removeStatistics() {
        if (statistics != null) {
            statistics.setRedirection(null);
            this.statistics = null;
        }
    }

    @ManyToOne
    @JoinColumn(name="acc_recno", nullable = false)
    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Redirection{" +
                "recno=" + recno +
                ", version=" + version +
                ", url='" + url + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", redirectType=" + redirectType +
                ", account=" + account +
                '}';
    }
}
