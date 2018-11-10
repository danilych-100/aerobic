package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ksenia.domain.CommandCoach;
import ru.ksenia.domain.CommandMember;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface CommandMemberRepository extends JpaRepository<CommandMember, Long> {
}
