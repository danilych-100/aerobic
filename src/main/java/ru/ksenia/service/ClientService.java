package ru.ksenia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ksenia.config.audit.AuditEventConverter;
import ru.ksenia.domain.Doctor;
import ru.ksenia.repository.DoctorRepository;
import ru.ksenia.repository.PersistenceAuditEventRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 */
@Service
@Transactional
public class ClientService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getDoctors(){
        return doctorRepository.findAll();
    }
}
