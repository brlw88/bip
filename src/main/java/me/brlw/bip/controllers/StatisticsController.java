package me.brlw.bip.controllers;

import me.brlw.bip.account.Account;
import me.brlw.bip.account.AccountService;
import me.brlw.bip.exception.ResourceNotFoundException;
import me.brlw.bip.statistics.Statistics;
import me.brlw.bip.statistics.StatisticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by ww on 09.09.16.
 */

@Controller
public class StatisticsController {
    private static final Logger LOG = LogManager.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET, path="/statistic/{AccountId}", produces = "application/json")
    public ResponseEntity<Map<String,Long>> get(@PathVariable("AccountId") String accountId, Principal principal)
    {
        LOG.debug("User: {} - reqesting statistics for AccountId={}", ()->principal.getName(), ()->accountId);
        Map<String, Long> resultMap = new HashMap<String, Long>();
        try {
            final Account account = accountService.findByAccountId(accountId);
            if (account == null)
                throw new ResourceNotFoundException("Account with id=" + accountId + " not found");

            Stream<Statistics> statisticsStream = statisticsService.findAllByAccountId(accountId);
            statisticsStream.forEach(entry -> resultMap.put(entry.getRedirection().getUrl(), entry.getNumRedirects()));
            LOG.info("Statistics containing {} entries for AccountId={} successfully collected", resultMap.size(), accountId );
            return ResponseEntity.ok(resultMap);
        }
        catch (ResourceNotFoundException ex)
        {
            LOG.info("Error getting statistics: {}", () -> ex.getMessage());
            return new ResponseEntity(Collections.EMPTY_MAP, HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex)
        {
            LOG.warn("Error getting statistics: ", ex);
            return new ResponseEntity(Collections.EMPTY_MAP, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
