package ru.ksenia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.ksenia.domain.CommandCoach;
import ru.ksenia.domain.DownloadRequest;

import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface DownloadRequestRepository extends JpaRepository<DownloadRequest, String> {

    List<DownloadRequest> getAllByRequestId(@Param("requestId") String requestId);
}
