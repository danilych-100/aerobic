package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ksenia.domain.CommandCoach;
import ru.ksenia.domain.DownloadRequest;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface DownloadRequestRepository extends JpaRepository<DownloadRequest, String> {
}
