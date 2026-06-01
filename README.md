# AdminLTE 4 + Spring Boot 4 Demo

Demo-Anwendung, die das **AdminLTE 4** UI-Template in einer **Spring Boot 4** /
**Thymeleaf**-Applikation mit **OAuth2 Login** gegen einen lokalen
**Keycloak** verwendet.

Alle AdminLTE-Assets und Vendor-Bibliotheken werden als **WebJars** über
`/webjars/**` ausgeliefert — es gibt keine eingecheckten Static-Assets und
keinen Frontend-Build-Schritt. CDN-Verweise wurden vollständig durch lokale
WebJars ersetzt, die App läuft damit komplett offline.

## Voraussetzungen

- JDK **21 LTS**
- Maven 3.9+ (oder der beigelegte `./mvnw`-Wrapper)
- Docker (für Keycloak)

## Setup

### 1. Keycloak starten

```bash
docker compose up -d
```

Keycloak ist dann unter <http://localhost:8180> erreichbar
(Admin-Konsole: `admin` / `admin`). Beim ersten Start wird der Realm
`adminlte-demo` aus `keycloak/adminlte-demo-realm.json` importiert.

Vorab angelegt:

- **Realm:** `adminlte-demo`
- **Client:** `adminlte-springboot` (Confidential, Standard Flow)
- **Client Secret:** `change-me-in-keycloak` (im Realm-Export gesetzt)
- **Redirect URI:** `http://localhost:8080/login/oauth2/code/keycloak`
- **Demo-User:** `demo` / `demo`

> Für eine echte Umgebung **unbedingt** Secret + User-Passwort über die
> Keycloak-Admin-Konsole rotieren.

### 2. Anwendung starten

```bash
./mvnw spring-boot:run
# oder
mvn spring-boot:run
```

App: <http://localhost:8080>

Über *Login* in der Topbar oder den Button auf der Startseite startet der
OIDC-Flow gegen Keycloak. Nach Login zeigt `/dashboard` ein paar
AdminLTE-Widgets sowie die OIDC-Claims des angemeldeten Users.

## Demo-Seiten

Neben dem geschützten `/dashboard` sind zahlreiche AdminLTE-Demo-Seiten als
**öffentlich erreichbare** Thymeleaf-Templates portiert (siehe `permitAll` in
`SecurityConfig` und das Routing in `WebMvcConfig`):

| Bereich | Routen |
|---|---|
| Dashboards | `/dashboard/v2`, `/dashboard/v3` |
| Theme-Generator | `/generate/theme` |
| Widgets | `/widgets/{small-box,info-box,cards}` |
| Layout-Varianten | `/layout/{unfixed-sidebar,fixed-sidebar,fixed-header,fixed-footer,fixed-complete,layout-custom-area,sidebar-mini,collapsed-sidebar,collapsed-sidebar-without-hover,logo-switch,layout-rtl}` |
| UI-Elemente | `/UI/{general,icons,timeline}` |
| Mailbox | `/mailbox/{inbox,read,compose}` |
| Formulare | `/forms/{elements,layout,validation,wizard}` |
| Tabellen | `/tables/{simple,data}` |
| Seiten | `/pages/{profile,settings,invoice,calendar,kanban,chat,file-manager,projects,pricing,faq,404,500,maintenance}` |
| Auth-Beispiele | `/examples/{login,register,login-v2,register-v2,lockscreen}` |

Diese Seiten sind reine Layout-/Routing-Mappings über `WebMvcConfig` und
benötigen keinen eigenen Controller.

## Konfiguration

Sensible Werte lassen sich per Umgebungsvariable überschreiben:

| Variable | Default | Bedeutung |
|---|---|---|
| `KEYCLOAK_CLIENT_SECRET` | `change-me-in-keycloak` | Secret des Keycloak-Clients |

Weitere relevante Defaults aus `application.yml`:

