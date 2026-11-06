# API Automation & Testing Portfolio

API testing projects covering test design, manual/exploratory testing in Postman, command-line runs with Newman, and Java-based automation with REST Assured.

---

## 📌 Overview

End-to-end API testing of RESTful services, validating functionality, data integrity and workflow state handling.

- Test cases designed per endpoint (positive, negative, auth, end-to-end)
- Postman collection with test scripts and environment variables
- CRUD lifecycles with dynamic request chaining (token extraction, ID injection) *(in progress)*
- CLI execution and HTML reporting via Newman *(planned)*
- Java automation framework with REST Assured and TestNG *(planned)*

---

## 🧰 Tech Stack

Postman, Newman, Java, REST Assured, TestNG, Maven

---

## 📂 Repository Structure

```text
api-automation-portfolio/
├── postman/              # Exported Postman collection and environment
├── test-cases/           # API test case sheet with execution results
├── rest-assured/         # Java + REST Assured + TestNG framework (coming soon)
└── README.md
```

---

## 🔬 Projects

### Restful-Booker API (8 endpoints)

| Area | Status |
|---|---|
| Test case design | ✅ 34 test cases written |
| Postman execution | 🚧 21 of 34 executed (Ping, Auth, Booking read and create) |
| Newman CLI reports | ⏳ Planned |
| REST Assured automation | ⏳ Planned |

---

## ⚙️ How to Run

### Postman (app)
1. Clone the repo: `git clone https://github.com/vikneshgs23/api-automation-portfolio.git`
2. In Postman, click **Import** and select `postman/Restful-Booker API.postman_collection.json`
3. Import `postman/Restful-Booker.postman_environment.json`
4. Select the **Restful-Booker** environment and run the requests or the Collection Runner
5. Run `CreateToken` first, because it saves the auth token used by later requests

### Newman (CLI) *(planned)*
```bash
npm install -g newman newman-reporter-htmlextra

newman run "postman/Restful-Booker API.postman_collection.json" \
  -e postman/Restful-Booker.postman_environment.json \
  -r cli,htmlextra
```

### REST Assured *(planned)*
```bash
cd rest-assured
mvn clean test
```

---

## 🔎 Key Findings

- `GET /ping` returns **201 Created** instead of 200.
- A failed login (wrong password, wrong username, empty body) returns **200** with `"reason": "Bad credentials"` instead of a 401 status.
- Restful-Booker is a shared public API and its data is wiped periodically, so a previously created booking can disappear. Tests should create their own data instead of relying on existing bookings.
- `POST /booking` accepts a non-numeric `totalprice` (e.g. `"abc"`), creates the booking and silently stores the price as `null` instead of returning a validation error.
- `POST /booking` accepts a booking whose checkout date is earlier than its checkin date, with no date validation.
- `PUT /booking/{id}` on a non-existent ID returns **405 Method Not Allowed** instead of 404.

---

## 👤 Author

- **GitHub:** [@vikneshgs23](https://github.com/vikneshgs23)
- **LinkedIn:** [GS Viknesh](https://www.linkedin.com/in/gs-viknesh)