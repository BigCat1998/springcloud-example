package com.me.ordinarycloud.tm;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 大猫
 */
@EnableTransactionManagerServer
@SpringBootApplication
public class TmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmApplication.class, args);
    }

}
