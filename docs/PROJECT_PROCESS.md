# LegacyDesk — Development Process

## 1. Purpose

LegacyDesk is a software engineering study project designed to simulate the development of a real-world backend under legacy technology constraints.

The project is not focused only on delivering features.

The main objective is to develop the ability to:

- understand business requirements;
- design software before implementation;
- make architectural decisions;
- work with legacy technology;
- solve modern problems using older tools;
- reason about security;
- identify failure scenarios;
- write maintainable code;
- test business rules;
- review and improve implementations.

The project should be treated as if it were developed by a small software engineering team.

---

# 2. Technology Constraints

The project intentionally uses an older technology stack.

## Required

- Java 8
- Spring Boot 2.7.x
- Maven
- PostgreSQL
- Docker
- REST API

## Constraint

Modern language and framework features must not be introduced only because they make implementation easier.

When a problem appears, the first question should be:

> "How would we solve this using the technologies available in this project?"

The goal is not to artificially write bad code.

The goal is to understand how the same engineering problem was solved before newer abstractions and language features existed.

---

# 3. Development Philosophy

The project follows a problem-first approach.

We do not start with:

> "Which technology should we use?"

We start with:

> "What problem are we solving?"

Then:

1. Understand the requirement.
2. Identify constraints.
3. Identify possible solutions.
4. Evaluate trade-offs.
5. Choose a solution.
6. Implement.
7. Test.
8. Review.
9. Refactor when necessary.

Technology should serve the problem.

---

# 4. Issue-Driven Development

Every significant piece of work must have an issue.

An issue should describe:

- the problem;
- the expected behavior;
- acceptance criteria;
- relevant constraints.

Issues are the source of truth for project work.

Do not implement unrelated features simply because they seem interesting.

If a new requirement appears during development, create a new issue or update the existing requirement before implementing it.

---

# 5. Branching Strategy

The `main` branch must remain stable.

Direct pushes to `main` are not allowed.

Each issue should normally have its own branch.

Branch format:

```text
feature/<issue>-<description>
fix/<issue>-<description>
chore/<issue>-<description>
refactor/<issue>-<description>
test/<issue>-<description>
docs/<issue>-<description>
````

Examples:

```text
feature/16-create-customer
feature/25-create-request
fix/35-invalid-status-transition
chore/4-configure-postgresql
test/65-idempotency-tests
```

---

# 6. Commits

Commits should be small and describe one logical change.

Use:

```text
feat:
fix:
chore:
refactor:
test:
docs:
```

Examples:

```text
feat: create customer domain
feat: implement customer repository
test: add customer validation tests
fix: prevent inactive users from authenticating
chore: configure flyway
docs: document authentication flow
```

Avoid commits such as:

```text
update
changes
final
teste
fix
coisas
```

---

# 7. Pull Requests

Every change should go through a Pull Request.

A PR should:

* reference its issue;
* have a clear description;
* contain only the necessary changes;
* include tests when applicable;
* explain relevant technical decisions;
* contain no secrets.

Preferred PR size:

```text
small and focused
```

Large PRs should be divided whenever possible.

---

# 8. Code Review

Code review is part of development, not a final ceremony.

The reviewer should evaluate:

## Correctness

* Does the implementation solve the issue?
* Are business rules respected?
* Are edge cases handled?

## Architecture

* Are responsibilities correctly separated?
* Is business logic outside controllers?
* Is infrastructure isolated from the domain?
* Is the implementation unnecessarily coupled?

## Security

* Can another user access this resource?
* Can a user perform an action outside their role?
* Are sensitive fields exposed?
* Can client-controlled data modify protected properties?
* Are errors exposing internal information?
* Are secrets present in logs or source code?

## Persistence

* Are transactions appropriate?
* Are queries efficient?
* Could this produce N+1 queries?
* Are indexes necessary?

## Testing

* Are important business rules tested?
* Are failure scenarios tested?
* Are security boundaries tested?

---

# 9. Security Philosophy

Security is a first-class requirement of LegacyDesk.

Authentication alone does not mean the system is secure.

The project must consider:

* authentication;
* authorization;
* object-level access control;
* function-level access control;
* sensitive data exposure;
* mass assignment;
* insecure configuration;
* injection;
* logging;
* error handling;
* resource consumption.

The project will use OWASP API Security guidance as a reference when evaluating API security. OWASP specifically identifies risks such as broken object-level authorization, broken authentication, excessive data exposure, broken function-level authorization and mass assignment. ([Guia do Desenvolvedor OWASP][1])

Security must also be tested from the perspective of an attacker.

We should not only ask:

> "Does the legitimate request work?"

We should also ask:

> "What happens if someone intentionally modifies the request?"

---

# 10. Data Protection

The API must return only the information required by the consumer.

Internal domain objects must not automatically become API responses.

Sensitive information must not be exposed through:

* API responses;
* logs;
* exceptions;
* stack traces;
* configuration;
* documentation;
* debug endpoints.

This is particularly important because exposing entire backend objects through API serialization can unintentionally reveal sensitive information. ([OWASP][2])

---

# 11. Legacy Constraint

The project intentionally avoids automatically adopting modern solutions.

When implementing a feature, the developer should consider:

> "Was this capability available in our chosen stack?"

If not, an alternative compatible with the project constraints must be investigated.

The objective is to understand the evolution of software engineering.

A modern solution may be easier.

That does not automatically make it valid for this project.

---

# 12. Failure Is Part of the Design

The system must not assume that dependencies always work.

External systems may:

* be unavailable;
* respond slowly;
* return invalid data;
* fail during processing;
* return errors;
* receive duplicate requests.

Important operations must therefore consider failure scenarios before implementation.

---

# 13. Idempotency

Operations that may be retried must be evaluated for idempotency.

A network failure must not automatically result in duplicated business operations.

The implementation must consider scenarios such as:

```text
request
    ↓
