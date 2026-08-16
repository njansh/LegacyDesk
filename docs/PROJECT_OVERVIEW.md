Sim. O próximo documento é o **`PROJECT_OVERVIEW.md`**.

Ele será a apresentação oficial do projeto: alguém que entrar no repositório deve conseguir ler esse arquivo e entender **o que é o LegacyDesk, qual problema ele resolve, para quem existe e quais são os limites do projeto**, sem precisar abrir as issues.

Eu sugiro deixá-lo assim:

````md
# LegacyDesk

## Service Management Platform

LegacyDesk is a backend system for managing customer service requests in a company.

The project simulates the development of a real-world system using a deliberately constrained legacy technology stack.

The objective is to solve modern software and security problems while working with technologies and approaches commonly found in older systems.

---

# 1. Project Context

Companies often depend on internal systems to manage customer requests, employees, assignments and service workflows.

Over time, these systems can become difficult to maintain because of:

- outdated technologies;
- accumulated business rules;
- weak access control;
- excessive exposure of data;
- integrations with external systems;
- duplicated operations;
- lack of proper auditing;
- limited observability.

LegacyDesk simulates this environment.

The system is intentionally developed using an older technology stack so that modern problems must be solved without simply relying on the newest language and framework features.

---

# 2. Problem

The company currently needs a centralized system to manage customer service requests.

Without such a system, employees may have difficulty:

- identifying which requests are pending;
- assigning requests to technicians;
- tracking request status;
- identifying who performed an action;
- reviewing the history of a request;
- controlling access to customer information;
- preventing unauthorized changes;
- dealing with duplicated requests;
- integrating with existing legacy systems.

The system must provide these capabilities while protecting the company's data.

---

# 3. Objective

The objective of LegacyDesk is to provide a backend API capable of managing the complete lifecycle of a customer service request.

The system must support:

- authentication;
- authorization;
- customer management;
- employee management;
- service requests;
- technicians;
- request workflow;
- comments;
- history;
- auditing;
- security controls;
- integration with a legacy system;
- asynchronous processing;
- idempotent operations.

---

# 4. Users

The initial system contains four main employee roles.

## ATTENDANT

Responsible for customer service operations.

Can:

- register customers;
- create service requests;
- view requests according to authorization rules;
- add relevant information to requests.

---

## TECHNICIAN

Responsible for handling assigned requests.

Can:

- view requests assigned to them;
- update the appropriate request information;
- work through the service workflow;
- add comments.

---

## SUPERVISOR

Responsible for operational management.

Can:

- assign requests to technicians;
- monitor requests;
- manage operational workflows;
- access information required for supervision.

---

## ADMIN

Responsible for system administration.

Can:

- manage employees;
- manage roles;
- manage system-level configuration;
- perform administrative operations.

---

# 5. Main Domain

The initial domain contains the following concepts:

```text
User
Customer
Request
Comment
RequestHistory
AuditEvent
````

Additional technical or integration models may exist when required by the architecture.

---

# 6. Request Lifecycle

A service request follows a controlled lifecycle.

Initial state:

```text
OPEN
```

Possible states include:

```text
OPEN
IN_PROGRESS
WAITING_CUSTOMER
RESOLVED
CANCELLED
```

The system must not allow arbitrary status changes.

Valid transitions are defined by business rules.

---

# 7. Request Priorities

Requests may have different priorities:

```text
LOW
NORMAL
HIGH
URGENT
```

Priority affects how the request is handled and how it appears in operational queries.

---

# 8. Security

Security is one of the main concerns of the project.

The system must protect against unauthorized access and unintended data exposure.

Security concerns include:

* authentication;
* authorization;
* object-level authorization;
* function-level authorization;
* sensitive data exposure;
* mass assignment;
* insecure error handling;
* sensitive information in logs;
* improper access to resources;
* unauthorized modification of data.

Security will not be treated only as configuration.

The project will include tests designed to intentionally attempt unauthorized operations.

---

# 9. Data Protection

The system must expose only the information necessary for each operation.

Internal entities must not automatically become API responses.

Sensitive information must not be exposed through:

* API responses;
* logs;
* exceptions;
* stack traces;
* configuration;
* documentation.

The system must also prevent clients from modifying internal fields that they should not control.

---

# 10. Auditing

Important operations must generate audit records.

An audit event should allow the system to answer:

```text
Who performed the action?
What action was performed?
When did it happen?
Which resource was affected?
What changed?
```

Examples include:

* status changes;
* assignment changes;
* role changes;
* administrative actions;
* sensitive data modifications.

---

# 11. Legacy Integration

LegacyDesk will communicate with an external legacy system.

The external system must be considered unreliable.

It may:

* become unavailable;
* respond slowly;
* return invalid data;
* return errors;
* process requests incompletely.

The application must handle these situations without compromising its own consistency.

---

# 12. Idempotency

The system must consider duplicated requests.

For example:

```text
Client
   |
   | POST request
   v
