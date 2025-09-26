# Progetto IDS – Guida rapida

Applicazione web Spring Boot per un marketplace agroalimentare con catalogo (prodotti e materie prime), carrello/ordini, eventi e dashboard per ruolo. Database H2 in‑memory e autenticazione JWT.

## Avvio (Windows PowerShell)

```powershell
.\u200cgradlew.bat bootRun
```

Oppure build + jar:

```powershell
.\u200cgradlew.bat build
java -jar .\build\libs\ProgettoIDS-0.0.1-SNAPSHOT.jar
```

Apri: http://localhost:8080

## Utenti di prova (seed)

- gestore / gestore (Gestore Piattaforma)
- curatore / curatore (Curatore)
- animatore / animatore (Animatore Filiera)
- acquirente / acquirente (Acquirente)
- produttore / produttore (Produttore)
- trasformatore / trasformatore (Trasformatore)
- distributore / distributore (Distributore Tipicità)

## Cosa puoi fare

- Catalogo pubblico: articoli approvati, dettaglio, aziende, eventi.
- Venditori: propongono prodotti/materie prime (da approvare dal Curatore).
- Curatore: approva articoli/prodotti/materie prime.
- Animatore: crea eventi (il Gestore li approva).
- Acquirente: carrello, checkout e storico ordini.
- Gestore: approva utenti e eventi; vista admin.

Vai su `/dashboard`: verrai instradato alla dashboard corretta in base al ruolo.

## Link utili

- Home e catalogo: `/`, `/articoli`, `/azienda/{id}`, `/eventi`
- Login/Registrazione: `/login`, `/register`
- Carrello/Ordini: `/carrello`, `/ordini`
- Console H2: `/h2-console` (JDBC URL: `jdbc:h2:mem:progettoids`)

Nota: il DB è in‑memory; i dati si ricreano ad ogni avvio grazie al seed.

## Autori

- Antonio Pennino
- Mariano Iscaro
- Damiano Montanaro
