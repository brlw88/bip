package me.brlw.bip.utils;

import me.brlw.bip.account.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ww on 08.09.16.
 */
public class RandomStringGenerator
{
    public final static int PASSWORD_LENGTH = 8;
    public final static int SHORT_URL_LENGTH = 6;

    private final static List<Character> symbols = new ArrayList<>();

    static
    {
        for( int cc=(int)'A'; cc <= (int)'Z'; cc++)
            symbols.add((char)cc);
        for( int cc=(int)'a'; cc <= (int)'z'; cc++)
            symbols.add((char)cc);
        for( int cc=(int)'0'; cc <= (int)'9'; cc++)
            symbols.add((char)cc);
    }

    public static String generatePassword()
    {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (true)
        {
            sb.setLength(0);
            boolean hasDigits = false, hasLetters = false, hasCapitals = false;
            for (int i=0; i<PASSWORD_LENGTH; i++) {
                char cc = symbols.get(r.nextInt(symbols.size()));
                hasDigits |= cc >= (byte) '0' && cc <= (byte) '9';
                hasLetters |= cc >= (byte) 'a' && cc <= (byte) 'z';
                hasCapitals |= cc >= (byte) 'A' && cc <= (byte) 'Z';
                sb.append(cc);
            }
            if (hasCapitals && hasLetters && hasDigits)
                break;
        }
        return sb.toString();
    }

    public static String generateShortUrl()
    {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (true)
        {
            sb.setLength(0);
            boolean hasLetters = false, hasCapitals = false;
            for (int i=0; i<SHORT_URL_LENGTH; i++) {
                char cc = symbols.get(r.nextInt(symbols.size() - 10));
                hasLetters |= cc >= (byte) 'a' && cc <= (byte) 'z';
                hasCapitals |= cc >= (byte) 'A' && cc <= (byte) 'Z';
                sb.append(cc);
            }
            if (hasCapitals && hasLetters)
                break;
        }
        return sb.toString();
    }

    public static String generateUsername()
    {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        int nameLength = Account.MIN_ACCOUNT_ID_LENGTH + r.nextInt(Account.MAX_ACCOUNT_ID_LENGTH - Account.MIN_ACCOUNT_ID_LENGTH );
        for (int i=0; i<nameLength; i++) {
            char cc;
            if (i == 0)
                cc = symbols.get(r.nextInt(symbols.size() - 10));
            else
                cc = symbols.get(r.nextInt(symbols.size()));
            sb.append(cc);
        }
        return sb.toString();

    }
}
