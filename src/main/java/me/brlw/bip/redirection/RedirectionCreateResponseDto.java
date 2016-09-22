package me.brlw.bip.redirection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by ww on 09.09.16.
 */

@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedirectionCreateResponseDto
{
    public String shortUrl;

    public RedirectionCreateResponseDto()
    {}

    public RedirectionCreateResponseDto(Redirection redirection)
    {
        shortUrl = redirection.getShortUrl();
    }

    public RedirectionCreateResponseDto(Throwable t)
    {
        shortUrl = null;
    }

    public RedirectionCreateResponseDto(String s)
    {
        shortUrl = null;
    }

    @Override
    public String toString() {
        return "RedirectionCreateResponseDto{" +
                "shortUrl='" + shortUrl + '\'' +
                '}';
    }
}
