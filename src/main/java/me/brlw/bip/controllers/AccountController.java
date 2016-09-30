package me.brlw.bip.controllers;

import me.brlw.bip.account.Account;
import me.brlw.bip.account.AccountCreateResponseDto;
import me.brlw.bip.account.AccountDto;
import me.brlw.bip.account.AccountService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * Created by ww on 08.09.16.
 */

@Controller
@RequestMapping(value="/account")
public class AccountController
{
    private static final Logger LOG = LogManager.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    private String getEncodedPassword(String password)
    {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<AccountCreateResponseDto> createAccount(@RequestBody(required = true) AccountDto accountDto)
    {
        LOG.debug("Creating account requested: {}", () -> accountDto.toString());
        final Account newAccount = new Account(accountDto);
        try
        {
            if (accountService.findByAccountId(newAccount.getAccountId()) != null) {
                String msg = String.format("AccountId = %s already exists", newAccount.getAccountId());
                LOG.warn("Error creating account: {}", () -> msg);
                return new ResponseEntity<AccountCreateResponseDto>(new AccountCreateResponseDto(msg), HttpStatus.CONFLICT);
            }
            else {
                final AccountCreateResponseDto responseDto = new AccountCreateResponseDto(newAccount);
                newAccount.setPassword(getEncodedPassword(newAccount.getPassword()));
                accountService.save(newAccount);
                LOG.info("Account for AccountId = {} successfully created", newAccount.getAccountId());
                return new ResponseEntity<AccountCreateResponseDto>(responseDto, HttpStatus.CREATED);
            }
        }
        catch (ConstraintViolationException ex)
        {
            String msg = ex.getConstraintViolations().stream().map( ConstraintViolation::getMessage ).collect(Collectors.joining(", "));
            LOG.info("Error creating account: {}", () -> msg);
            return new ResponseEntity<AccountCreateResponseDto>(new AccountCreateResponseDto(msg), HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex)
        {
            LOG.warn("Error creating account: ", ex);
            return new ResponseEntity<AccountCreateResponseDto>(new AccountCreateResponseDto(ex), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
