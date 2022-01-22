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

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, ClienteRepository clienteRepository,
            UserRepository userRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (clienteRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Person entities...");
            ExampleDataGenerator<Cliente> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
                    Cliente.class, LocalDateTime.of(2022, 1, 21, 0, 0, 0));
            samplePersonRepositoryGenerator.setData(Cliente::setId, DataType.ID);
            samplePersonRepositoryGenerator.setData(Cliente::setNombre, DataType.FIRST_NAME);
            samplePersonRepositoryGenerator.setData(Cliente::setCorreoElectronico, DataType.EMAIL);
            samplePersonRepositoryGenerator.setData(Cliente::setCelular, DataType.PHONE_NUMBER);
            samplePersonRepositoryGenerator.setData(Cliente::setFechaNacimiento, DataType.DATE_OF_BIRTH);
            clienteRepository.saveAll(samplePersonRepositoryGenerator.create(100, seed));

            logger.info("... generating 2 User entities...");
            User user = new User();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);
            User admin = new User();
            admin.setName("Emma Powerful");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            admin.setRoles(Stream.of(Role.USER, Role.ADMIN).collect(Collectors.toSet()));
            userRepository.save(admin);

            logger.info("Generated demo data");
        };
    }

}