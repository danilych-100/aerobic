package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ksenia.domain.Client;
import ru.ksenia.domain.Doctor;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
