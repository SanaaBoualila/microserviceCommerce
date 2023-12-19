package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exception.ProduitIntrouvableException;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao prodDao){
        this.productDao = prodDao;
    }

    @GetMapping("/Produits")
    public List<Product> listeProduits() {
        return productDao.findAll();
    }

    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);
        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
        return produit;
    }

    @GetMapping(value = "/test/Produits/{prixLimit}")
    public List<Product> testRequete(@PathVariable int prixLimit){
        return productDao.findByPrixGreaterThan(prixLimit);
    }


    @PostMapping(value = "/Produits")
    public ResponseEntity<Product> ajouterProduit(@Valid @RequestBody Product product) {
        Product productAdded = productDao.save(product);
        if (Objects.isNull(productAdded)) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @PutMapping(value="/Produits")
    public Product updateProduit(@RequestBody Product product){
        return productDao.save(product);
    }


    @DeleteMapping(value = "/Produits/{id}")
    public void deleteProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }

    @GetMapping(value="/AdminProduits")
    public String calculerMargeProduit() {
        List<Product> products = productDao.findAll();
        JSONObject resultatJson = new JSONObject();

        for (Product prod : products) {
            int result = prod.getPrix() - prod.getPrixAchat();
            String key = "Product{id=" + prod.getId() + ", nom='" + prod.getNom() + "', prix=" + prod.getPrix() + "}";
            resultatJson.put(key, result);
        }

        return resultatJson.toJSONString();
    }






}