server processes request
    ↓
response is lost
    ↓
client retries
```

The second request must not blindly duplicate the first operation.

---

# 14. Testing Philosophy

Tests are not written only to increase coverage.

They exist to prove that important behavior is correct.

Priority should be given to:

1. business rules;
2. security rules;
3. failure scenarios;
4. integration boundaries;
5. persistence;
6. API behavior.

A test should preferably answer:

> "What behavior are we protecting?"

rather than simply:

> "What lines of code have been executed?"

---

# 15. Documentation

Important decisions must be documented.

Documentation should explain:

* what the system does;
* why the architecture was chosen;
* important business rules;
* technology constraints;
* relevant security decisions;
* difficult technical decisions;
* how to run the project;
* how to run tests.

If a decision is not obvious, document the reason.

---

# 16. Scope Control

The MVP must have a defined boundary.

Features outside the agreed MVP must not be added simply because they would be interesting.

Examples of features intentionally outside the initial scope:

* mobile application;
* complete frontend;
* payments;
* WhatsApp integration;
* SMS;
* chatbot;
* microservices;
* Kubernetes;
* Kafka;
* artificial intelligence;
* complex dashboards.

New ideas should become future issues rather than silently expanding the MVP.

---

# 17. Definition of Done

An issue is considered complete only when:

* implementation is finished;
* acceptance criteria are satisfied;
* relevant tests exist;
* tests pass;
* code has been reviewed;
* security implications have been considered;
* documentation has been updated when necessary;
* the Pull Request is approved;
* the change has been merged into `main`.

---

# 18. Learning Rule

The project is also a learning exercise.

When facing a technical problem, the preferred process is:

1. Understand the problem.
2. Research the available options.
3. Propose a solution.
4. Explain why the solution was chosen.
5. Implement it.
6. Test it.
7. Review the result.

Solutions should not be copied blindly.

The developer must be able to explain:

> "Why does this work?"

and:

> "Why did we choose this instead of the alternatives?"

---

# 19. Project Principle

The main principle of LegacyDesk is:

> Solve modern problems with the constraints of yesterday, using the engineering discipline required today.

