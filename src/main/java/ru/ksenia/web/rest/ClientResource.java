package ru.ksenia.web.rest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ksenia.domain.Doctor;
import ru.ksenia.service.ClientService;
import ru.ksenia.web.rest.dto.FullClientData;
import ru.ksenia.web.rest.errors.InternalServerErrorException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClientResource {

    @Autowired
    private ClientService clientService;

    @GetMapping("/doctors")
    public List<Doctor> getDoctors() {
        return clientService.getDoctors();
    }

    @PostMapping("/registerClient")
    public ResponseEntity<Void> registerClient(@RequestBody FullClientData fullClientData) {
        try {
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/downloadReport")
    public void downloadReport(final @RequestParam(required = true) String reportId, final HttpServletResponse httpServletResponse){

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.YY-HHmm");
        headers.put("Content-Disposition", String.format("attachment; filename=report-%s.%s", fmt.print(now), "txt"));

        try {
            writeDataToResponse(
                httpServletResponse,
                "application/octet-stream;",
                headers,
                new byte[0]
            );
        } catch (IOException e) {
            throw new InternalServerErrorException("Не удалось скачать квитанцию");
        }
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


}
