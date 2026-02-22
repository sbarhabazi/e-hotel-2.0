# E-Hotel — Système de gestion hôtelière

Application web de gestion hôtelière multi-établissements développée avec Spring Boot 3.

## Technologies utilisées

| Technologie | Version | Rôle |
|---|---|---|
| Java | 17 | Langage de programmation |
| Spring Boot | 3.2.4 | Framework principal (MVC, IoC) |
| Spring Data JPA / Hibernate | — | ORM et accès à la base de données |
| Thymeleaf | — | Moteur de templates HTML côté serveur |
| PostgreSQL | — | Base de données relationnelle |
| Lombok | 1.18.38 | Génération automatique de code (getters/setters) |
| Bootstrap | 5.3.3 | Framework CSS (interface utilisateur) |
| Maven | 3.x | Outil de build et gestion des dépendances |

## Prérequis

- Java 17+
- Maven 3.6+
- PostgreSQL installé et démarré en local

## Configuration de la base de données

Créer une base de données PostgreSQL :

```sql
CREATE DATABASE db_hotel;
```

Les identifiants de connexion par défaut (fichier `EHotel/src/main/resources/application.properties`) :

```
Hôte     : localhost:5432
Base     : db_hotel
Utilisateur : postgres
Mot de passe : password
```

Modifier ces valeurs si nécessaire avant de lancer l'application.

Le schéma SQL complet est disponible dans le fichier `schema.sql` à la racine du projet.

## Lancement de l'application

```bash
cd EHotel
chmod +x mvnw
./mvnw spring-boot:run
```

L'application est accessible à l'adresse : **http://localhost:8080**

### Lancement rapide (1 commande)

Depuis la racine du projet, une seule commande demarre PostgreSQL (Docker), importe `schema.sql` si necessaire, puis lance l'application:

```bash
chmod +x start-ehotel.sh
./start-ehotel.sh
```

Commandes par systeme:

- macOS / Linux: `./start-ehotel.sh`
- Windows (CMD / PowerShell): `start-ehotel.bat`
- Windows (Git Bash / WSL): `./start-ehotel.sh`

Variables optionnelles (port, credentials, image Docker):

- `EHOTEL_PG_PORT`
- `EHOTEL_DB_NAME`
- `EHOTEL_DB_USER`
- `EHOTEL_DB_PASSWORD`
- `EHOTEL_PG_IMAGE`

## Structure du projet

```
e-hotel/
├── schema.sql                          # Schéma SQL de création des tables
└── EHotel/
    ├── pom.xml                         # Configuration Maven et dépendances
    └── src/main/
        ├── java/com/example/EHotel/
        │   ├── EHotelApplication.java  # Point d'entrée Spring Boot
        │   ├── controllers/            # Contrôleurs Spring MVC (routes HTTP)
        │   ├── services/               # Logique métier (@Transactional)
        │   ├── repositories/           # Accès BDD via Spring Data JPA
        │   ├── model/                  # Entités JPA (tables de la BDD)
        │   └── dtos/                   # Objets de transfert (formulaires)
        └── resources/
            ├── application.properties  # Configuration Spring Boot
            └── templates/              # Vues HTML Thymeleaf
```

## Fonctionnalités

### Portail Client
- Recherche de chambres disponibles par critères (dates, capacité, prix, chaîne, étoiles)
- Réservation d'une chambre (création automatique du client si inconnu)

### Portail Employé
- Gestion des clients (ajout, modification, suppression)
- Consultation des réservations et locations

### Portail Manager
- Gestion complète des hôtels (CRUD)
- Gestion des chambres (CRUD)
- Gestion des employés (CRUD)
- Enregistrement des paiements
- Transformation d'une réservation en location (check-in)

## Modèle de données principal

```
HotelChain → Hotel → Room → Booking → Rental → Payment
                              ↓                    ↓
                        BookingArchive       RentalArchive
                     Customer ←────────────────────┘
                     Employee (gérant de l'hôtel)
```

## Comptes et accès

L'application ne gère pas d'authentification. Les trois portails sont accessibles directement via les URLs :

| Portail | URL |
|---|---|
| Accueil | `/` |
| Client | `/room/search` |
| Employé | `/employee` |
| Manager | `/manager` |
