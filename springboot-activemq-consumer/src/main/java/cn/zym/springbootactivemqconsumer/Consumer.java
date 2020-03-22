package cn.zym.springbootactivemqconsumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @JmsListener(destination = "${queue}")
    public void receive(String msg) {
        System.out.println("接收到一条msg:"+msg);
    }


}
