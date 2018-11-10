package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ksenia.domain.Authority;
import ru.ksenia.domain.Client;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
