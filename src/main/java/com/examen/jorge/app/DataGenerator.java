package com.examen.jorge.app;

import com.examen.jorge.backend.data.Role;
import com.examen.jorge.backend.data.entity.Cliente;
import com.examen.jorge.backend.data.entity.User;
import com.examen.jorge.backend.repositories.ClienteRepository;
import com.examen.jorge.backend.repositories.UserRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

//    @Bean
//    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, ClienteRepository clienteRepository,
//            UserRepository userRepository) {
//        return args -> {
//            Logger logger = LoggerFactory.getLogger(getClass());
//            if (clienteRepository.count() != 0L) {
//                logger.info("Using existing database");
//                return;
//            }
//            int seed = 123;
//
//            logger.info("Generating demo data");
//
//            logger.info("... generating 100 Sample Person entities...");
//            ExampleDataGenerator<Cliente> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
//                    Cliente.class, LocalDateTime.of(2022, 1, 21, 0, 0, 0));
//            samplePersonRepositoryGenerator.setData(Cliente::setId, DataType.ID);
//            samplePersonRepositoryGenerator.setData(Cliente::setNombre, DataType.FIRST_NAME);
//            samplePersonRepositoryGenerator.setData(Cliente::setCorreoElectronico, DataType.EMAIL);
//            samplePersonRepositoryGenerator.setData(Cliente::setCelular, DataType.PHONE_NUMBER);
//            samplePersonRepositoryGenerator.setData(Cliente::setFechaNacimiento, DataType.DATE_OF_BIRTH);
//            clienteRepository.saveAll(samplePersonRepositoryGenerator.create(10, seed));
//
//            logger.info("... generating 2 User entities...");
//            User user = new User();
//            user.setName("User Normal");
//            user.setUsername("user");
//            user.setHashedPassword(passwordEncoder.encode("user"));
//            user.setRoles(Collections.singleton(Role.USER));
//            userRepository.save(user);
//            User admin = new User();
//            admin.setName("Admin");
//            admin.setUsername("admin");
//            admin.setHashedPassword(passwordEncoder.encode("admin"));
//            admin.setRoles(Stream.of(Role.USER, Role.ADMIN).collect(Collectors.toSet()));
//            userRepository.save(admin);
//
//            logger.info("Generated demo data");
//        };
//    }

}