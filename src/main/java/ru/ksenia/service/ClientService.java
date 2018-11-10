package ru.ksenia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ksenia.domain.Command;
import ru.ksenia.domain.CommandCoach;
import ru.ksenia.domain.CommandMember;
import ru.ksenia.repository.CommandCoachRepository;
import ru.ksenia.repository.CommandMemberRepository;
import ru.ksenia.repository.CommandRepository;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 */
@Service
@Transactional
public class ClientService {

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandCoachRepository commandCoachRepository;

    @Autowired
    private CommandMemberRepository commandMemberRepository;

    public List<CommandDTO> getCommands(){
        List<CommandDTO> commandDTOS = new ArrayList<>();
        commandRepository.findAll().forEach(command -> {
            CommandDTO commandDto = new CommandDTO();
            commandDto.setRegion(command.getRegion());
            commandDto.setName(command.getName());
            commandDto.setAgeCategory(command.getAgeCategory());
            commandDto.setNomination(command.getNomination());
            commandDto.setMemberCount(command.getMemberCount());
            commandDto.setPhoneNumber(command.getPhoneNumber());
            commandDto.setEmail(command.getEmail());

            command.getCoaches().forEach(coach -> {
                CommandCoachDTO commandCoachDTO = new CommandCoachDTO();
                commandCoachDTO.setName(coach.getName());
                commandCoachDTO.setBirthDate(Date.from(coach.getBirthDate()));
                commandCoachDTO.setPassportDesc(coach.getPassportDesc());
                commandCoachDTO.setPassportNumber(coach.getPassportNumber());
                commandCoachDTO.setPassportSeries(coach.getPassportSeries());
                commandDto.getCoaches().add(commandCoachDTO);
            });

            command.getMembers().forEach(member -> {
                CommandMemberDTO commandMemberDTO = new CommandMemberDTO();
                commandMemberDTO.setName(member.getName());
                commandMemberDTO.setBirthDate(Date.from(member.getBirthDate()));
                commandMemberDTO.setPassportDesc(member.getPassportDesc());
                commandMemberDTO.setPassportNumber(member.getPassportNumber());
                commandMemberDTO.setPassportSeries(member.getPassportSeries());
                commandMemberDTO.setQuality(member.getQuality());
                commandDto.getMembers().add(commandMemberDTO);
            });
            commandDTOS.add(commandDto);
        });
        return commandDTOS;
    }

    @Transactional
    public void registerCommand(CommandDTO commandDTO) {
        Command command = new Command();
        command.setRegion(commandDTO.getRegion());
        command.setName(commandDTO.getName());
        command.setAgeCategory(commandDTO.getAgeCategory());
        command.setNomination(commandDTO.getNomination());
        command.setMemberCount(commandDTO.getMemberCount());
        command.setPhoneNumber(commandDTO.getPhoneNumber());
        command.setEmail(commandDTO.getEmail());

        commandDTO.getCoaches().forEach(commandCoachDTO -> {
            CommandCoach commandCoach = new CommandCoach();
            commandCoach.setName(commandCoachDTO.getName());
            commandCoach.setBirthDate(commandCoachDTO.getBirthDate().toInstant());
            commandCoach.setPassportDesc(commandCoachDTO.getPassportDesc());
            commandCoach.setPassportNumber(commandCoachDTO.getPassportNumber());
            commandCoach.setPassportSeries(commandCoachDTO.getPassportSeries());
            command.getCoaches().add(commandCoach);
        });

        commandDTO.getMembers().forEach(commandMemberDTO -> {
            CommandMember commandMember = new CommandMember();
            commandMember.setName(commandMemberDTO.getName());
            commandMember.setBirthDate(commandMemberDTO.getBirthDate().toInstant());
            commandMember.setPassportDesc(commandMemberDTO.getPassportDesc());
            commandMember.setPassportNumber(commandMemberDTO.getPassportNumber());
            commandMember.setPassportSeries(commandMemberDTO.getPassportSeries());
            commandMember.setQuality(commandMemberDTO.getQuality());
            command.getMembers().add(commandMember);
        });

        commandRepository.save(command);
    }
}
