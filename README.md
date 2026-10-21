# Currency Converter

A JavaFX desktop application for converting currencies using exchange rates from the [open.er-api.com](https://open.er-api.com/) service. It supports conversion in either direction, currency selection, and swapping the source and destination currencies.

## Features

- Load the currency list from the exchange-rate service
- Convert between supported currencies while typing in either amount field
- Swap the source and destination currencies
- Cache exchange-rate responses by source currency for the lifetime of the application
- Display currency codes and localized names

## Technology

- Java 21
- JavaFX 21
- Gson 2.13.1
- Java `HttpClient`
- Maven and JavaFX Maven Plugin

## Run locally

Requirements: Java 21 and a graphical desktop environment.

From the repository root:

```bash
mvn javafx:run
```

The Maven JavaFX plugin launches `com.renan.Main`.

## How it works

The source tree separates the JavaFX interface (`ui`), currency and response models (`model`), and conversion and HTTP code (`service`). `ApiCliente` requests the latest rates for a base currency. `ConversorService` parses the JSON response with Gson, caches it by base currency, and multiplies the entered amount by the selected destination rate. The interface uses JavaFX listeners to recalculate either direction and guards updates to prevent the two fields from repeatedly updating each other.

```text
src/main/java/com/renan/
├── Main.java
├── model/
│   ├── CurrencyResponse.java
│   └── Moeda.java
├── service/
│   ├── ApiCliente.java
│   └── ConversorService.java
└── ui/
    ├── MainView.java
    └── Styles.java
```

## Current scope and limitations

This is a completed personal learning project. The HTTP request is synchronous when a rate is not yet cached, so conversion can temporarily block the JavaFX interface on a cache miss. The cache has no expiration, and the HTTP client currently returns the response body without checking the HTTP status. These are useful areas for future improvements, along with clearer user-facing error messages and automated tests.

## Author

Renan Hammerschlag
