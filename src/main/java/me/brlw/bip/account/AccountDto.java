package me.brlw.bip.account;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ww on 09.09.16.
 */

@JsonAutoDetect
public class AccountDto
{
    @JsonProperty(value="AccountId", required = true)
    public String id;

    public AccountDto()
    {}

    public AccountDto(String id)
    {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "id='" + id + '\'' +
                '}';
    }
}
