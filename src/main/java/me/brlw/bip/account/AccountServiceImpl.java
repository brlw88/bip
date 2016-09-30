package me.brlw.bip.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ww on 08.09.16.
 */

@Service("accountService")
@Repository
public class AccountServiceImpl implements AccountService
{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account findByAccountId(String id)
    {
        return accountRepository.findByAccountId(id);
    }

    @Override
    public Account save(Account account)
    {
        return accountRepository.save(account);
    }
}
