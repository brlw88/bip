package me.brlw.bip.account;

import java.util.List;

/**
 * Created by ww on 08.09.16.
 */
public interface AccountService
{
    Account findByAccountId(String id);
    Account save(Account account);
}
