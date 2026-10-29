# GuessZA 🇿🇦
### A South African GeoGuessr Clone

> Drop a pin. Find out how well you know South Africa.

![GuessZA Gameplay](assets/readme.gif)

---

## What is it?

GuessZA shows you a Google Street View panorama from somewhere in South Africa. You drop a pin on a map where you think it is, and get scored based on how close your guess was. Five rounds per game, 5000 points per round max.

---

## Built With

| Layer | Technology |
|---|---|
| Backend | Spring Boot |
| ORM | Spring Data JPA |
| Database | PostgreSQL |
| Migrations | Flyway |
| Containerisation | Docker |
| Maps & Street View | Google Maps JavaScript API |
| Frontend | Vanilla HTML / CSS / JS |

---

## How to Run

### Prerequisites

- [Docker](https://www.docker.com/products/docker-desktop)
- A [Google Maps JavaScript API key](https://developers.google.com/maps/documentation/javascript/get-api-key)

### Steps

**1. Clone the repo**
```bash
git clone https://github.com/thembu/guessza.git
cd guessza
```

**2. Create your `.env` file**

Database credentials are read from a `.env` file in the project root, which is gitignored so your credentials never get committed. Copy the template and fill in your own values:

```bash
cp .env.example .env
```

On Windows PowerShell, use `Copy-Item .env.example .env`.

Docker Compose loads `.env` automatically. Postgres only applies these credentials the **first time** it creates the database, so if you change them later, reset the database (see [Notes](#notes)).

**3. Add your Google Maps API key**

In `src/main/resources/static/game.html` and `result.html`, find this line and replace `YOUR_API_KEY` with your key:

```html
<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initGoogleMaps" async defer></script>
```

**4. Start the app**
```bash
docker compose up --build
```

This starts both the PostgreSQL database and the Spring Boot application. Flyway runs the schema migrations automatically on first start.

**5. Open the game**

Go to [http://localhost:8080](http://localhost:8080)

---

## Notes

- To reset the database and reseed from scratch: `docker compose down -v && docker compose up --build` (this deletes all users and games)
- The location pool contains 30 curated South African locations across all 9 provinces
