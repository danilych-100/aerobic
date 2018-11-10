package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ksenia.domain.Command;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface CommandRepository extends JpaRepository<Command, Long> {
}
