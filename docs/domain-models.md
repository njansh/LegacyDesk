Crie o arquivo **`docs/domain-models.md`** dentro da pasta `docs/` com o conteúdo abaixo para atender a todos os critérios de aceitação da **Issue #1**.

```markdown
# Domain Models & Core Relationships

## 1. Domain Entities & Value Objects

### 1.1 User
Represents internal company employees interacting with the service desk.
* **Fields:**
  * `id` (Long, PK)
  * `name` (String, required, max 100)
  * `email` (String, required, unique, validated)
  * `password` (String, required, hashed via BCrypt)
  * `role` (Role enum: `ATTENDANT`, `TECHNICIAN`, `SUPERVISOR`, `ADMIN`)
  * `active` (Boolean, default `true`)
  * `createdAt` (Instant, immutable)
  * `updatedAt` (Instant)

### 1.2 Customer
Represents external clients requesting services.
* **Fields:**
  * `id` (Long, PK)
  * `name` (String, required, max 120)
  * `document` (String, required, unique, tax/company ID)
  * `email` (String, required)
  * `phone` (String, optional)
  * `active` (Boolean, default `true`, soft-delete support)
  * `createdAt` (Instant, immutable)
  * `updatedAt` (Instant)

### 1.3 Request
Represents a customer support/service ticket.
* **Fields:**
  * `id` (Long, PK)
  * `customer` (Customer reference, required)
  * `assignedTechnician` (User reference, optional, must have `TECHNICIAN` role)
  * `description` (String, required, max 2000)
  * `priority` (Priority enum: `LOW`, `NORMAL`, `HIGH`, `URGENT`)
  * `status` (RequestStatus enum: `OPEN`, `IN_PROGRESS`, `WAITING_CUSTOMER`, `RESOLVED`, `CANCELLED`)
  * `createdAt` (Instant, immutable)
  * `updatedAt` (Instant)
  * `resolvedAt` (Instant, nullable)

### 1.4 Comment
Represents chronological communication and technical notes within a request.
* **Fields:**
  * `id` (Long, PK)
  * `request` (Request reference, required)
  * `author` (User reference, required)
  * `content` (String, required, max 1000)
  * `createdAt` (Instant, immutable)

### 1.5 RequestHistory
Immutable timeline recording all state changes and key transitions.
* **Fields:**
  * `id` (Long, PK)
  * `requestId` (Long, required)
  * `operatorId` (Long, required)
  * `eventType` (EventType enum: `CREATED`, `ASSIGNED`, `STATUS_CHANGED`, `PRIORITY_CHANGED`, `RESOLVED`, `CANCELLED`)
  * `previousValue` (String, nullable)
  * `newValue` (String, nullable)
  * `occurredAt` (Instant, immutable)

### 1.6 AuditEvent
System-level security audit trail recording critical mutations.
* **Fields:**
  * `id` (Long, PK)
  * `operatorId` (Long, required)
  * `action` (String, required)
  * `resourceType` (String, required)
  * `resourceId` (String, required)
  * `occurredAt` (Instant, immutable)
  * `details` (String/JSON, sanitized, no credentials/PII)

---

## 2. Enums & Invariants

```text
Priority: LOW | NORMAL | HIGH | URGENT

RequestStatus: 
  OPEN -> IN_PROGRESS -> WAITING_CUSTOMER -> IN_PROGRESS -> RESOLVED
       \              \                                /
        \---------------> CANCELLED <-----------------/

Roles:
  ATTENDANT   -> Creates requests, interacts with customers.
  TECHNICIAN  -> Works on assigned requests, adds internal comments.
  SUPERVISOR  -> Assigns requests to technicians, updates priorities.
  ADMIN       -> Manages employee accounts, access policies, and audit logs.

```

---

## 3. Relationships & Multiplicities

* **Customer 1 : N Request** (A customer can open multiple requests).
* **User (Technician) 1 : N Request** (A technician handles multiple assigned requests).
* **Request 1 : N Comment** (A request aggregates multiple immutable comments).
* **Request 1 : N RequestHistory** (A request has a linear, immutable history log).

---

## 4. Core Business Invariants

1. **Immutabilit    y:** Comments, history entries, and audit logs cannot be updated or deleted once created.
2. **Soft Deletion:** Neither Customers nor Users are physically deleted from the database (`active = false`).
3. **Role Validation:** Only active users with the `TECHNICIAN` role can be assigned to a `Request`.
4. **Transition Guard:** Status transitions must strictly follow the state diagram; illegal jumps trigger d