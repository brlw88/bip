package me.brlw.bip.account;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by ww on 08.09.16.
 */
public interface AccountRepository extends CrudRepository<Account, Long>
{
    Account findByAccountId(String id);
}
