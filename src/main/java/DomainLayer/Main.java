package DomainLayer;

import PresentationLayer.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"DomainLayer", "Util"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
}
