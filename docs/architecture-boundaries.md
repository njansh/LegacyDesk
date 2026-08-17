# Backend Modular Architecture & Layer Boundaries

## 1. Architectural Style: Clean / Hexagonal Layering

LegacyDesk follows a decoupled modular structure to ensure business logic remains independent of frameworks (Spring Boot 2.7.x), persistence mechanisms (JPA/Hibernate), and external transport protocols (REST/JSON).

```text
+-------------------------------------------------------------------+
|                           Web Layer                               |
|   (REST Controllers, Security Filters, Request/Response DTOs)     |
+---------------------------------+---------------------------------+
                                  |
                                  v
+---------------------------------+---------------------------------+
|                       Application Layer                           |
|       (Use Cases / Interactors, Input/Output Ports, Mappers)       |
+---------------------------------+---------------------------------+
                                  |
                                  v
+---------------------------------+---------------------------------+
|                         Domain Layer                              |
|   (Entities, Value Objects, Domain Enums, Domain Exceptions,      |
|                    Repository Interfaces / Ports)                 |
+-------------------------------------------------------------------+
                                  ^
                                  | (implements Repository Ports)
+---------------------------------+---------------------------------+
|                      Infrastructure Layer                         |
|   (JPA Entities, Spring Data Repositories, Flyway, Legacy Client, |
|                Async Executors, Security Adapters)                |
+-------------------------------------------------------------------+

```

---

## 2. Layer Responsibilities & Strict Boundaries

### 2.1 Domain Layer (`com.legacydesk.core.domain`)

* **Strict Rule:** Zero external framework dependencies. No Spring `@Component`, no JPA `@Entity`, no Jackson annotations.
* **Contains:**
* Pure domain models and aggregate roots (`User`, `Customer`, `Request`, `Comment`).
* Domain exceptions (`InvalidStatusTransitionException`, `CustomerInactiveException`).
* Repository Interfaces / Outbound Ports (`UserRepository`, `RequestRepository`).
* Domain business invariants and validation rules.



### 2.2 Application Layer (`com.legacydesk.core.application`)

* **Contains:**
* Use Cases / Services orchestrating domain operations (`CreateRequestUseCase`, `AssignTechnicianUseCase`).
* Inbound commands and query contracts.
* Transaction boundary coordination (`@Transactional`).


* **Strict Rule:** Controllers never bypass this layer to access repositories directly. Business logic belongs inside domain entities or dedicated domain services, not in application orchestrators.

### 2.3 Web Layer (`com.legacydesk.entrypoint.rest`)

* **Contains:**
* REST Controllers (`@RestController`, `@RequestMapping`).
* Request payload contracts (Command DTOs) validated via `@Valid` / JSR-303.
* Response projections (Response DTOs) to prevent excessive data exposure.
* Global exception handlers (`@ControllerAdvice`) converting domain exceptions to standardized HTTP error responses (RFC 7807 / unified error format).


* **Strict Rule:** Controllers must never accept or return domain entities or JPA entities directly.

### 2.4 Infrastructure Layer (`com.legacydesk.infrastructure`)

* **Contains:**
* JPA Entity adapters and Spring Data repository interfaces.
* Flyway database migrations (`db/migration`).
* Legacy system integration HTTP clients (Java 8 compatible).
* Asynchronous execution policies and thread pool configurations.
* Audit log event listeners and persistence mechanisms.



---

## 3. Package Layout

```text
com.legacydesk
├── domain
│   ├── model          # Pure domain models (User, Request, Customer, etc.)
│   ├── exception      # Domain specific business exceptions
│   └── repository     # Outbound repository interfaces (Ports)
├── application
│   ├── usecase        # Application use cases / interactors
│   └── dto            # Application-level commands and query results
├── infrastructure
│   ├── persistence    # JPA entities, Spring Data Repositories, Mappers
│   ├── legacy         # External legacy client adapters
│   ├── async          # Concurrency and thread pool infrastructure
│   ├── audit          # Audit log persistence and interceptors
│   └── config         # Spring configuration beans
└── entrypoint
    └── rest
        ├── controller # REST Controllers
        ├── dto        # Request/Response payloads
        └── handler    # Global @ControllerAdvice exception mapper

```

---

## 4. Key Architectural Decisions

1. **Dependency Inversion:** Higher-level domain modules do not depend on lower-level infrastructure modules. Both depend on abstractions (repository interfaces defined in domain).
2. **Framework Isolation:** Domain entities can be fully tested with pure unit tests without bootstrapping the Spring application context.
3. **No Mass Assignment:** Incoming JSON maps strictly to input DTOs, which are transformed into domain commands after sanitization.

