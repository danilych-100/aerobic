package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.ksenia.domain.Command;

import javax.persistence.Lob;
import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface CommandRepository extends JpaRepository<Command, Long> {

    List<Command> findAllByUserId(@Param("userId") Long userId);


}