- App-Port `8080`, `forward-headers-strategy: framework` (für Reverse-Proxy-Setups)
- Keycloak-Issuer `http://localhost:8180/realms/adminlte-demo`
- `spring.thymeleaf.cache: false` (nur für Entwicklung)

## Frontend-Assets (WebJars)

AdminLTE und alle Vendor-Libs kommen als Maven-Dependencies und werden unter
`/webjars/<artifactId>/<pfad>` ausgeliefert. Dank `webjars-locator-lite`
referenzieren die Templates **versionslos**, z. B.:

```html
<link rel="stylesheet" th:href="@{/webjars/adminlte/css/adminlte.min.css}"/>
<script th:src="@{/webjars/bootstrap/js/bootstrap.min.js}"></script>
```

Verwendete WebJars (Versionen in `pom.xml`):

- **In-house** (`de.l9g.webjars`): `adminlte` 4.0.0, `jsvectormap` 1.5.3,
  `source-sans-3` 5.0.12
- **Public**: `bootstrap` 5.3.8, `popperjs__core` 2.11.8, `bootstrap-icons`
  1.13.1, `overlayscrollbars` 2.11.4, `apexcharts` 3.54.1, `sortablejs` 1.15.0,
  `fullcalendar` 6.1.15, `tabulator-tables` 6.3.1

> AdminLTE 4 wird hier als eigenes WebJar (`de.l9g.webjars:adminlte`)
> bereitgestellt, da es kein offizielles AdminLTE-4-WebJar gibt.

## Projektstruktur

```
adminlte4-springboot/
├── pom.xml                              # Spring Boot 4, WebJar-Dependencies
├── docker-compose.yml                   # Keycloak 26 (Port 8180)
├── keycloak/adminlte-demo-realm.json    # Realm + Client + Demo-User
└── src/
    ├── main/java/com/example/adminltedemo/
    │   ├── AdminLteDemoApplication.java
    │   ├── config/
    │   │   ├── SecurityConfig.java      # OAuth2 Login + RP-initiated Logout
    │   │   └── WebMvcConfig.java        # View-Controller für die Demo-Seiten
    │   └── web/HomeController.java       # "/" und "/dashboard"
    ├── main/resources/
    │   ├── application.yml              # OAuth2 Client → Keycloak
    │   └── templates/
    │       ├── layout/main.html         # AdminLTE-Grundlayout (Layout-Dialect)
    │       ├── layout/blank.html        # Layout ohne Sidebar (Auth-Seiten)
    │       ├── fragments/{header,sidebar,footer}.html
    │       ├── index.html  dashboard.html
    │       └── {dashboard,widgets,layout,UI,mailbox,forms,
    │            tables,pages,examples,generate,error}/…  # Demo-Seiten
    └── test/java/com/example/adminltedemo/
        └── SmokeTest.java               # Kontext-Start + "/" erreichbar
```

## Hinweise

- AdminLTE-CSS/JS und alle Vendor-Libs werden unter `/webjars/**` ausgeliefert
  (`SecurityConfig` lässt diesen Pfad permitAll).
- Logout ist **RP-initiated OIDC Logout** — nach `/logout` wird auch die
  Keycloak-Session beendet; danach Redirect auf `/`.
- Nach erfolgreichem Login wird per `defaultSuccessUrl("/dashboard", true)`
  immer auf `/dashboard` weitergeleitet.
- Dark Mode nutzt Bootstrap 5.3 `data-bs-theme` (Light/Dark/Auto-Umschaltung in
  der Topbar, Auswahl wird in `localStorage` unter `lte-theme` gespeichert).
- `spring.thymeleaf.cache: false` ist nur für die Entwicklung sinnvoll; in
  Produktion über ein eigenes Profil aktivieren.
- Der Smoke-Test mockt das `ClientRegistrationRepository`, damit der Kontext
  ohne laufenden Keycloak hochfährt.
```

