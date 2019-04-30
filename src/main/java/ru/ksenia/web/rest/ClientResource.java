package ru.ksenia.web.rest;

import org.apache.commons.compress.utils.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ksenia.domain.CommandRequest;
import ru.ksenia.domain.DownloadRequest;
import ru.ksenia.repository.DownloadRequestRepository;
import ru.ksenia.service.ClientService;
import ru.ksenia.service.util.report.Report;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.CommandRequestDTO;
import ru.ksenia.web.rest.dto.admin.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private DownloadRequestRepository downloadRequestRepository;

    @GetMapping("/getCommand")
    public ResponseEntity<CommandDTO> getCommand(@RequestParam Long commandId) {
        return ResponseEntity.ok(clientService.getCommand(commandId));
    }

    @GetMapping("/getAllCommandUserInfo")
    public ResponseEntity<List<CommandUserInfoDTO>> getAllCommandUserInfo() {
        return ResponseEntity.ok(clientService.getAllCommandUserInfo());

    }

    @GetMapping("/getRequestInfo")
    public ResponseEntity<RequestInfoDTO> getRequestInfo(@RequestParam String requestId)
        throws UnsupportedEncodingException {
        return ResponseEntity.ok(clientService.getRequestInfo(requestId));
    }

    @GetMapping("/getAllRequests")
    public ResponseEntity<List<CommandRequestAdminInfoDTO>> getAllRequests() throws UnsupportedEncodingException {
        return ResponseEntity.ok(clientService.getAllRequests());
    }

    @GetMapping("/getCommandForCurrentUser")
    public ResponseEntity<CommandDTO> getCommandForCurrentUser() {
        try {
            return ResponseEntity.ok(clientService.getCommandForCurrentUser());
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/updateCommand")
    public ResponseEntity<Void> updateCommand(@RequestBody CommandDTO command) {
        try {
            clientService.updateCommand(command);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/downloadFileMusic")
    public @ResponseBody ResponseEntity<byte[]> downloadFileMusic(final @RequestBody(required = true)DonwloadFileRequest
                                                          donwloadFileReques, HttpServletResponse response) {

        String[] splittedFileName = donwloadFileReques.getMusicFileName().split(".");
        String ext = splittedFileName.length == 2 ? splittedFileName[1] : "mp3";

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.YY-HHmm");

        response.setHeader("Content-Disposition", String.format("attachment; filename=%s", donwloadFileReques.getMusicFileName()));
        response.setContentType("audio/mpeg");

        return ResponseEntity.ok(donwloadFileReques.getMusicFile().getBytes());
    }

    @PostMapping(value = "/saveFileToRequest")
    @ResponseStatus(HttpStatus.OK)
    public void saveFileToRequest(@RequestParam("file") MultipartFile file,
                                  @RequestParam("requestId") String requestId,
                                  @RequestParam("requestName") String requestName,
                                  @RequestParam("musicFileName") String musicFileName) throws IOException {
        clientService.saveFileToDownloadRequest(requestId, file.getBytes(), requestName, musicFileName);
    }

    @GetMapping("/downloadMusicFile")
    public void saveDownloadedMusicFile(final @RequestParam(required = true)String id,
                                        final @RequestParam(required = true)String commandName,
                                        final HttpServletResponse httpServletResponse) throws IOException {

        log.debug("Request for downloadMusicFile. Command Request Id : " + id);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        DownloadRequest downloadRequest = downloadRequestRepository.getAllByRequestId(id).get(0);

        String name = downloadRequest.getMusicFileName() != null && !downloadRequest.getMusicFileName().isEmpty()
                      ? downloadRequest.getMusicFileName()
                      : "Unnamed.mp3";

        String fileName = URLEncoder.encode(name, "UTF-8");
        //fileName = URLDecoder.decode(fileName, "WINDOWS-1251");
        headers.put("Content-Disposition", String.format("attachment; filename=%s", fileName));

        writeDataToResponse(
            httpServletResponse,
            "application/octet-stream;",
            headers,
            downloadRequest.getMusicFile()
        );
    }

    @GetMapping("/downloadMultipleMusicFile")
    public void saveDownloadedMusicFile(final @RequestParam(required = true) List<String> ids,
                                        final HttpServletResponse httpServletResponse) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        List<DownloadRequest> downloadRequests = downloadRequestRepository.getAllByRequestIds(ids);

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.YY-HHmm");
        //fileName = URLDecoder.decode(fileName, "WINDOWS-1251");
        headers.put("Content-Disposition", String.format("attachment; filename=music-%s.zip", fmt.print(now)));

        httpServletResponse.setContentType("application/zip");

        for (String header : headers.keySet()) {
            httpServletResponse.setHeader(header, headers.get(header));
        }

        try (ZipOutputStream zippedOUt = new ZipOutputStream(httpServletResponse.getOutputStream())) {
            int counter = 0;
            byte[] buffer = new byte[1024];
            for(DownloadRequest downloadRequest : downloadRequests){
                ZipEntry e = new ZipEntry(transliterate(downloadRequest.getMusicFileName()));
                // Configure the zip entry, the properties of the file
                e.setSize(downloadRequest.getMusicFile().length);
                e.setTime(System.currentTimeMillis());
                // etc.
                zippedOUt.putNextEntry(e);
                // And the content of the resource:
                ByteArrayInputStream bais = new ByteArrayInputStream(downloadRequest.getMusicFile());
                //StreamUtils.copy(bais, zippedOUt);
                int length;

                while ((length = bais.read(buffer)) > 0) {
                    zippedOUt.write(buffer, 0, length);
                }
                zippedOUt.closeEntry();

                counter++;
                if(counter == 10){
                    zippedOUt.flush();
                    counter = 0;
                }
            }

            zippedOUt.finish();
        } catch (Exception e) {
            // Do something with Exception
        }
    }

    @GetMapping("/createExcelFileForRequests")
    public void createExcelFileForRequests(final HttpServletResponse httpServletResponse) throws Exception {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        Report report = clientService.createExcelReportFile();

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.YY-HHmm");

        String fileName = URLEncoder.encode("Отчет по зарегистрированным пользователям", "UTF-8");
        headers.put("Content-Disposition", String.format("attachment; filename=%s-%s.xls", fileName, fmt.print(now)));

        writeDataToResponse(
            httpServletResponse,
            "application/octet-stream;",
            headers,
            report.getContent()
        );
    }

    @GetMapping("/createUsersExcelReport")
    public void createUsersExcelReport(final HttpServletResponse httpServletResponse) throws Exception {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        Report report = clientService.createUsersExcelReport();

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.YY-HHmm");

        String fileName = URLEncoder.encode("Отчет по зарегистрированным заявкам", "UTF-8");
        headers.put("Content-Disposition", String.format("attachment; filename=%s-%s.xls", fileName, fmt.print(now)));

        writeDataToResponse(
            httpServletResponse,
            "application/octet-stream;",
            headers,
            report.getContent()
        );
    }

    @GetMapping("/downloadRequestsToJson")
    public void downloadRequestsToJson(final HttpServletResponse httpServletResponse) throws Exception {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        byte[] data = clientService.createRequestsToJson();

        String fileName = URLEncoder.encode("Отчет по зарегистрированным заявкам", "UTF-8");
        headers.put("Content-Disposition", "attachment; filename=data.json");

        writeDataToResponse(
            httpServletResponse,
            "application/octet-stream;",
            headers,
            data
        );
    }



    private void writeDataToResponse(final HttpServletResponse response, final String contentType, final Map<String, String> headers, final byte[] data) throws IOException {
        response.setContentType(contentType);
        response.setContentLength(data.length);


        for (String header : headers.keySet()) {
            response.setHeader(header, headers.get(header));
        }

        ServletOutputStream outputStream = response.getOutputStream();

        try {
            outputStream.write(data);
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    private String transliterate(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ',', '.', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", ",", ".", "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }


    @GetMapping("/getCommands")
    public List<CommandDTO> getCommands() {
        return clientService.getCommands();
    }
}
