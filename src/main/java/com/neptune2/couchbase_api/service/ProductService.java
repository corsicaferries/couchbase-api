package com.neptune2.couchbase_api.service;

import com.couchbase.client.java.query.QueryResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.couchbase.client.java.Cluster;
import com.neptune2.couchbase_api.model.Allergen;
import com.neptune2.couchbase_api.model.Familles;
import com.neptune2.couchbase_api.model.Product;
import com.neptune2.couchbase_api.service.ExcelExportService;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.couchbase.client.java.json.JsonObject;

@SuppressWarnings("unused")
@Service
public class ProductService {

    private final CouchbaseScopeService scopeService;
    private final Cluster cluster;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${image.url}")
    private String imageUrl;

    public ProductService(CouchbaseScopeService scopeService, Cluster cluster) {
        this.scopeService = scopeService;
        this.cluster = cluster;
    }

    public List<Product> getRepositoryProducts() {
        List<Product> products = new ArrayList<>();

        try {
            // Récupération des éléments nécessaires
            // Collection collection = scopeService.getRepositoryCollection();

            String bucket = "Neptune-dev";
            String scope = "repository";
            String coll = "product";

            // ✅ Requête N1QL : sélectionne tous les documents de la collection
            /*
             * String query = String.format(
             * "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.vatType,p.type,c.id AS categoryId,c.name.fr AS categoryName_fr,c.description.fr as category_description_fr, p.description.fr as description_fr, p.allergens AS allergens FROM `%s`.`%s`.`%s` AS p UNNEST p.categories AS c ORDER BY p.id"
             * ,
             * bucket, scope, coll);
             */
            String query = String.format(
                    "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.type, p.description.fr as description_fr,p.vatType,p.typ_prod,p.familles.num_fami,p.familles.lib_fami,p.familles.num_sfam,p.familles.lib_sfam,p.familles.num_ssfa,p.familles.lib_ssfa,p.cod_prog FROM `%s`.`%s`.`%s` AS p ORDER BY p.id",
                    bucket, scope, coll);

            System.out.println(query);

            QueryResult result = cluster.query(query);

            // ✅ Parcours des résultats et conversion en objets Product
            result.rowsAsObject().forEach(row -> {
                Product p = new Product();
                p.setId(row.getInt("id")); // si ton document contient un champ "id"
                p.setName(row.getString("name"));
                p.setpriceIncludingTax(row.getDouble("priceIncludingTax"));
                p.setVatType(String.valueOf(row.get("vatType")));
                // p.setVatType(row.getString("vatType"));
                p.setType_product(row.getString("type"));
                p.setCategoryId(row.getString("categoryId"));
                p.setCategoryName_fr(row.getString("categoryName_fr"));
                p.setCategorydescription_fr(row.getString("categorydescription_fr"));
                p.setDescription_fr(row.getString("description_fr"));
                // p.setAllergens(row.getString("allergens"));

                com.couchbase.client.java.json.JsonArray allergensArray = row.getArray("allergens");
                if (allergensArray != null) {
                    // Convertir JsonArray en chaîne JSON
                    String allergensJsonString = allergensArray.toString();
                    // System.out.println("allergenJsonString=" + allergensJsonString);
                    try {
                        TypeFactory tf = objectMapper.getTypeFactory();
                        List<Allergen> allergensList = objectMapper.readValue(
                                allergensJsonString,
                                tf.constructCollectionType(List.class, Allergen.class));
                        // System.out.println("DEBUG allergensList parsed = " + allergensList);
                        p.setAllergens(allergensList);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur de parsing du champ allergens JSON", e);
                    }
                } else {
                    // Pas d’allergènes : tu peux laisser la liste vide ou null selon ton design
                    p.setAllergens(List.of());
                }
                p.settypeDeProduit(row.getString("typ_prod"));
                Familles familles = new Familles();
                familles.setcodeFamille(row.getInt("num_fami"));
                familles.setlibelleFamille(row.getString("lib_fami").trim());
                familles.setcodeSousFamille(row.getInt("num_sfam"));
                familles.setlibelleSousFamille(row.getString("lib_sfam").trim());
                familles.setcodeSousSousFamille(row.getInt("num_ssfa"));
                familles.setlibelleSousSousFamille(row.getString("lib_ssfa").trim());
                p.setFamilles(familles);
                p.setGencod(row.getString("cod_prog"));

                p.setImageUrl(imageUrl + p.getId() + ".png");
                products.add(p);
            });

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des produits : " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    public Product getProductById(Integer productId) {
        String bucket = "Neptune-dev";
        String scope = "repository";
        String coll = "product";

        // ✅ Requête N1QL : sélectionne tous les documents de la collection
        /*
         * SELECT
         * vatType,
         * name.fr,
         * name.en,
         * name.it,
         * _class,
         * id,
         * typ_prod,
         * familles.num_fami,
         * familles.num_sfam,
         * familles.num_ssfa,
         * cod_prog,
         * priceIncludingTax
         */
        String query = String.format(
                "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.type, p.description.fr as description_fr,p.vatType,p.typ_prod,p.familles.num_fami,p.familles.lib_fami,p.familles.num_sfam,p.familles.lib_sfam,p.familles.num_ssfa,p.familles.lib_ssfa,p.cod_prog FROM `%s`.`%s`.`%s` AS p WHERE p.id="
                        + productId + " ORDER BY p.id",
                bucket, scope, coll);
        /*
         * String query = String.format(
         * "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.vatType,p.type,c.id AS categoryId,c.name.fr AS categoryName_fr,c.description.fr as category_description_fr, p.description.fr as description_fr, p.allergens AS allergens FROM `%s`.`%s`.`%s` AS p UNNEST p.categories AS c WHERE p.id = "
         * + productId + " ORDER BY p.id",
         * bucket, scope, coll);
         */
        System.out.println(query);

        QueryResult result = cluster.query(query);
        List<JsonObject> rows = result.rowsAsObject();

        // ✅ Parcours des résultats et conversion en objets Product

        if (rows == null || rows.isEmpty()) {
            throw new RuntimeException("Aucun produit trouvé avec l'id " + productId);
        }
        JsonObject json = rows.get(0);
        Product p = new Product();
        p.setId(json.getInt("id")); // si ton document contient un champ "id"
        p.setName(json.getString("name"));
        p.setpriceIncludingTax(json.getDouble("priceIncludingTax"));
        p.setVatType(String.valueOf(json.get("vatType")));
        // p.setVatType(json.getString("vatType"));
        p.setType_product(json.getString("type"));
        p.setCategoryId(json.getString("categoryId"));
        p.setCategoryName_fr(json.getString("categoryName_fr"));
        p.setCategorydescription_fr(json.getString("categorydescription_fr"));
        p.setDescription_fr(json.getString("description_fr"));

        com.couchbase.client.java.json.JsonArray allergensArray = json.getArray("allergens");
        if (allergensArray != null) {
            // Convertir JsonArray en chaîne JSON
            String allergensJsonString = allergensArray.toString();
            // System.out.println("allergenJsonString=" + allergensJsonString);
            try {
                TypeFactory tf = objectMapper.getTypeFactory();
                List<Allergen> allergensList = objectMapper.readValue(
                        allergensJsonString,
                        tf.constructCollectionType(List.class, Allergen.class));
                // System.out.println("DEBUG allergensList parsed = " + allergensList);
                p.setAllergens(allergensList);
            } catch (IOException e) {
                throw new RuntimeException("Erreur de parsing du champ allergens JSON", e);
            }
        } else {
            // Pas d’allergènes : tu peux laisser la liste vide ou null selon ton design
            p.setAllergens(List.of());
        }

        p.settypeDeProduit(json.getString("typ_prod"));
        Familles familles = new Familles();
        familles.setcodeFamille(json.getInt("num_fami"));
        familles.setlibelleFamille(json.getString("lib_fami").trim());
        familles.setcodeSousFamille(json.getInt("num_sfam"));
        familles.setlibelleSousFamille(json.getString("lib_sfam").trim());
        familles.setcodeSousSousFamille(json.getInt("num_ssfa"));
        familles.setlibelleSousSousFamille(json.getString("lib_ssfa").trim());
        p.setFamilles(familles);
        p.setGencod(json.getString("cod_prog"));

        p.setImageUrl(imageUrl + p.getId() + ".png");

        return p;
    }

    public List<Product> getProductsByssfa(Integer num_ssfa) {
        List<Product> products = new ArrayList<>();

        try {
            // Récupération des éléments nécessaires
            // Collection collection = scopeService.getRepositoryCollection();

            String bucket = "Neptune-dev";
            String scope = "repository";
            String coll = "product";

            // ✅ Requête N1QL : sélectionne tous les documents de la collection
            /*
             * String query = String.format(
             * "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.vatType,p.type,c.id AS categoryId,c.name.fr AS categoryName_fr,c.description.fr as category_description_fr, p.description.fr as description_fr, p.allergens AS allergens FROM `%s`.`%s`.`%s` AS p UNNEST p.categories AS c WHERE p.num_ssfa="
             * + num_ssfa + " ORDER BY p.id",
             * bucket, scope, coll);
             */
            String query = String.format(
                    "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.type, p.description.fr as description_fr,p.vatType,p.typ_prod,p.familles.num_fami,p.familles.lib_fami,p.familles.num_sfam,p.familles.lib_sfam,p.familles.num_ssfa,p.familles.lib_ssfa,p.cod_prog FROM `%s`.`%s`.`%s` AS p WHERE p.familles.num_ssfa="
                            + num_ssfa + " ORDER BY p.id",
                    bucket, scope, coll);

            System.out.println(query);
            QueryResult result = cluster.query(query);

            if (result.rowsAsObject().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aucun produit trouvé pour num_ssfa = " + num_ssfa);
            }

            // ✅ Parcours des résultats et conversion en objets Product
            result.rowsAsObject().forEach(row -> {
                Product p = new Product();
                p.setId(row.getInt("id")); // si ton document contient un champ "id"
                p.setName(row.getString("name"));
                p.setpriceIncludingTax(row.getDouble("priceIncludingTax"));
                p.setVatType(String.valueOf(row.get("vatType")));
                // p.setVatType(row.getString("vatType"));
                p.setType_product(row.getString("type"));
                p.setCategoryId(row.getString("categoryId"));
                p.setCategoryName_fr(row.getString("categoryName_fr"));
                p.setCategorydescription_fr(row.getString("categorydescription_fr"));
                p.setDescription_fr(row.getString("description_fr"));
                // p.setAllergens(row.getString("allergens"));

                p.settypeDeProduit(row.getString("typ_prod"));
                Familles familles = new Familles();
                familles.setcodeFamille(row.getInt("num_fami"));
                familles.setlibelleFamille(row.getString("lib_fami").trim());
                familles.setcodeSousFamille(row.getInt("num_sfam"));
                familles.setlibelleSousFamille(row.getString("lib_sfam").trim());
                familles.setcodeSousSousFamille(row.getInt("num_ssfa"));
                familles.setlibelleSousSousFamille(row.getString("lib_ssfa").trim());
                p.setFamilles(familles);
                p.setGencod(row.getString("cod_prog"));
                p.setImageUrl(imageUrl + p.getId() + ".png");

                com.couchbase.client.java.json.JsonArray allergensArray = row.getArray("allergens");
                if (allergensArray != null) {
                    // Convertir JsonArray en chaîne JSON
                    String allergensJsonString = allergensArray.toString();
                    // System.out.println("allergenJsonString=" + allergensJsonString);
                    try {
                        TypeFactory tf = objectMapper.getTypeFactory();
                        List<Allergen> allergensList = objectMapper.readValue(
                                allergensJsonString,
                                tf.constructCollectionType(List.class, Allergen.class));
                        // System.out.println("DEBUG allergensList parsed = " + allergensList);
                        p.setAllergens(allergensList);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur de parsing du champ allergens JSON", e);
                    }
                } else {
                    // Pas d’allergènes : tu peux laisser la liste vide ou null selon ton design
                    p.setAllergens(List.of());
                }
                p.setImageUrl(imageUrl + p.getId() + ".png");
                products.add(p);
            });

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des produits : " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByType(String typeProduct) {
        List<Product> products = new ArrayList<>();

        try {
            // Récupération des éléments nécessaires
            // Collection collection = scopeService.getRepositoryCollection();

            String bucket = "Neptune-dev";
            String scope = "repository";
            String coll = "product";

            // ✅ Requête N1QL : sélectionne tous les documents de la collection
            /*
             * String query = String.format(
             * "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.vatType,p.type,c.id AS categoryId,c.name.fr AS categoryName_fr,c.description.fr as category_description_fr, p.description.fr as description_fr, p.allergens AS allergens FROM `%s`.`%s`.`%s` AS p UNNEST p.categories AS c WHERE p.num_ssfa="
             * + num_ssfa + " ORDER BY p.id",
             * bucket, scope, coll);
             */
            String query = String.format(
                    "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.type, p.description.fr as description_fr,p.vatType,p.typ_prod,p.familles.num_fami,p.familles.lib_fami,p.familles.num_sfam,p.familles.lib_sfam,p.familles.num_ssfa,p.familles.lib_ssfa,p.cod_prog FROM `%s`.`%s`.`%s` AS p WHERE p.typ_prod=\""
                            + typeProduct + "\" ORDER BY p.id",
                    bucket, scope, coll);

            System.out.println(query);
            QueryResult result = cluster.query(query);

            if (result.rowsAsObject().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aucun produit trouvé pour le type produit = " + typeProduct);
            }

            // ✅ Parcours des résultats et conversion en objets Product
            result.rowsAsObject().forEach(row -> {
                Product p = new Product();
                p.setId(row.getInt("id")); // si ton document contient un champ "id"
                p.setName(row.getString("name"));
                p.setpriceIncludingTax(row.getDouble("priceIncludingTax"));
                p.setVatType(String.valueOf(row.get("vatType")));
                // p.setVatType(row.getString("vatType"));
                p.setType_product(row.getString("type"));
                p.setCategoryId(row.getString("categoryId"));
                p.setCategoryName_fr(row.getString("categoryName_fr"));
                p.setCategorydescription_fr(row.getString("categorydescription_fr"));
                p.setDescription_fr(row.getString("description_fr"));
                // p.setAllergens(row.getString("allergens"));

                p.settypeDeProduit(row.getString("typ_prod"));
                Familles familles = new Familles();
                familles.setcodeFamille(row.getInt("num_fami"));
                familles.setlibelleFamille(row.getString("lib_fami").trim());
                familles.setcodeSousFamille(row.getInt("num_sfam"));
                familles.setlibelleSousFamille(row.getString("lib_sfam").trim());
                familles.setcodeSousSousFamille(row.getInt("num_ssfa"));
                familles.setlibelleSousSousFamille(row.getString("lib_ssfa").trim());
                p.setFamilles(familles);
                p.setGencod(row.getString("cod_prog"));
                p.setImageUrl(imageUrl + p.getId() + ".png");

                com.couchbase.client.java.json.JsonArray allergensArray = row.getArray("allergens");
                if (allergensArray != null) {
                    // Convertir JsonArray en chaîne JSON
                    String allergensJsonString = allergensArray.toString();
                    // System.out.println("allergenJsonString=" + allergensJsonString);
                    try {
                        TypeFactory tf = objectMapper.getTypeFactory();
                        List<Allergen> allergensList = objectMapper.readValue(
                                allergensJsonString,
                                tf.constructCollectionType(List.class, Allergen.class));
                        // System.out.println("DEBUG allergensList parsed = " + allergensList);
                        p.setAllergens(allergensList);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur de parsing du champ allergens JSON", e);
                    }
                } else {
                    // Pas d’allergènes : tu peux laisser la liste vide ou null selon ton design
                    p.setAllergens(List.of());
                }
                p.setImageUrl(imageUrl + p.getId() + ".png");
                products.add(p);
            });

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des produits : " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsYellows() {
        List<Product> products = new ArrayList<>();

        try {
            // Récupération des éléments nécessaires
            // Collection collection = scopeService.getRepositoryCollection();

            String bucket = "Neptune-dev";
            String scope = "repository";
            String coll = "product";

            // ✅ Requête N1QL : sélectionne tous les documents de la collection
            String query = String.format(
                    "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.type, p.description.fr as description_fr,p.vatType,p.typ_prod,p.familles.num_fami,p.familles.lib_fami,p.familles.num_sfam,p.familles.lib_sfam,p.familles.num_ssfa,p.familles.lib_ssfa,p.cod_prog FROM `%s`.`%s`.`%s` AS p WHERE p.familles.num_ssfa in [102701,102704,102707,102709,102711,102713,102715,102718] ORDER BY p.id",
                    bucket, scope, coll);

            System.out.println(query);
            QueryResult result = cluster.query(query);

            if (result.rowsAsObject().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aucun produit trouvé pour Yellows");
            }

            // ✅ Parcours des résultats et conversion en objets Product
            result.rowsAsObject().forEach(row -> {
                Product p = new Product();
                p.setId(row.getInt("id")); // si ton document contient un champ "id"
                p.setName(row.getString("name"));
                p.setpriceIncludingTax(row.getDouble("priceIncludingTax"));
                p.setVatType(String.valueOf(row.get("vatType")));
                // p.setVatType(row.getString("vatType"));
                p.setType_product(row.getString("type"));
                p.setCategoryId(row.getString("categoryId"));
                p.setCategoryName_fr(row.getString("categoryName_fr"));
                p.setCategorydescription_fr(row.getString("categorydescription_fr"));
                p.setDescription_fr(row.getString("description_fr"));
                // p.setAllergens(row.getString("allergens"));

                p.settypeDeProduit(row.getString("typ_prod"));
                Familles familles = new Familles();
                familles.setcodeFamille(row.getInt("num_fami"));
                familles.setlibelleFamille(row.getString("lib_fami").trim());
                familles.setcodeSousFamille(row.getInt("num_sfam"));
                familles.setlibelleSousFamille(row.getString("lib_sfam").trim());
                familles.setcodeSousSousFamille(row.getInt("num_ssfa"));
                familles.setlibelleSousSousFamille(row.getString("lib_ssfa").trim());
                p.setFamilles(familles);
                p.setGencod(row.getString("cod_prog"));
                p.setImageUrl(imageUrl + p.getId() + ".png");

                com.couchbase.client.java.json.JsonArray allergensArray = row.getArray("allergens");
                if (allergensArray != null) {
                    // Convertir JsonArray en chaîne JSON
                    String allergensJsonString = allergensArray.toString();
                    // System.out.println("allergenJsonString=" + allergensJsonString);
                    try {
                        TypeFactory tf = objectMapper.getTypeFactory();
                        List<Allergen> allergensList = objectMapper.readValue(
                                allergensJsonString,
                                tf.constructCollectionType(List.class, Allergen.class));
                        // System.out.println("DEBUG allergensList parsed = " + allergensList);
                        p.setAllergens(allergensList);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur de parsing du champ allergens JSON", e);
                    }
                } else {
                    // Pas d’allergènes : tu peux laisser la liste vide ou null selon ton design
                    p.setAllergens(List.of());
                }
                p.setImageUrl(imageUrl + p.getId() + ".png");
                products.add(p);
            });

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des produits : " + e.getMessage());
            e.printStackTrace();
        }

        // 🔽 À la fin de ton try, juste avant "return products;"
        try {
            // Sérialisation JSON de la liste products
            String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products);

            // Écriture dans un fichier externe
            Path outputPath = Paths.get("C:/temp/products_yellows.json");

            Files.writeString(outputPath, jsonOutput);

            System.out.println("✅ Fichier JSON généré : " + outputPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l’écriture du fichier JSON : " + e.getMessage());
        }

        return products;
    }

    public List<Product> getProductsDV() {
        List<Product> products = new ArrayList<>();

        try {
            // Récupération des éléments nécessaires
            // Collection collection = scopeService.getRepositoryCollection();

            String bucket = "Neptune-dev";
            String scope = "repository";
            String coll = "product";

            // ✅ Requête N1QL : sélectionne tous les documents de la collection
            String query = String.format(
                    "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.type, p.description.fr as description_fr,p.vatType,p.typ_prod,p.familles.num_fami,p.familles.lib_fami,p.familles.num_sfam,p.familles.lib_sfam,p.familles.num_ssfa,p.familles.lib_ssfa,p.cod_prog FROM `%s`.`%s`.`%s` AS p WHERE p.familles.num_ssfa in [102702,102705,102708,102710,102712,102714,102716,102719] ORDER BY p.id",
                    bucket, scope, coll);

            System.out.println(query);
            QueryResult result = cluster.query(query);

            if (result.rowsAsObject().isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aucun produit trouvé pour DolceVita");
            }

            // ✅ Parcours des résultats et conversion en objets Product
            result.rowsAsObject().forEach(row -> {
                Product p = new Product();
                p.setId(row.getInt("id")); // si ton document contient un champ "id"
                p.setName(row.getString("name"));
                p.setpriceIncludingTax(row.getDouble("priceIncludingTax"));
                p.setVatType(String.valueOf(row.get("vatType")));
                // p.setVatType(row.getString("vatType"));
                p.setType_product(row.getString("type"));
                p.setCategoryId(row.getString("categoryId"));
                p.setCategoryName_fr(row.getString("categoryName_fr"));
                p.setCategorydescription_fr(row.getString("categorydescription_fr"));
                p.setDescription_fr(row.getString("description_fr"));
                // p.setAllergens(row.getString("allergens"));

                p.settypeDeProduit(row.getString("typ_prod"));
                Familles familles = new Familles();
                familles.setcodeFamille(row.getInt("num_fami"));
                familles.setlibelleFamille(row.getString("lib_fami").trim());
                familles.setcodeSousFamille(row.getInt("num_sfam"));
                familles.setlibelleSousFamille(row.getString("lib_sfam").trim());
                familles.setcodeSousSousFamille(row.getInt("num_ssfa"));
                familles.setlibelleSousSousFamille(row.getString("lib_ssfa").trim());
                p.setFamilles(familles);
                p.setGencod(row.getString("cod_prog"));
                p.setImageUrl(imageUrl + p.getId() + ".png");

                com.couchbase.client.java.json.JsonArray allergensArray = row.getArray("allergens");
                if (allergensArray != null) {
                    // Convertir JsonArray en chaîne JSON
                    String allergensJsonString = allergensArray.toString();
                    // System.out.println("allergenJsonString=" + allergensJsonString);
                    try {
                        TypeFactory tf = objectMapper.getTypeFactory();
                        List<Allergen> allergensList = objectMapper.readValue(
                                allergensJsonString,
                                tf.constructCollectionType(List.class, Allergen.class));
                        // System.out.println("DEBUG allergensList parsed = " + allergensList);
                        p.setAllergens(allergensList);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur de parsing du champ allergens JSON", e);
                    }
                } else {
                    // Pas d’allergènes : tu peux laisser la liste vide ou null selon ton design
                    p.setAllergens(List.of());
                }
                p.setImageUrl(imageUrl + p.getId() + ".png");
                products.add(p);
            });

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des produits : " + e.getMessage());
            e.printStackTrace();
        }

        // --- Export JSON et Excel ---
        try {
            // ✅ 1. Export JSON
            String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(products);
            Path outputPathJson = Paths.get("C:/temp/products_dolceVita.json");
            Files.writeString(outputPathJson, jsonOutput);
            System.out.println("✅ Fichier JSON généré : " + outputPathJson.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la génération du JSON/Excel : " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
}
