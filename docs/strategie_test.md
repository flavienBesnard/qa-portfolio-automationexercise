# stratégie de test (Phase 0 - socle technique)

## Objectif et périmètre
- **Objectif** : disposer d'un socle reproductible pour la suite du projet (smokes UI/API, CI verte).
- **Périmètre** : UI Web + API publiques d'AutomationExercise (couverte de façon légère).
- **Hors périmètre** Phase 0 : performances et sécurité (seront traités sur app/mocks locales).

## Environnements et exécution
- **Local Windows (PowerShell)** : 
  - API : `mvn -pl api-tests -am -DAPI_BASE_URL=https://automationexercise.com/api test`  
  - UI  : `mvn -pl ui-tests -am -DBASE_URL=https://automationexercise.com test`
- **CI GitHub (runner Linux)** : workflow `ci-min.yml` avec service container **Selenium** (Chrome **headless**).
- **Cibles** : EXTERNAL_TARGET uniquement (site de démo), pas de charge (pas de tests de perf sur un site tiers).

## Données de test
- Générées à la volée (Faker sera utilisé en Phase 3-4).
- Les smokes sont **idempotents** →  Pas de persistance obligatoire en Phase 0.

## Types et niveaux (Phase 0)
- Niveaux : API (1 smoke), UI (1 smoke).
- Types : **Smoke** (présence/chemin critique uniquement).
- Critères d'arrêt : smokes verts en local et en CI.

## Risques adressés (Phase 0)
- **R1 “chez moi/CI”** → versions figées (JDK 21), navigateur en **conteneur Selenium**.
- **R2 instabilité d’URL/env** → **variables d’environnement** (`BASE_URL`, `API_BASE_URL`, `SELENIUM_REMOTE_URL`).
- **R3 flaky de démarrage** → **smokes** très courts + exécution **headless** en CI.

## Prochaines étapes (Phases 1+)
- Définir exigences, concevoir cas de test H/M/B puis automatiser API -> UI, Allure, mocks,a11y/perf/sécu.


## Glossaire (TP)
- **CI (Continuous Integration)** : exécuter automatiquement les tests à chaque push (GitHub Actions) pour un feedback rapide.
- **Smoke tests** : tests vitaux, très courts. S’ils échouent, on arrête.
- **Headless** : navigateur sans interface (plus rapide/stable en CI).
- **EXTERNAL_TARGET** : cible = site externe de démo. Tests **fonctionnels légers** uniquement.
- **Idempotent** : rejouer le test ne change pas l’état (ex. GET, lecture de titre).
- **Flaky** : test instable (passe/échoue sans changement de code). On limite ça avec des smokes simples, headless et une config propre.
- **Mock** : faux service/API (ex. WireMock) pour stabiliser ou simuler des erreurs (utilisé plus tard).
