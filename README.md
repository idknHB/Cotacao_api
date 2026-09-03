# Currency Converter

> A desktop application built with Java and JavaFX for real-time currency conversion using live exchange rates.

Currency Converter is a desktop application designed to provide fast and intuitive currency conversion between multiple international currencies.

The project was developed as a practical study of Java desktop development, REST API consumption, software architecture, asynchronous operations, caching strategies, and user interface design.

---

## 💱 Project Overview

The application allows users to convert values between different currencies in real time using exchange rates obtained from an external API.

Unlike traditional calculators, the converter supports bidirectional conversion, allowing the user to edit either field while automatically updating the opposite value.

The project focuses on providing a clean user experience while maintaining a well-structured and maintainable codebase.

---

## ✨ Features

### Currency Conversion

- Real-time exchange rate retrieval
- Support for multiple international currencies
- Bidirectional conversion
- Automatic conversion while typing
- Currency swap functionality

### Performance

- Exchange rate caching
- Reduced API requests
- Faster currency switching
- Improved UI responsiveness

### User Interface

- Modern dark theme
- Google-inspired layout
- Real-time updates
- Simplified workflow
- Responsive controls

---

## 🛠️ Technologies

- Java 21
- JavaFX
- Gson
- Maven
- Exchange Rate API
- Git / GitHub

---

## 🏗️ Architecture

The application follows a layered architecture to improve maintainability and separation of responsibilities.

```text
com.renan

├── model
│   ├── CurrencyResponse
│   └── Moeda
│
├── service
│   ├── ApiCliente
│   └── ConversorService
│
├── ui
│   ├── MainView
│   └── Styles
│
└── Main
```

### Responsibilities

#### Model

Responsible for representing application data and API responses.

#### Service

Handles communication with external services, caching and conversion logic.

#### UI

Responsible for JavaFX components, event handling and user interactions.

---

## ⚙️ Technical Highlights

### REST API Consumption

The application retrieves live exchange rates from an external API and maps the JSON responses using Gson.

### Caching System

To avoid unnecessary requests and improve performance, exchange rates are cached after the first request.

### Bidirectional Conversion

Users can type values in either currency field, and the application automatically performs the reverse conversion when needed.

### Event Management

Special handling was implemented to prevent recursive updates between text fields during automatic conversions.

---

## 📈 Development Journey

During development, several improvements were implemented:

- Initial single-direction conversion
- Currency name support
- Layer separation (UI / Service / Model)
- Conversion cache implementation
- Automatic currency swapping
- Bidirectional conversion
- Interface redesign
- Performance optimization

---

## 🎯 Learning Goals

This project was developed to improve practical experience with:

- Java desktop development
- JavaFX
- REST APIs
- JSON serialization/deserialization
- Software architecture
- Event-driven programming
- Caching strategies
- Git version control
- User interface design

---

## 🚧 Project Status

**Version 1.0 — Completed**

The application is fully functional and currently fulfills its original objectives. Future improvements may focus on visual enhancements and additional usability features.

---

## 👨‍💻 Author

**Renan Hammerschlag**

Developed as a personal learning and portfolio project.