package me.brlw.bip.redirection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ww on 09.09.16.
 */

@JsonAutoDetect
public class RedirectionDto
{
    @JsonProperty(value="url", required = true)
    public String url;

    @JsonProperty(value="redirectType", required = false)
    public Integer redirectType = Redirection.DEFAULT_REDIRECTION_TYPE;

    @Override
    public String toString() {
        return "RedirectionDto{" +
                "url='" + url + '\'' +
                ", redirectType=" + redirectType +
                '}';
    }

    public RedirectionDto()
    {}

    public RedirectionDto(String url, Integer... redirectType)
    {
        this.url = url;
        if (redirectType.length == 1)
            this.redirectType = redirectType[0];
    }
}
