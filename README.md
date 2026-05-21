# Retail E-Commerce Checkout Subsystem - Verification & Testing Project

## 1. Project Overview
This project focuses on the systematic verification, refactoring, and automated testing of a core retail e-commerce checkout subsystem. The primary objective was to audit, identify, and resolve fifteen intentional functional, structural, and architectural defects embedded across multiple core and auxiliary system classes. 

Following source code repair, a highly detailed automated test suite was engineered under the `edu.cczu` package using the JUnit 5 framework and the Mockito mocking engine to completely isolate units under test. Detailed statement execution and conditional branch coverage metrics were monitored, captured, and generated using the JaCoCo test-coverage platform.

---

## 2. Infrastructure & Build Configuration (`pom.xml`)
The software pipeline and dependency lifecycle are managed using the Apache Maven automation model. The project object configuration structure is optimized to ensure reliable compilation and runtime compliance within a modern Java 26 environment.

### Core Maven Properties
* **Java Version Compliance:** `maven.compiler.source` and `maven.compiler.target` properties are explicitly defined as `26` to leverage modern compiler standards.
* **Character Encoding Specification:** `project.build.sourceEncoding` is set to `UTF-8` to enforce cross-platform string and log encoding uniformity.

### Framework Dependencies
* **JUnit Jupiter Engine (JUnit 5):** Embedded within the `test` scope to supply modern verification annotations (`@Test`, `@BeforeEach`), lifecycle controllers, and assertion engines.
* **Mockito Core:** Integrated to create isolated, simulated runtime instances of complex dependencies.
* **Mockito JUnit Jupiter Extension:** Configured to automatically initialize mocks via `@ExtendWith(MockitoExtension.class)` and seamlessly bind runtime stubs to the JUnit lifecycle.

### Build Plugins & JaCoCo Integration
To automate testing cycles and collect runtime performance telemetry, the build lifecycle is configured with:
* **Maven Surefire Plugin:** Tasked with executing unit tests, managing execution isolation, and logging test run success streams.
* **JaCoCo Maven Plugin (Version 0.8.14):** Upgraded specifically to support Java 26 bytecode analysis. It is bound to two execution goals:
  1. `prepare-agent`: Hooks into the target JVM execution thread prior to test execution to pass properties and track active branch paths.
  2. `report`: Executes during the Maven package/test phase to process recorded branch arrays into human-readable visual HTML tracking modules under `target/site/jacoco/`.

---

## 3. Subsystem Architecture & Class Breakdown

The subsystem is architecturally split into **Core Logic Components** (which drive business policies and modifications) and **Auxiliary Data Classes** (which handle data representation, transfer payloads, and external contracts).

### 3.1. Core Logic Components
* **`Product`**: Acts as the basic domain entity representing items available in the store. It encapsulates values for unique identification, name tracking, pricing models, stock counts, and categories.
* **`ShoppingCart`**: A stateful collection manager tasked with storing selected items, computing cumulative financial figures, validating stock levels, and handling item management operations.
* **`CheckoutService`**: The orchestrator of the checkout pipeline. It coordinates with the `ShoppingCart` to verify inventory, calculates promotional discounts, builds payment requests, invokes external payment gateways, and generates final order confirmations.
* **`PaymentService`**: Manages the payment verification workflow by mapping internal request payloads into external gateway calls, processing status returns, handling errors, and logging unique transaction markers.

### 3.2. Auxiliary Data Classes & Structs
Auxiliary classes serve as the backbone of data integrity. They do not compute business logic decisions; instead, they define data structures, transfer protocols, data capsules, and communication interfaces.
* **`CartItem` (Newly Engineered Struct)**: A custom data wrapper designed and introduced during the refactoring process to fix the broken `ShoppingCart` primitive map architecture. It binds a real `Product` object reference directly to its associated cart quantity value, enabling dynamic calculation of prices (`product.getPrice() * quantity`) and accurate runtime stock checks.
* **`Order`**: An auxiliary data capsule representing a finalized transaction record. It safely encapsulates information regarding unique order IDs, initial calculations, discount amounts, final transaction totals, and current order status strings (`PAID` or `FAILED`).
* **`PaymentRequest`**: An auxiliary Data Transfer Object (DTO) used to securely bundle billing fields (order ID, final amount, payment method) into a single, standardized container before passing it to the payment processors.
* **`PaymentResponse`**: An auxiliary data transfer payload returned by payment processing engines. It houses primitive status fields indicating absolute payment execution outcomes (`isSuccess`), unique transaction registration identification numbers assigned by banking networks, and clear diagnostic text messages describing processing failures.
* **`ThirdPartyPaymentInterface`**: An auxiliary contract mapping interface defining the connection rules for interacting with external banking networks and remote payment processing gateways, allowing dependencies to be cleanly mocked during test runs.

---

## 4. Defect Audit Log & Refactoring Summary

A total of 15 intentional defects were audited and refactored across the subsystem components to align with business constraints:

