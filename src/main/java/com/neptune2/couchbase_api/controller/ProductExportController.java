package com.neptune2.couchbase_api.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.neptune2.couchbase_api.model.Product;
import com.neptune2.couchbase_api.service.ExcelExportService;
import com.neptune2.couchbase_api.service.ProductService;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class ProductExportController {

    private final ProductService productService;
    private final ExcelExportService excelExportService;

    public ProductExportController(ProductService productService, ExcelExportService excelExportService) {
        this.productService = productService;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/products/DV")
    public ResponseEntity<byte[]> exportProductsDV() {
        try {
            List<Product> products = productService.getProductsDV();

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            ByteArrayInputStream in = excelExportService.productsToExcel(products);
            byte[] excelBytes = in.readAllBytes();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products_dolceVita.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur : " + e.getMessage()).getBytes());
        }
    }
}
