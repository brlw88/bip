package me.brlw.bip.redirection;

import me.brlw.bip.account.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ww on 09.09.16.
 */
public interface RedirectionRepository extends CrudRepository<Redirection, Long>
{
    Redirection findByUrlAndAccount(String shortUrl, Account account);
    Redirection findByShortUrl(String shortUrl);
    Redirection findByRecno(long recNo);

    @Modifying
    @Query("UPDATE Statistics s SET s.numRedirects = s.numRedirects + 1 WHERE s.redirection= :redirection")
    @Transactional
    int incrementNumRedirects(@Param("redirection") Redirection redirection);
}
