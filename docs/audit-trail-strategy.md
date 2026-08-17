# Audit Trail Event Model & System Strategy

## 1. Overview & Purpose

LegacyDesk requires strict traceability and compliance logging for all state-mutating operations. The audit trail system captures security events, authentication attempts, privilege elevations, and domain mutations while guaranteeing that:
1. Audit records are strictly append-only and immutable.
2. Sensitive credentials and PII (passwords, raw tokens) are never persisted in audit payloads.
3. Audit failures do not silently compromise critical business operations without dedicated telemetry.

---

## 2. Event Models: Audit Trail vs. Request History

To maintain a clean separation of concerns, the system distinguishes between Domain Request History and Security/System Audit Events:

| Dimension | Request History (RequestHistory) | Audit Event (AuditEvent) |
| :--- | :--- | :--- |
| Scope | Operational ticket lifecycle | Cross-cutting system & security mutations |
| Audience | Attendants, Technicians, Customers | Supervisors, Admins, Compliance Auditors |
| Granularity | Ticket status, assignments, priority shifts | User logins, role updates, ticket deletions, data export |
| Storage | Relational table request_histories | Relational table audit_events |

---

## 3. Audit Event Model Specification

### 3.1 Entity Structure (AuditEvent)
* id (BIGINT, Primary Key, Auto-increment)
* operatorId (BIGINT, Nullable for system-triggered events)
* action (VARCHAR(100), Required - e.g., AUTH_LOGIN_SUCCESS, USER_ROLE_CHANGED, REQUEST_STATUS_UPDATED)
* resourceType (VARCHAR(60), Required - e.g., USER, REQUEST, CUSTOMER, AUTH)
* resourceId (VARCHAR(100), Required - string identifier of the impacted aggregate)
* occurredAt (TIMESTAMP WITH TIME ZONE, Required, Immutable)
* ipAddress (VARCHAR(45), Nullable, Client IPv4/IPv6)
* details (TEXT, Nullable, Sanitized JSON payload with structured change delta)

---

## 4. Key Event Categories

AUTH EVENTS:
  - AUTH_LOGIN_SUCCESS
  - AUTH_LOGIN_FAILED
  - AUTH_PASSWORD_CHANGED

USER MUTATIONS:
  - USER_CREATED
  - USER_STATUS_UPDATED (Active/Inactive)
  - USER_ROLE_PROMOTED

REQUEST WORKFLOW:
  - REQUEST_CREATED
  - REQUEST_ASSIGNED
  - REQUEST_STATUS_TRANSITION
  - REQUEST_PRIORITY_UPDATED

CUSTOMER MUTATIONS:
  - CUSTOMER_CREATED
  - CUSTOMER_DEACTIVATED

---

## 5. Security & Sanitization Invariants

1. Payload Masking: Any fields matching password, token, secret, or authorization must be stripped or replaced with [REDACTED] before persistence.
2. Immutability Enforcement: The audit_events table will not have UPDATE or DELETE permissions exposed in application repositories.
3. Decoupled Dispatch: Domain events trigger audit records via Spring Application Events (ApplicationEventPublisher) to prevent direct coupling between domain use cases and audit storage.
