# QA Portfolio - AutomationExercise de Flavien
Projet QA complet réalisé sur le site de démonstration **AutomationExercise**.
L'objectif est de présenter une **démarche QA professionnelle** (ISTQB) : allant de la mise en place des différentes documentations (exigences, cas de tests, rapport de bugs et matrice de traçabilité) aux tests manuels et automatisés (UI & API), avec une forte attention portée à la **méthodologie ISTQB**, à la **traçabilité** et à la **qualité du code**.


## Quickstart

### Créer un compte de test (requis pour les tests nécessitant l'authentification)
1. Aller sur https://www.automationexercise.com/
2. Aller sur la page Signup / Login
3. Créer un compte 
4. Renseigner des adresses fictives pour les tests
5. Utiliser cet email / mot de passe dans les variables d'environnement ci-dessous 

### Variables d'environnement (UI smoke)
Les tests UI qui nécessitent l'authentification utilisent un compte de test : 
- `TEST_USER_EMAIL`
- `TEST_USER_PASSWORD`

### Définir les variables d'environnement (Windows / PowerShell)

**Option A - Session courante (recommandé pour tester rapidement)**
```powershell
$env:TEST_USER_EMAIL="votre_email"
$env:TEST_USER_PASSWORD="votre_mot_de_passe"
```
**Option B - Persistant (Windows)**
```powershell
setx TEST_USER_EMAIL "votre_email"
setx TEST_USER_PASSWORD "votre_mot_de_passe"
```
Après setx fermer et rouvrir le terminal et l'IDE

### Lancer le projet et exécuter ceci
API smoke : mvn -pl api-tests test -Dgroups=smoke
UI smoke  : mvn -pl ui-tests  test -Dgroups=smoke



 ## Objectifs du projet
 - Mettre en pratique une **démarche de test structurée** (ISTQB) :  exigences, cas de tests, priorisation (H/M/B), traçabilité (EX <-> CT <-> exécutions <-> bugs)
 - Concevoir et exécuter des **tests manuels** documentés ainsi que la gestion d'anomalies
 - Mettre en place une **automatisation ciblée et pertinente** : **smoke** puis **régression** (prévue)
 - Code propre et maintenable (POM, waits centralisés, stabilité)

 ## Démarche QA & méthodologie
Le projet suit une approche **ISTQB** :
- Analyse des exigences
- Conception des cas de test (priorisation H / M / B)
- Exécution manuelle avec journal de test
- Gestion et suivi des anomalies
- Traçabilité exigences <-> cas de test <-> exécutions <-> bugs
- Définition d'une stratégie d'automatisation (smoke / régression ciblée)

Les documents de référence sont disponibles dans le dossier `docs/`.


## Structure du projet
- `docs/` : documentation QA (exigences, cas de tests, matrice de traçabilité, rapports de bugs, stratégie)
- `ui-tests/` : tests automatisés UI (Selenium + TestNG + AssertJ)
- `api-tests/` : tests automatisés API (RestAssured + TestNG + AssertJ)
- `.github/workflows/ci.yml` : CI GitHub Actions


 ## Périmètre fonctionnel du projet (docs + manuel)
 - **UI Web**
    - Authentification / compte utilisateur
    - Catalogue produits
    - Panier
    - Checkout / paiement
    - Formulaire de contact

- **API publiques**
    - Produits / marques
    - Recherche
    - Authentification
    - Gestion du compte utilisateur (CRUD)

- **Automatisation** : Voir section Smoke automatisé (v1). La régression est prévue.



## Automatisation des tests
L'automatisation est **volontairement ciblée** :

- **Smoke tests automatisés** (UI & API) 
    - Vérification rapide des parcours critiques
    - Utilisés comme **go/no-go**
- **Régression automatisée ciblée**
    - Prévue après stabilisation du smoke

Les tests automatisés reprennent les **CT-ID** afin de maintenir la traçabilité avec les tests manuels.

Le scénario de confirmation de paiement , bien que métier critique, est volontairement exclu du smoke UI afin de garantir la stabilité des exécutions. Il est prévu dans une régression automatisée ciblée. 


## Smoke automatisé (v1)
### UI (Selenium)
- CT-AUTH-001 / EX-02 - Login OK
- CT-CART-001 / EX-13 - Add to cart 
- CT-CHECKOUT-001 / EX-19 - Checkout : formulaire de paiement visible (paiement complet EX-20 exclu du smoke)

### API (RestAssured)
- CT-API-001 / EX-24 - GET '/productsList' -> 200 
  > Note : AutomationExercise renvoie parfois un `Content-Type` incorrect (`text/html`) malgré un body JSON.


## Exécution des tests
- Pour lancer rapidement le projet voir **Quickstart**
### Prérequis
- Java 21 recommandé 
- Maven
- Google Chrome
- Git

### Exécution locale (Windows / PowerShell)

**API**
```bash
mvn -pl api-tests test -Dgroups=smoke
```
**UI** 
```bash
mvn -pl ui-tests test -Dgroups=smoke
```
**Exécution par groupe** (lance smoke sur api-tests et ui-tests)
```bash
mvn test -Dgroups=smoke
```

### Groupes de tests
| Groupe | Module     | Description               |
|------|------------|---------------------------|
| `api, smoke` | `api-tests` | Vérifie `GET /productsList` → **200** |
| `ui, smoke`  | `ui-tests`  | CT-AUTH-001, CT-CART-001, CT-CHECKOUT-001 |

## CI (GitHub Actions)
- Objectif : feedback rapide sur la stabilité de l'application
- Job api-smoke : exécute le smoke API
- Job ui-smoke : exécute le smoke UI en headless (uniquement si TEST_USER_EMAIL et TEST_USER_PASSWORD sont définis dans les Secrets GitHub)

Pour ajouter les secrets GitHub : Settings -> Secrets and variables -> Actions

## Documentation QA
Tout est dans docs/ : 
- Exigences fonctionnelles
- Cas de test
- Matrice de traçabilité
- Journal d'exécution
- Rapport de bugs
- Stratégie de test
- Chartes de tests exploratoires

## Preuves d'exécution
Les captures et preuves sont disponibles dans le dossier : `docs/preuves`.

## État du projet
- Documentation QA : finalisée
- Tests manuels : réalisés
- Automatisation smoke : réalisée
- Régression automatisée : prévue
- CI : en place (smoke), UI nécessite secrets

## Notes
Ce projet est réalisé sur un site tiers de démonstration, présence possible d'overlays/publicités (ex: #google_vignette). Le projet intègre donc des contournements.


## Auteur
Projet réalisé dans un objectif de montée en compétences QA et de présentation professionnelle.
