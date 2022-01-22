package com.examen.jorge.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.examen.jorge.backend.data.entity.Cliente;
import com.examen.jorge.backend.repositories.ClienteRepository;


@Service
public class ClienteService extends CrudService<Cliente, Integer> {

    private ClienteRepository repository;

    public ClienteService(@Autowired ClienteRepository repository) {
        this.repository = repository;
    }

    public Optional<Cliente> get(Integer id) {
        return repository.findById(id);
    }

    public Cliente update(Cliente entity) {
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<Cliente> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public  List<Cliente> getAll() {
        return getRepository().findAll();
    }
    
    protected ClienteRepository getRepository() {
        return repository;
    }
    
}
