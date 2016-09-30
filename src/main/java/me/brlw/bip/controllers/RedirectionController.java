package me.brlw.bip.controllers;

import me.brlw.bip.account.Account;
import me.brlw.bip.account.AccountService;
import me.brlw.bip.redirection.Redirection;
import me.brlw.bip.redirection.RedirectionCreateResponseDto;
import me.brlw.bip.redirection.RedirectionDto;
import me.brlw.bip.redirection.RedirectionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ww on 09.09.16.
 */

@Controller
public class RedirectionController
{
    private static final Logger LOG = LogManager.getLogger(RedirectionController.class);

    @Autowired
    private RedirectionService redirectionService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST, path = "/register", produces = "application/json")
    public ResponseEntity<RedirectionCreateResponseDto> registerRedirection(@RequestBody(required = true) RedirectionDto redirectionDto, Principal principal)
    {
        LOG.debug("User: {} -  creating short url for: {}", ()->principal.getName(), ()->redirectionDto.toString());
        try
        {
            final Account principalsAccount = accountService.findByAccountId(principal.getName());
            if (principalsAccount == null)
                throw new Exception("Account with id=" + principal.getName() + " not found");
            synchronized (RedirectionController.class) {
                final Redirection existingRedirection = redirectionService.findByUrlAndAccount(redirectionDto.url, principalsAccount);
                if (existingRedirection != null)
                    return ResponseEntity.ok(new RedirectionCreateResponseDto(existingRedirection));
                else {
                    final Redirection newRedirection = redirectionService.createRedirectionWithUniqueShortUrl(redirectionDto, principalsAccount);
                    LOG.info("User: {} - short URL for URL = {} redirection successfully created", () -> principal.getName(), () -> redirectionDto.url);
                    return new ResponseEntity<RedirectionCreateResponseDto>(new RedirectionCreateResponseDto(newRedirection), HttpStatus.CREATED);
                }
            }
        }
        catch (ConstraintViolationException ex)
        {
            String msg = ex.getConstraintViolations().stream().map( ConstraintViolation::getMessage ).collect(Collectors.joining(", "));
            LOG.info("Error creating redirection: {}", () -> msg);
            return new ResponseEntity<RedirectionCreateResponseDto>(new RedirectionCreateResponseDto(msg), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex)
        {
            LOG.warn("Error creating redirection: ", ex);
            return new ResponseEntity<RedirectionCreateResponseDto>(new RedirectionCreateResponseDto(ex), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void createRedirectHttpResponse(HttpServletResponse httpServletResponse, String url, int redirectType) throws IOException
    {
        try (PrintWriter resPrintWriter = httpServletResponse.getWriter()) {
            httpServletResponse.setStatus(redirectType);
            httpServletResponse.setHeader("Location", url);
            resPrintWriter.write("HTTP/1.1 " + redirectType + " Redirect");
            resPrintWriter.flush();
        }
    }

    @RequestMapping(method = RequestMethod.GET,
            path= { "/{shortUrl:[A-Za-z]{6}}",
                    "/{shortUrl:[A-Za-z]{6}}/{alternativeImplementation:[0-9]{1}}"
            })
    public void redirect(@PathVariable String shortUrl, @PathVariable Optional<String> alternativeImplementation, HttpServletResponse httpServletResponse)
    {
        LOG.debug("Redirecting to shortURL: {}", shortUrl);

        try {
            Redirection thisRedirection = redirectionService.findByShortUrl(shortUrl);
            if (thisRedirection != null) {
                createRedirectHttpResponse(httpServletResponse, thisRedirection.getUrl(), thisRedirection.getRedirectType());

                if (!alternativeImplementation.isPresent())
                    redirectionService.updateStatisticsUsingKeyedLocks(thisRedirection);
                else
//                    redirectionService.updateStatisticsUsingOptimisticLocking(thisRedirection);
                    redirectionService.updateStatisticsUsingSQLUpdate(thisRedirection);

                LOG.debug("Successful redirect toURL: {}", () -> thisRedirection.getUrl());
            } else
                httpServletResponse.sendError(404, "HTTP/1.1 404 Short URL Not Found");
        }
        catch (Exception ex)
        {
            LOG.warn("Error creating redirect response: ", ex);
            try {
                httpServletResponse.sendError(500, "HTTP/1.1 500 Internal server error");
            }
            catch (IOException ioex) {
                LOG.warn("Error creating 500 response: ", ioex);
            }
        }
    }

}
