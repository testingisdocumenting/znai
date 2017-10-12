package com.example.demo.springboot.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Iterable<Customer> findAllByOrderByLastName();

    @Override
    void deleteAll();
}