### `Product` Class Refactoring
* **Defect 1 (Price Constraint Flaw):** Refactored from `if (price < 0)` to `if (price <= 0)` to enforce that all catalog items maintain a positive commercial value greater than 0 USD.
* **Defect 2 (Negative Stock Boundary):** Changed the boundary check from `if (stock < -1)` to `if (stock < 0)` to strictly prevent illegal negative inventory states.
* **Defect 3 (Incomplete Category Null Check):** Added defensive conditions to reject objects if either the category or name attributes are null or empty strings.
* **Defect 4 (Unvalidated Inventory Subtraction):** Refactored the `updateStock` method to check that deduction arguments are strictly greater than 0 and do not exceed current stock levels, preventing negative stock or illegal modifications.

### `ShoppingCart` Class Refactoring
* **Defect 5 (Missing Quantity Input Rule):** Refactored the cart insertion flow to instantly drop execution and return `false` if an entered quantity parameter is 0 or lower.
* **Defect 6 (Reversed Stock Condition):** Corrected a reversed conditional operator that initially rejected valid purchases and accepted out-of-stock orders, changing it to return `false` if stock levels drop below the requested amount.
* **Defect 7 (Omitted Stock Modification Event):** Programmed the cart to trigger `product.updateStock(quantity)` automatically upon a successful cart addition to keep catalog data synchronized.
* **Defect 8 (Incorrect Non-Existent Product Return State):** Refactored the item removal flow to return `true` instead of `false` when an item ID is already absent from the cart, satisfying the desired post-condition.
* **Defects 9 & 10 (Collections Structural Architecture Defect):** The provided cart class stored elements inside a primitive `Map<Long, Integer>` tracking only product IDs and numerical quantities, forcing total price and inventory validation methods to return hardcoded dummy results (`0.0` and `true`). To resolve this, the internal collection architecture was updated to a `Map<Long, CartItem>`, granting the class access to real object attributes at runtime.

### `CheckoutService` Class Refactoring
* **Defect 11 (Inverted Discount Matrix Rules):** Corrected an inverted logical matrix where orders under 50 USD received discounts while large orders over 100 USD received zero reductions. Changed to `if (total >= 100.0)` to safely apply a 10 percent discount.
* **Defect 12 (Ignored Payment Gateway Results):** Refactored the checkout routine to explicitly evaluate `response.isSuccess()` rather than ignoring gateway returns and hardcoding a successful transaction.
* **Defect 13 (Unbounded Negative Order Limits):** Added safety constraints to ensure that deep discount rules cannot drive final payable order balances below 0 USD.
* **Defects 14 & 15 (Implicit Service Dependencies Omissions):** Refactored service dependencies to interface with the modernized `CartItem` data map layer, enabling real-time stock balances and accurate total cost calculation evaluations.

### `PaymentService` Class Refactoring
* **Defect 16 (Missing Null Pointer Safeguards):** Added instant validation blocks to reject payment requests immediately if the payment method string parameter evaluates to null.
* **Defect 17 (False Success on Missing Transaction IDs):** Refactored the transaction logging mechanism to capture null returns from third-party banking gateways and route them as hard failures instead of false successes.
* **Defect 18 (Swallowed Exception Details):** Refactored the try-catch error safety net to explicitly append `e.getMessage()` into the failed payment response, preserving critical debugging diagnostics.

---

## 5. Automated Test Suite Engineering (3A Pattern)

All unit and integration tests are isolated under the `src/test/java/edu/cczu/` directory and rigorously implement the structured **Arrange, Act, and Assert (3A) Pattern**.

### Test Modules Implemented
1. **`ProductTest.java`**: Focuses on validating state rules. Tests proper item initialization, price thresholds, negative inventory barriers, and validation rules for categories.
2. **`ShoppingCartTest.java`**: Verifies internal state transformations of the new `CartItem` data layer. Evaluates automatic inventory deductions, item additions, lookups, and total cart value calculations.
3. **`CheckoutServiceTest.java`**: Isolates the checkout orchestration engine by utilizing Mockito to mock out components for `ShoppingCart` and `PaymentService`. Verifies successful checkouts, appropriate discount calculations, cart wiping post-purchase, and rapid pipeline failure drops if out-of-stock items are detected.
4. **`PaymentServiceTest.java`**: Simulates integration behaviors by mocking out the `ThirdPartyPaymentInterface` contract via argument matchers. Validates responses for clean transaction tokens, null checks, and tests graceful exception catch routines using Mockito's `thenThrow` stubs.

### Technical Debugging Obstacles Overcome
* **Compilation Bug:** The original test classes included a non-standard custom test assertion named `falseToggle`, resulting in early compiler failures. This was repaired across all test files by substituting standard JUnit 5 `assertFalse` validation syntax.
* **Strict Mockito Stubbing Exception:** During initial integration checks, an explicit stubbing rule tracking a transaction ID (`when(simulatedResponse.getTransactionId()).thenReturn(999L);`) was declared inside `CheckoutServiceTest.java`. Since the underlying service logic only evaluates `isSuccess()` and never invokes `getTransactionId()`, Mockito threw an `UnnecessaryStubbingException` to preserve test suite cleanliness. Removing this unused stub resolved the issue and allowed a successful test pass.

---

## 6. How to Build, Run, and Verify Locally

### Prerequisites
* Java Development Kit (JDK) 26 configured in your system path environment variables.
* Apache Maven installed and authenticated.

### Executing the Lifecycle Commands
To clean the project, compile the updated classes, run the unit test suite, and generate code coverage reports, run the following command in your root directory terminal:

```bash
mvn clean test