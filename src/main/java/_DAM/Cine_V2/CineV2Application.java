package _DAM.Cine_V2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CineV2Application {

	public static void main(String[] args) {
		SpringApplication.run(CineV2Application.class, args);
	}

}
