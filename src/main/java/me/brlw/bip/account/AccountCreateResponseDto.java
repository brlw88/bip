package me.brlw.bip.account;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by ww on 08.09.16.
 */

public class AccountCreateResponseDto
{
    public boolean success;
    public String description;
    public String password;

    public AccountCreateResponseDto()
    {}

    public AccountCreateResponseDto(Account account)
    {
        success = true;
        description = "Your account is opened";
        password = account.getPassword();
    }

    public AccountCreateResponseDto(Throwable t)
    {
        success = false;
        description = t.getMessage();
        password = null;
    }

    public AccountCreateResponseDto(String s)
    {
        success = false;
        description = s;
        password = null;
    }

    @Override
    public String toString() {
        return "AccountCreateResponseDto{" +
                "success=" + success +
                ", description='" + description + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
