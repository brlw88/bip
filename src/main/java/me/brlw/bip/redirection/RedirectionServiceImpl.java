package me.brlw.bip.redirection;

import me.brlw.bip.account.Account;
import me.brlw.bip.exception.NumRetriesExceededException;
import me.brlw.bip.statistics.Statistics;
import me.brlw.bip.utils.KeyedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.Lock;

/**
 * Created by ww on 09.09.16.
 */

@Service("redirectionService")
@Repository
@Transactional
public class RedirectionServiceImpl implements RedirectionService
{
    private static final int NUM_RETRIES = 50;

    @Autowired
    private RedirectionRepository redirectionRepository;

    private static final KeyedLock<String> shortUrlLocks = new KeyedLock<>();


    @Override
    @Transactional(readOnly = true)
    public Redirection findByUrlAndAccount(String url, Account account) {
        return redirectionRepository.findByUrlAndAccount(url, account);
    }
    @Override
    @Transactional(readOnly = true)
    public Redirection findByShortUrl(String shortUrl) {
        return redirectionRepository.findByShortUrl(shortUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public Redirection findByRecno(long recNo) {
        return redirectionRepository.findByRecno(recNo);
    }

    @Override
    public Redirection save(Redirection redirection) {
        return redirectionRepository.save(redirection);
    }

    @Override
    public Redirection createRedirectionWithUniqueShortUrl(RedirectionDto redirectionDto, Account principalsAccount)
    {
        Redirection newRedirection;
        int retries = 0;
        do {
            newRedirection = new Redirection(redirectionDto);
            if (redirectionRepository.findByShortUrl(newRedirection.getShortUrl()) == null)
                break;
        }
        while (++retries < NUM_RETRIES);
        if (retries == NUM_RETRIES)
            throw new NumRetriesExceededException("Failed to create unique short URL: retries count exceeded");
        newRedirection.setAccount(principalsAccount);
        redirectionRepository.save(newRedirection);
        return newRedirection;
    }

    @Override
    public void updateStatisticsUsingKeyedLocks(Redirection thisRedirection) {
        Lock shortUrlLock = shortUrlLocks.getLockFor(thisRedirection.getShortUrl());
        shortUrlLock.lock();
        try {
            thisRedirection = redirectionRepository.findByRecno(thisRedirection.getRecno());
            Statistics statistics = thisRedirection.getStatistics();
            if (statistics == null)
                thisRedirection.addStatistics(new Statistics());
            else
                statistics.setNumRedirects(statistics.getNumRedirects() + 1);
            redirectionRepository.save(thisRedirection);
        } finally {
            shortUrlLock.unlock();
        }
    }

    @Override
    @Modifying
    @Transactional(propagation = Propagation.NEVER)
    public void updateStatisticsUsingOptimisticLocking(Redirection thisRedirection)
    {
        final int NUM_RETRIES = 50;

        int retries = 0;
        do {
            Statistics statistics = thisRedirection.getStatistics();
            if (statistics == null)
                thisRedirection.addStatistics(new Statistics());
            else
                statistics.setNumRedirects(statistics.getNumRedirects() + 1);
            try {
                redirectionRepository.save(thisRedirection);
                break;
            } catch (ObjectOptimisticLockingFailureException | DataIntegrityViolationException ex) {
                thisRedirection = redirectionRepository.findByRecno(thisRedirection.getRecno());
            }
        }
        while (++retries < NUM_RETRIES);
        if (retries == NUM_RETRIES)
            throw new NumRetriesExceededException("Failed to update statistics: retries count exceeded");
    }

}
