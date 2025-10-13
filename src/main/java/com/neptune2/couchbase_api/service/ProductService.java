package com.neptune2.couchbase_api.service;

import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.Cluster;
import com.neptune2.couchbase_api.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import com.couchbase.client.java.json.JsonObject;

@Service
public class ProductService {

    private final CouchbaseScopeService scopeService;
    private final Cluster cluster;

    public ProductService(CouchbaseScopeService scopeService, Cluster cluster) {
        this.scopeService = scopeService;
        this.cluster = cluster;
    }

    public List<Product> getRepositoryProducts() {
        List<Product> products = new ArrayList<>();

        try {
            // Récupération des éléments nécessaires
            // Collection collection = scopeService.getRepositoryCollection();

            String bucket = "ClickAndCollect";
            String scope = "repository";
            String coll = "product";

            // ✅ Requête N1QL : sélectionne tous les documents de la collection
            String query = String.format(
                    "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.vatType,p.type,c.id AS categoryId,c.name.fr AS categoryName_fr  FROM `%s`.`%s`.`%s` AS p UNNEST p.categories AS c ORDER BY p.id",
                    bucket, scope, coll);

            System.out.println(query);

            QueryResult result = cluster.query(query);

            // ✅ Parcours des résultats et conversion en objets Product
            result.rowsAsObject().forEach(row -> {
                Product p = new Product();
                p.setId(row.getInt("id")); // si ton document contient un champ "id"
                p.setName(row.getString("name"));
                p.setpriceIncludingTax(row.getDouble("priceIncludingTax"));
                p.setVatType(row.getString("vatType"));
                p.setType(row.getString("type"));
                p.setCategoryId(row.getString("categoryId"));
                p.setCategoryName_fr(row.getString("categoryName_fr"));

                products.add(p);
            });

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des produits : " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    public Product getProductById(Integer productId) {
        String bucket = "ClickAndCollect";
        String scope = "repository";
        String coll = "product";

        // ✅ Requête N1QL : sélectionne tous les documents de la collection
        String query = String.format(
                "SELECT p.id, p.name.fr AS name, p.priceIncludingTax,p.vatType,p.type,c.id AS categoryId,c.name.fr AS categoryName_fr  FROM `%s`.`%s`.`%s` AS p UNNEST p.categories AS c WHERE p.id = "
                        + productId + " ORDER BY p.id",
                bucket, scope, coll);
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
        p.setVatType(json.getString("vatType"));
        p.setType(json.getString("type"));
        p.setCategoryId(json.getString("categoryId"));
        p.setCategoryName_fr(json.getString("categoryName_fr"));

        return p;
    }
}
