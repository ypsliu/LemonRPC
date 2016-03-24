package lemon.rpc.examples;

import com.lemon.rpcframe.provider.ProviderMain;

public class ProviderTest {

    public static void main(String[] args) {
        try {
            final ProviderMain provider = new ProviderMain();
            provider.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
