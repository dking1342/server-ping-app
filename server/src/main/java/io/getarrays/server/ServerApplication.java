package io.getarrays.server;

import io.getarrays.server.enums.Status;
import io.getarrays.server.model.Server;
import io.getarrays.server.repository.ServerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		System.out.println("server listening...");
	}

	@Bean
	CommandLineRunner run(ServerRepository serverRepository){
		return args -> {
			serverRepository.save(new Server(
					null,
					"192.168.1.168",
					"Ubuntu Linux",
					"16 GB",
					"Personal PC",
					"http://localhost:8080/server/image/server1.png",
					Status.SERVER_UP
				)
			);
			serverRepository.save(new Server(
							null,
							"127.0.0.1",
							"Windows",
							"32 GB",
							"Personal Compupter",
							"http://localhost:8080/server/image/server1.png",
							Status.SERVER_DOWN
				)
			);
		};
	}
	
	@Bean
	public CorsFilter corsFilter(){
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList(
				"http://localhost:3000",
				"http://localhost:4200"
		));
		corsConfiguration.setAllowedHeaders(Arrays.asList(
				"Origin",
				"Access-Control-Allow-Origin",
				"Content-Type",
				"Accept",
				"Jwt-Token",
				"Authorization",
				"Origin, Accept",
				"X-Requested-With",
				"Access-Control-Request-Method",
				"Access-Control-Request-Headers"
		));
		corsConfiguration.setExposedHeaders(Arrays.asList(
				"Origin",
				"Content-Type",
				"Accept",
				"Jwt-Token",
				"Authorization",
				"Access-Control-Allow-Origin",
				"Access-Control-Allow-Origin",
				"Access-Control-Allow-Credentials",
				"Filename"
		));
		corsConfiguration.setAllowedMethods(Arrays.asList(
				"GET",
				"POST",
				"PUT",
				"PATCH",
				"DELETE",
				"OPTIONS"
		));
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
}
