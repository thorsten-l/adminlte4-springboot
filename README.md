# AdminLTE 4 + Spring Boot 4 Demo

Demo-Anwendung, die das **AdminLTE 4** UI-Template in einer **Spring Boot 4** /
**Thymeleaf**-Applikation mit **OAuth2 Login** gegen einen lokalen
**Keycloak** verwendet.

## Voraussetzungen

- JDK **21 LTS**
- Maven 3.9+ (oder das beigelegte Wrapper-Setup deiner IDE)
- Docker (für Keycloak)

Die AdminLTE-Assets liegen bereits eingecheckt unter
`src/main/resources/static/adminlte/{css,js,assets}` — kein Vorbau-Schritt
nötig. Zum Aktualisieren der Assets siehe Abschnitt
*AdminLTE-Assets aktualisieren* unten.

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

## Konfiguration

Sensible Werte lassen sich per Umgebungsvariable überschreiben:

| Variable | Default | Bedeutung |
|---|---|---|
| `KEYCLOAK_CLIENT_SECRET` | `change-me-in-keycloak` | Secret des Keycloak-Clients |

## Projektstruktur

```
adminlte4-springboot/
├── pom.xml                              # Spring Boot 4
├── docker-compose.yml                   # Keycloak 26 (Port 8180)
├── keycloak/adminlte-demo-realm.json    # Realm + Client + Demo-User
└── src/main/
    ├── java/com/example/adminltedemo/
    │   ├── AdminLteDemoApplication.java
    │   ├── config/SecurityConfig.java   # OAuth2 Login + RP-initiated Logout
    │   └── web/HomeController.java
    └── resources/
        ├── application.yml              # OAuth2 Client → Keycloak
        ├── static/adminlte/             # eingecheckte AdminLTE-Dist-Assets
        │   ├── css/  js/  assets/
        └── templates/
            ├── layout/main.html         # AdminLTE-Grundlayout (Layout-Dialect)
            ├── fragments/{header,sidebar,footer}.html
            ├── index.html               # Start
            └── dashboard.html           # geschützt
```

## AdminLTE-Assets aktualisieren

Die Assets unter `src/main/resources/static/adminlte/` sind aus dem
AdminLTE-Repo gebaut und committed. Zum Aktualisieren auf eine neuere
AdminLTE-Version:

```bash
cd ../AdminLTE
npm install
npm run build
cp -R dist/css  ../adminlte4-springboot/src/main/resources/static/adminlte/
cp -R dist/js   ../adminlte4-springboot/src/main/resources/static/adminlte/
cp -R dist/assets ../adminlte4-springboot/src/main/resources/static/adminlte/
```

## Hinweise

- AdminLTE-CSS/JS werden unter `/adminlte/**` ausgeliefert
  (`SecurityConfig` lässt diesen Pfad permitAll).
- Logout ist **RP-initiated OIDC Logout** — nach `/logout` wird auch die
  Keycloak-Session beendet.
- `spring.thymeleaf.cache: false` ist nur für die Entwicklung sinnvoll; in
  Produktion über ein eigenes Profil deaktivieren.
- Der Smoke-Test mockt das `ClientRegistrationRepository`, damit der Kontext
  ohne laufenden Keycloak hochfährt.
