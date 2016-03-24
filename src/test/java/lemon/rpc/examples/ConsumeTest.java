package lemon.rpc.examples;

import com.lemon.rpcframe.consumer.ConsumerMain;

public class ConsumeTest {

    public static void main(String[] args) {
        try {
            final ConsumerMain task = new ConsumerMain();
            task.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
