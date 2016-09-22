package me.brlw.bip.redirection;

import me.brlw.bip.account.Account;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;

/**
 * Created by ww on 09.09.16.
 */
public interface RedirectionRepository extends CrudRepository<Redirection, Long>
{
    Redirection findByUrlAndAccount(String shortUrl, Account account);
    Redirection findByShortUrl(String shortUrl);
    Redirection findByRecno(long recNo);
}
