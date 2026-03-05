# Amazon Locker 

### Core requirements - 
1. Locker should have multiple compartments.
2. Each compartment would have different size. (Small, medium, large) 
3. Packages are stored in compartment according to the package and compartment size. 
4. when paced the package successfully in compartment(if vacant and valid size)
    a new access token is generated and send to receiver. 
5. Access token will expire after 7 days and then if package in locker, we have to manually remove it. 
6. Receiver enters the access key and successfully retrieve the packege. 
   - access code dropped 
   - if access code validation failed, return the error. No lockout for now. 

### Entities
1. Locker
2. Compartment
3. Package
4. AccessToken

### Relationship
1. `1.Locker` -> `n.Compartment`
2. `1.Compartment` -> `0..1.AccessToken` (1 at a time)
3. `1.AccessToken` -> `1.Package`

### Class Diagram 

```text
class Locker
    - compartments: List<Compartment>
    - activeTokens: Map<String, AccessToken>
    - allocationStrategy: AllocationStrategy

    + Locker(compartments, allocationStrategy)
    + depositPackage(pkg: Package) -> AccessToken | error
    + pickup(tokenCode) -> void | error
    + cleanupExpiredTokens() -> void

class Package:
    - orderId: String
    - size: Size
    - receiverContact: String

    + Package(orderId, size, receiverContact)

class AccessToken:
    - code: String (6-digit OTP)
    - expiration: DateTime
    - compartment: Compartment
    - pkg: Package

    + AccessToken(code, expiration, compartment, pkg)
    + isExpired() -> boolean
    + getCompartment() -> Compartment
    + getCode() -> String

class Compartment:
    - id: String
    - size: Size
    - occupied: boolean

    + Compartment(id, size)
    + isOccupied() -> boolean
    + markOccupied() -> void
    + markFree() -> void

enum Size:
    SMALL
    MEDIUM
    LARGE
```

### Design Pattern - Strategy (Allocation)

```text
interface AllocationStrategy:
    + allocate(compartments: List<Compartment>, size: Size) -> Compartment | null

class BestFitStrategy implements AllocationStrategy:
    // SMALL pkg -> try SMALL, then MEDIUM, then LARGE

class ExactFitStrategy implements AllocationStrategy:
    // only exact size match
```

### Flow

```text
Deposit:
  1. deliveryAgent calls locker.depositPackage(package)
  2. allocationStrategy.allocate(compartments, package.size)
  3. no compartment found -> NO_COMPARTMENT_AVAILABLE
  4. mark compartment occupied, generate 6-digit OTP (7-day expiry)
  5. store token in activeTokens, notify receiver
  6. return AccessToken

Pickup:
  1. receiver calls locker.pickup(tokenCode)
  2. token not found -> INVALID_TOKEN
  3. token expired -> TOKEN_EXPIRED
  4. mark compartment free, remove token

Cleanup:
  1. scheduled call to locker.cleanupExpiredTokens()
  2. iterate activeTokens, free expired compartments, remove tokens
```
