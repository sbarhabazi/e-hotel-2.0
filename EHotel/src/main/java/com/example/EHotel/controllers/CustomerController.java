package com.example.EHotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur Spring MVC gérant la page d'accueil du portail client.
 *
 * Préfixe de route : /customer
 *
 * Routes disponibles :
 *   GET /customer/ → Afficher la page d'accueil du portail client
 *
 * Ce contrôleur est minimaliste : il affiche simplement la page d'accueil
 * du portail client qui propose les options disponibles (recherche de chambres, etc.).
 * La gestion complète des clients (CRUD) est réalisée dans EmployeeController.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    /**
     * Affiche la page d'accueil du portail client.
     *
     * GET /customer/ → vue "customer.html"
     *
     * @return Le nom du template Thymeleaf à afficher ("customer")
     */
    @GetMapping("")
    public String showCustomerHome() {
        // Retourne simplement le nom du template de la page d'accueil client
        return "customer";  // Correspond à src/main/resources/templates/customer.html
    }


}
