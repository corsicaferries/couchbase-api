package com.neptune2.couchbase_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptune2.couchbase_api.model.Ticket;
import com.neptune2.couchbase_api.model.TicketReponse;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/api/tickets")
public class FormTicketController {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${api.key}")
    private String apiKey;

    @GetMapping("/formTicket")
    public String showForm() {
        return "formTicket";
    }

    /*
     * @PostMapping("/searchTickets")
     * public String submitForm(
     * 
     * @RequestParam(value = "dateVoyage", required = false) String dateVoyage,
     * 
     * @RequestParam(value = "codeLigne", required = false) String codeLigne,
     * 
     * @RequestParam(value = "heureDepart", required = false) String heureDepart,
     * 
     * @RequestParam(value = "caisse", required = false) String caisse,
     * 
     * @RequestParam(value = "startDate", required = false) String startDate,
     * 
     * @RequestParam(value = "endDate", required = false) String endDate,
     * Model model) {
     * 
     * UriComponentsBuilder builder =
     * UriComponentsBuilder.fromHttpUrl("http://localhost:9090/api/tickets");
     * 
     * if (dateVoyage != null && !dateVoyage.isEmpty()) {
     * builder.queryParam("dateVoyage", dateVoyage);
     * }
     * if (codeLigne != null && !codeLigne.isEmpty()) {
     * builder.queryParam("codLigne", codeLigne);
     * }
     * if (heureDepart != null && !heureDepart.isEmpty()) {
     * builder.queryParam("heureDepart", heureDepart);
     * }
     * if (caisse != null && !caisse.isEmpty()) {
     * builder.queryParam("caisse", caisse);
     * }
     * if (startDate != null && !startDate.isEmpty()) {
     * builder.queryParam("startDate", startDate);
     * }
     * if (endDate != null && !endDate.isEmpty()) {
     * builder.queryParam("endDate", endDate);
     * }
     * 
     * String apiUrl = builder.toUriString();
     * System.out.println("Calling API URL: " + apiUrl);
     * 
     * HttpHeaders headers = new HttpHeaders();
     * headers.setContentType(MediaType.APPLICATION_JSON);
     * headers.set("X-API-KEY", apiKey); // <-- ta variable
     * // d’authentification
     * HttpEntity<String> entity = new HttpEntity<>(headers);
     * try {
     * ResponseEntity<String> response = restTemplate.exchange(
     * apiUrl, // URL
     * HttpMethod.GET, // Méthode HTTP
     * entity, // Contient les headers
     * String.class);
     * 
     * // String body = response.getBody();
     * // System.out.println("✅ Réponse JSON : " + body);
     * 
     * model.addAttribute("tickets", response.getBody());
     * }
     * 
     * catch (Exception e) {
     * e.printStackTrace();
     * model.addAttribute("error", "Erreur lors de l'appel à l'API : " +
     * e.getMessage());
     * 
     * }
     * 
     * return "resultTicket";
     * 
     * }
     */
    @PostMapping("/searchTickets")
    public ResponseEntity<byte[]> submitForm(
            @RequestParam(value = "dateVoyage", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateVoyage,

            @RequestParam(value = "codeLigne", required = false) String codeLigne,

            @RequestParam(value = "heureDepart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDepart,

            @RequestParam(value = "caisse", required = false) String caisse,

            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) throws Exception {

        // Formatter pour date et heure si besoin par l’API
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Construire URL avec seulement les paramètres non nuls
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:9090/api/tickets");

        if (dateVoyage != null)
            builder.queryParam("dateVoyage", dateVoyage.format(dateFormatter));
        if (codeLigne != null && !codeLigne.isEmpty())
            builder.queryParam("codLigne", codeLigne);
        if (heureDepart != null)
            builder.queryParam("heureDepart", heureDepart.format(timeFormatter));
        if (caisse != null && !caisse.isEmpty())
            builder.queryParam("caisse", caisse);
        if (startDate != null && !startDate.isEmpty())
            builder.queryParam("startDate", startDate);
        if (endDate != null && !endDate.isEmpty())
            builder.queryParam("endDate", endDate);

        String apiUrl = builder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey); // ton API key

        // Créer l'entité
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Appeler l'API avec exchange (GET)        
ResponseEntity<TicketReponse> response = restTemplate.exchange(
        apiUrl,
        HttpMethod.GET,
        entity,
        TicketReponse.class
);
        List<Ticket>tickets = response.getBody().getTickets();

        // Générer le fichier Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tickets");

        // En-tête
        Row header = sheet.createRow(0);
        String[] columns = { "ID", "Date Voyage", "Code Ligne", "Heure Départ", "Caisse" };
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        // Remplir les données
        int rowNum = 1;
        if (tickets != null) {
            for (Ticket t : tickets) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, t.getId());
                createCell(row, 1, t.getDateVoyage());
                createCell(row, 2, t.getCodeLigne());
                createCell(row, 3, t.getHeureDepart());
                createCell(row, 4, t.getCaisse());
            }
        }

        // Écrire le fichier dans un tableau d’octets
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        // Préparer la réponse HTTP pour téléchargement        
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tickets.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(out.toByteArray());
    }

    // Méthode utilitaire pour gérer différents types de valeurs
    private void createCell(Row row, int colIndex, Object value) {
        Cell cell = row.createCell(colIndex);
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
