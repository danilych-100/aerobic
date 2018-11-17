package ru.ksenia.service.mapper;

import ru.ksenia.domain.Command;
import ru.ksenia.domain.CommandCoach;
import ru.ksenia.domain.CommandMember;
import ru.ksenia.domain.CommandRequest;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.CommandRequestDTO;

import java.util.Date;

public class CommandMapper {

    private CommandMapper(){}

    public static Command mapDTOToEntity(CommandDTO commandDTO, Command command){
        command.setRegion(commandDTO.getRegion());
        command.setName(commandDTO.getName());
        command.setMemberCount(commandDTO.getMemberCount());
        command.setPhoneNumber(commandDTO.getPhoneNumber());
        command.setEmail(commandDTO.getEmail());

        commandDTO.getCoaches().forEach(commandCoachDTO -> {
            command.getCoaches().add(mapCommandCoachDToToEntity(commandCoachDTO));
        });
        commandDTO.getMembers().forEach(commandMemberDTO -> {
            command.getMembers().add(mapCommandMemberDToToEntity(commandMemberDTO));
        });
        commandDTO.getRequests().forEach(commandRequestDTO -> {
            command.getRequests().add(mapCommandRequestDToToEntity(commandRequestDTO));
        });

        return command;
    }

    public static CommandDTO mapEntityToDTO(Command command){
        CommandDTO commandDTO = new CommandDTO();
        commandDTO.setRegion(command.getRegion());
        commandDTO.setName(command.getName());
        commandDTO.setMemberCount(command.getMemberCount());
        commandDTO.setPhoneNumber(command.getPhoneNumber());
        commandDTO.setEmail(command.getEmail());

        command.getCoaches().forEach(commandCoach -> {
            commandDTO.getCoaches().add(mapCommandCoachEntityToDTO(commandCoach));
        });
        command.getMembers().forEach(commandMember -> {
            commandDTO.getMembers().add(mapCommandMemberEntityToDTO(commandMember));
        });
        command.getRequests().forEach(commandRequest -> {
            commandDTO.getRequests().add(mapCommandRequestEntityToDTO(commandRequest));
        });

        return commandDTO;
    }

    private static CommandRequest mapCommandRequestDToToEntity(CommandRequestDTO commandRequestDTO){
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.setName(commandRequestDTO.getName());
        commandRequest.setAgeCategory(commandRequestDTO.getAgeCategory());
        commandRequest.setNomination(commandRequestDTO.getNomination());
        commandRequest.setMusic(commandRequestDTO.getMusic());
        commandRequestDTO.getCoaches().forEach(commandCoachDTO -> {
            commandRequest.getCoaches().add(mapCommandCoachDToToEntity(commandCoachDTO));
        });
        commandRequestDTO.getMembers().forEach(commandMemberDTO -> {
            commandRequest.getMembers().add(mapCommandMemberDToToEntity(commandMemberDTO));
        });
        return commandRequest;
    }

    private static CommandRequestDTO mapCommandRequestEntityToDTO(CommandRequest commandRequest){
        CommandRequestDTO commandRequestDTO = new CommandRequestDTO();
        commandRequestDTO.setName(commandRequest.getName());
        commandRequestDTO.setAgeCategory(commandRequest.getAgeCategory());
        commandRequestDTO.setNomination(commandRequest.getNomination());
        commandRequestDTO.setMusic(commandRequest.getMusic());
        commandRequest.getCoaches().forEach(commandCoach -> {
            commandRequestDTO.getCoaches().add(mapCommandCoachEntityToDTO(commandCoach));
        });
        commandRequest.getMembers().forEach(commandMember -> {
            commandRequestDTO.getMembers().add(mapCommandMemberEntityToDTO(commandMember));
        });
        return commandRequestDTO;
    }

    private static CommandCoach mapCommandCoachDToToEntity(CommandCoachDTO commandCoachDTO){
        CommandCoach commandCoach = new CommandCoach();
        commandCoach.setName(commandCoachDTO.getName());
        commandCoach.setBirthDate(commandCoachDTO.getBirthDate().toInstant());
        commandCoach.setPassportDesc(commandCoachDTO.getPassportDesc());
        commandCoach.setPassportNumber(commandCoachDTO.getPassportNumber());
        commandCoach.setPassportSeries(commandCoachDTO.getPassportSeries());
        return commandCoach;
    }
    private static CommandCoachDTO mapCommandCoachEntityToDTO(CommandCoach commandCoach){
        CommandCoachDTO commandCoachDTO = new CommandCoachDTO();
        commandCoachDTO.setName(commandCoach.getName());
        commandCoachDTO.setBirthDate(Date.from(commandCoach.getBirthDate()));
        commandCoachDTO.setPassportDesc(commandCoach.getPassportDesc());
        commandCoachDTO.setPassportNumber(commandCoach.getPassportNumber());
        commandCoachDTO.setPassportSeries(commandCoach.getPassportSeries());
        return commandCoachDTO;
    }

    private static CommandMember mapCommandMemberDToToEntity(CommandMemberDTO commandMemberDTO){
        CommandMember commandMember = new CommandMember();
        commandMember.setName(commandMemberDTO.getName());
        commandMember.setGender(commandMemberDTO.getGender());
        commandMember.setBirthDate(commandMemberDTO.getBirthDate().toInstant());
        commandMember.setPassportDesc(commandMemberDTO.getPassportDesc());
        commandMember.setPassportNumber(commandMemberDTO.getPassportNumber());
        commandMember.setPassportSeries(commandMemberDTO.getPassportSeries());
        commandMember.setBirthCertificateNumber(commandMemberDTO.getBirthCertificateNumber());
        commandMember.setBirthCertificateDesc(commandMemberDTO.getBirthCertificateDesc());
        commandMember.setQuality(commandMemberDTO.getQuality());
        return commandMember;
    }

    private static CommandMemberDTO mapCommandMemberEntityToDTO(CommandMember commandMember){
        CommandMemberDTO commandMemberDTO = new CommandMemberDTO();
        commandMemberDTO.setName(commandMember.getName());
        commandMemberDTO.setGender(commandMember.getGender());
        commandMemberDTO.setBirthDate(Date.from(commandMember.getBirthDate()));
        commandMemberDTO.setPassportDesc(commandMember.getPassportDesc());
        commandMemberDTO.setPassportNumber(commandMember.getPassportNumber());
        commandMemberDTO.setPassportSeries(commandMember.getPassportSeries());
        commandMemberDTO.setBirthCertificateNumber(commandMember.getBirthCertificateNumber());
        commandMemberDTO.setBirthCertificateDesc(commandMember.getBirthCertificateDesc());
        commandMemberDTO.setQuality(commandMember.getQuality());
        return commandMemberDTO;
    }
}