Server
   |
   | operation completed
   |
   X response lost
   |
Client retries
   |
   v
Server
```

A retry must not automatically create a duplicated business operation.

---

# 13. Asynchronous Processing

Some operations may not need to block the main request.

The project will therefore include asynchronous processing.

The implementation must consider:

* concurrency;
* failure;
* retry;
* duplicated execution;
* consistency.

The chosen solution must remain compatible with the project's legacy technology constraints.

---

# 14. Technology Constraints

LegacyDesk intentionally uses:

* Java 8;
* Spring Boot 2.7.x;
* Maven;
* PostgreSQL;
* Docker;
* REST API.

The project should not introduce modern technologies simply because they make implementation easier.

When a problem is encountered, the solution must first be evaluated within the constraints of the project.

---

# 15. Architecture

The backend will use a modular architecture.

The system should separate responsibilities between:

```text
Domain
Application / Use Cases
Infrastructure
API
```

The exact internal structure may evolve as the project develops.

The main architectural principle is:

> Business rules should not depend directly on infrastructure details.

---

# 16. API

The system will expose a REST API.

The API will provide operations for:

* authentication;
* users;
* customers;
* requests;
* comments;
* history;
* auditing;
* reports;
* integrations.

API responses should use dedicated representations rather than exposing persistence entities directly.

---

# 17. Database

PostgreSQL will be used as the primary database.

Database changes must be versioned using Flyway.

The database schema must evolve through migrations rather than manual changes.

---

# 18. Testing

Testing will focus primarily on behavior.

Important areas include:

* authentication;
* authorization;
* business rules;
* request lifecycle;
* data access;
* security;
* idempotency;
* asynchronous processing;
* integrations;
* API behavior.

Security tests must attempt to violate authorization rules rather than only testing successful requests.

---

# 19. MVP 1 Scope

The first MVP includes:

* authentication;
* authorization;
* customer management;
* employee management;
* service requests;
* request workflow;
* comments;
* history;
* auditing;
* security testing;
* legacy integration;
* asynchronous processing;
* idempotency;
* reports;
* API documentation;
* automated tests.

The complete implementation is tracked through the project's GitHub issues.

---

# 20. Outside the MVP

The following are intentionally outside the initial scope:

* mobile application;
* complete frontend;
* payment processing;
* WhatsApp integration;
* SMS;
* chatbot;
* microservices migration;
* Kubernetes;
* Kafka;
* artificial intelligence;
* complex dashboards.

These may be considered as future projects or extensions.

---

# 21. Project Goal

LegacyDesk is not intended to demonstrate only that a backend can be built.

The project is intended to demonstrate the ability to work under constraints.

The central challenge is:

> Build a system that solves modern problems using an older technology stack while maintaining good architecture, security, reliability and maintainability.

---

# 22. Development Process

The development process is documented separately in:

`PROJECT_PROCESS.md`

The project roadmap and implementation tasks are tracked through GitHub Issues.
