package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ksenia.domain.Command;
import ru.ksenia.domain.CommandCoach;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface CommandCoachRepository extends JpaRepository<CommandCoach, Long> {
}
