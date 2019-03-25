package ru.ksenia.service.mapper;

import ru.ksenia.domain.CommandMember;
import ru.ksenia.domain.CommandRequest;
import ru.ksenia.service.dto.ExportMemberDTO;
import ru.ksenia.service.dto.ExportRequestDTO;

import java.util.stream.Collectors;

public class ExportMapper {

    private ExportMapper(){}

    public static ExportRequestDTO mapEntityToExportDTO(CommandRequest commandRequest){
        ExportRequestDTO exportRequestDTO = new ExportRequestDTO();
        exportRequestDTO.setCommandName(commandRequest.getCommand().getName());
        exportRequestDTO.setRegion(commandRequest.getCommand().getRegion());
        exportRequestDTO.setNomination(commandRequest.getNomination());
        exportRequestDTO.setAgeCategory(commandRequest.getAgeCategory());
        exportRequestDTO.setMembers(commandRequest.getMembers()
            .stream()
            .map(ExportMapper::mapEntityToExportDTO)
            .collect(Collectors.toList())
        );

        return exportRequestDTO;
    }

    private static ExportMemberDTO mapEntityToExportDTO(CommandMember commandMember){
        ExportMemberDTO exportMemberDTO = new ExportMemberDTO();
        exportMemberDTO.setName(commandMember.getName());
        exportMemberDTO.setGender(commandMember.getGender());
        exportMemberDTO.setQuality(commandMember.getQuality());

        return exportMemberDTO;
    }
}
