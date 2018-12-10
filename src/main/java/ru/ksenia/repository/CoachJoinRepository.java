package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.ksenia.domain.CoachJoinTable;

import java.util.List;

public interface CoachJoinRepository extends JpaRepository<CoachJoinTable, Long> {

    public List<CoachJoinTable> findAllByUserId(@Param("userId") Long userId);
}
