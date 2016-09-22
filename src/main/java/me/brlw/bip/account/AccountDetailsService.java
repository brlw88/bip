package me.brlw.bip.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vl on 10.09.16.
 */

@Component
public class AccountDetailsService implements UserDetailsService
{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException
    {
        Account account = accountRepository.findByAccountId(accountId);

        if (account == null)
            throw new UsernameNotFoundException(String.format("Account with AccountId = %s doesn't exist", accountId));

        List<GrantedAuthority> authorities = new ArrayList<>();
        return new User(account.getAccountId(), account.getPassword(), authorities);
    }

}
