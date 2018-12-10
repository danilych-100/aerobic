package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.ksenia.domain.CoachJoinTable;
import ru.ksenia.domain.MemberJoinTable;

import java.util.List;

public interface MemberJoinRepository extends JpaRepository<MemberJoinTable, Long> {

    public List<MemberJoinTable> findAllByUserId(@Param("userId") Long userId);
}
