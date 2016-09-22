package me.brlw.bip;

import me.brlw.bip.client.RestInterfaceTests;

/**
 * Created by vl on 17.09.16.
 */
public class TestClient {
    public static void main(String[] args)
    {
        RestInterfaceTests tests = new RestInterfaceTests();
        tests.runAllTests();
    }
}
