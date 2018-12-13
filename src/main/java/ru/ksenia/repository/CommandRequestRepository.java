package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ksenia.domain.Command;
import ru.ksenia.domain.CommandRequest;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface CommandRequestRepository extends JpaRepository<CommandRequest, String> {
}
