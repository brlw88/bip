package me.brlw.bip.redirection;

import me.brlw.bip.account.Account;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ww on 09.09.16.
 */
public interface RedirectionService
{
    Redirection findByUrlAndAccount(String url, Account account);
    Redirection findByShortUrl(String shortUrl);
    Redirection findByRecno(long recNo);
    Redirection save(Redirection redirection);

    Redirection createRedirectionWithUniqueShortUrl(RedirectionDto redirectionDto, Account principalsAccount);

    void updateStatisticsUsingKeyedLocks(Redirection thisRedirection);

    void updateStatisticsUsingOptimisticLocking(Redirection thisRedirection);

    void updateStatisticsUsingSQLUpdate(Redirection thisRedirection);
}
