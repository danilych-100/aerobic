package ru.ksenia.service.mapper;

import ru.ksenia.domain.Command;
import ru.ksenia.domain.CommandCoach;
import ru.ksenia.domain.CommandMember;
import ru.ksenia.domain.CommandRequest;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.CommandRequestDTO;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandMapper {

    private CommandMapper(){}

    public static Command mapDTOToEntity(CommandDTO commandDTO, Command command){
        List<CommandCoach> commandCoachList = new ArrayList<>();
        commandDTO.getCoaches().forEach(commandCoachDTO -> {
            CommandCoach commandCoach = mapCommandCoachDToToEntity(commandCoachDTO);
            commandCoach.setCommand(command);
            commandCoachList.add(commandCoach);
        });

        List<CommandMember> commandMemberList = new ArrayList<>();
        commandDTO.getMembers().forEach(commandMemberDTO -> {
            CommandMember commandMember = mapCommandMemberDToToEntity(commandMemberDTO);
            commandMember.setCommand(command);
            commandMemberList.add(commandMember);
        });


        List<CommandRequest> commandRequestList = new ArrayList<>();
        commandDTO.getRequests().forEach(commandRequestDTO -> {
            CommandRequest commandRequest = null;
            try {
                commandRequest = mapCommandRequestDToToEntity(commandRequestDTO);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            List<CommandMember> commandMembersForRequest = new ArrayList<>();
            List<CommandCoach> commandCoachesForRequest = new ArrayList<>();
            for(CommandMemberDTO commandMemberDTO : commandRequestDTO.getMembers()){
                for(CommandMember commandMember : commandMemberList){
                    if(isMembersEquals(commandMember, commandMemberDTO)){
                        commandMembersForRequest.add(commandMember);
                        commandMember.setCommandRequest(commandRequest);
                        break;
                    }
                }
            }
            for(CommandCoachDTO commandCoachDTO : commandRequestDTO.getCoaches()){
                for(CommandCoach commandCoach : commandCoachList){
                    if(isCoachesEquals(commandCoach, commandCoachDTO)){
                        commandCoachesForRequest.add(commandCoach);
                        commandCoach.setCommandRequest(commandRequest);
                        break;
                    }
                }
            }

            commandRequest.setCoaches(commandCoachesForRequest);
            commandRequest.setMembers(commandMembersForRequest);
            commandRequest.setCommand(command);
            commandRequestList.add(commandRequest);
        });

        command.setRegion(commandDTO.getRegion());
        command.setName(commandDTO.getName());
        command.setMemberCount(commandDTO.getMemberCount());
        command.setPhoneNumber(commandDTO.getPhoneNumber());
        command.setEmail(commandDTO.getEmail());

        command.setCoaches(commandCoachList);
        command.setMembers(commandMemberList);
        command.setRequests(commandRequestList);

        return command;
    }


    private static boolean isCoachesEquals(CommandCoach first, CommandCoachDTO second){
        if(!first.getName().equals(second.getName())){
            return false;
        }
        if(Date.from(first.getBirthDate()).compareTo(second.getBirthDate()) != 0){
            return false;
        }

        return true;
    }

    private static boolean isMembersEquals(CommandMember first, CommandMemberDTO second){
        if(!first.getName().equals(second.getName())){
            return false;
        }
        if(!first.getGender().equals(second.getGender())){
            return false;
        }
        if(!first.getQuality().equals(second.getQuality())){
            return false;
        }
        if(Date.from(first.getBirthDate()).compareTo(second.getBirthDate()) != 0){
            return false;
        }

        return true;
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
            try {
                commandDTO.getRequests().add(mapCommandRequestEntityToDTO(commandRequest));
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        return commandDTO;
    }

    private static CommandRequest mapCommandRequestDToToEntity(CommandRequestDTO commandRequestDTO)
        throws UnsupportedEncodingException {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.setName(commandRequestDTO.getName());
        commandRequest.setAgeCategory(commandRequestDTO.getAgeCategory());
        commandRequest.setNomination(commandRequestDTO.getNomination());
        if(commandRequestDTO.getMusic() != null){
            commandRequest.setMusic(commandRequestDTO.getMusic().getBytes("UTF-8"));
        }

        commandRequest.setMusicFileName(commandRequestDTO.getMusicFileName());
        return commandRequest;
    }

    private static CommandRequestDTO mapCommandRequestEntityToDTO(CommandRequest commandRequest)
        throws UnsupportedEncodingException {
        CommandRequestDTO commandRequestDTO = new CommandRequestDTO();
        commandRequestDTO.setName(commandRequest.getName());
        commandRequestDTO.setAgeCategory(commandRequest.getAgeCategory());
        commandRequestDTO.setNomination(commandRequest.getNomination());
        if(commandRequest.getMusic() != null){
            commandRequestDTO.setMusic(new String(commandRequest.getMusic(),"UTF-8"));
        }

        commandRequestDTO.setMusicFileName(commandRequest.getMusicFileName());
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
    public static CommandCoachDTO mapCommandCoachEntityToDTO(CommandCoach commandCoach){
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

    public static CommandMemberDTO mapCommandMemberEntityToDTO(CommandMember commandMember){
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
