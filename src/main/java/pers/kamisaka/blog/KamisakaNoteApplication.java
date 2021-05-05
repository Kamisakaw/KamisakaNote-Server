package pers.kamisaka.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@EnableCaching
public class KamisakaNoteApplication {
	public static void main(String[] args) {
		SpringApplication.run(KamisakaNoteApplication.class, args);
	}

}
