package com.neptune2.couchbase_api.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.neptune2.couchbase_api.model.Product;

@Service
public class ExcelExportService {

    public ByteArrayInputStream productsToExcel(List<Product> products) throws Exception {
        String[] columns = { "ID", "Nom", "Prix TTC", "Type", "Type Produit",
                "Description", "Famille", "Sous-Famille", "Sous-sous-Famille", "Code Produit" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Produits DolceVita");

            // En-têtes
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Lignes de données
            int rowIdx = 1;
            for (Product p : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getpriceIncludingTax());
                row.createCell(3).setCellValue(p.gettypeDeProduit());
                row.createCell(4).setCellValue(p.gettypeDeProduit());
                row.createCell(5).setCellValue(p.getDescription_fr());
                if (p.getFamilles() != null) {
                    row.createCell(6).setCellValue(p.getFamilles().getlibelleFamille());
                    row.createCell(7).setCellValue(p.getFamilles().getlibelleSousFamille());
                    row.createCell(8).setCellValue(p.getFamilles().getlibelleSousSousFamille());
                }
                row.createCell(9).setCellValue(p.getGencod());
            }

            // Ajuste la taille des colonnes
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
