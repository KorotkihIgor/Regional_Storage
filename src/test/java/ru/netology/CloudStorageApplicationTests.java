package ru.netology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.configTest.MyTestConfiguration;

@SpringBootTest()
class CloudStorageApplicationTests {

    public static void main(String[] args) {
        SpringApplication.from(CloudStorageApplication::main)
                .with(MyTestConfiguration.class)
                .run(args);
    }

}
