# API Automation & Testing Portfolio

API testing projects covering test design, manual/exploratory testing in Postman, command-line runs with Newman, and Java-based automation with REST Assured.

[![API tests and reports](https://github.com/vikneshgs23/api-automation-portfolio/actions/workflows/api-tests.yml/badge.svg)](https://github.com/vikneshgs23/api-automation-portfolio/actions/workflows/api-tests.yml)

---

## 📌 Overview

End-to-end API testing of RESTful services, validating functionality, data integrity and workflow state handling.

- Test cases designed per endpoint (positive, negative, auth, end-to-end)
- Postman collection with test scripts and environment variables
- CRUD lifecycles with dynamic request chaining (token extraction, ID injection)
- CLI execution and HTML reporting via Newman
- Java automation framework with REST Assured and TestNG *(in progress)*
- GitHub Actions runs the tests on every push and publishes HTML reports

---

## 🧰 Tech Stack

Postman, Newman, Java, REST Assured, TestNG, Maven

---

## 📂 Repository Structure

```text
api-automation-portfolio/
├── postman/              # Exported Postman collection and environment
├── test-cases/           # API test case sheet with execution results
├── rest-assured/         # Java + REST Assured + TestNG framework
├── .github/workflows/   # CI: runs the tests and publishes reports
└── README.md
```

---

## 🔬 Projects

### Restful-Booker API (8 endpoints)

| Area | Status |
|---|---|
| Test case design | ✅ 34 test cases written |
| Postman execution | ✅ 34 of 34 executed (2 defects found) |
| Newman CLI reports | ✅ 41 requests, 87 assertions, 2 failing (documented defects) |
| REST Assured automation | 🚧 In progress (14 of 34 test cases automated, runs in CI) |
| CI and live reports | ✅ [REST Assured and Newman reports](https://vikneshgs23.github.io/api-automation-portfolio/), updated on every push |

---

## ⚙️ How to Run

### Postman (app)
1. Clone the repo: `git clone https://github.com/vikneshgs23/api-automation-portfolio.git`
2. In Postman, click **Import** and select `postman/Restful-Booker API.postman_collection.json`
3. Import `postman/Restful-Booker.postman_environment.json`
4. Select the **Restful-Booker** environment and run the requests or the Collection Runner
5. Run `CreateToken` first, because it saves the auth token used by later requests

### Newman (CLI)
```bash
npm install -g newman newman-reporter-htmlextra

newman run "postman/Restful-Booker API.postman_collection.json" -e "postman/Restful-Booker.postman_environment.json" -r "cli,htmlextra" --reporter-htmlextra-export "postman/reports/report.html" --reporter-htmlextra-skipSensitiveData
```
The HTML report is saved to `postman/reports/report.html`. On Windows PowerShell, put the whole command on one line.

Two assertions fail on purpose: they document real API defects (see Key Findings).

📊 [View the latest Newman report](https://vikneshgs23.github.io/api-automation-portfolio/postman/reports/report.html)


### REST Assured
```bash
cd rest-assured
mvn clean test
```
📊 [View the latest REST Assured report](https://vikneshgs23.github.io/api-automation-portfolio/rest-assured/emailable-report.html)

---

## 🔎 Key Findings
- `GET /ping` returns **201 Created** instead of 200.
- A failed login (wrong password, wrong username, empty body) returns **200** with `"reason": "Bad credentials"` instead of a 401 status.
- `POST /booking` accepts a non-numeric `totalprice` (e.g. `"abc"`), creates the booking and silently stores the price as `null` instead of returning a validation error.
- `POST /booking` accepts a booking whose checkout date is earlier than its checkin date, with no date validation.
- `PUT` and `DELETE` on a non-existent ID return **405 Method Not Allowed** instead of 404.
- A successful `DELETE` returns **201 Created** instead of 200 or 204.
- Restful-Booker is a shared public API and its data is wiped periodically, so tests should create their own data instead of relying on existing bookings.
- Requests depend on shared state (token, booking ID). In the collection, Auth runs first and a fresh booking is created before the PATCH group, so a Newman run starts from known data.

---

## 👤 Author

- **GitHub:** [@vikneshgs23](https://github.com/vikneshgs23)
- **LinkedIn:** [GS Viknesh](https://www.linkedin.com/in/gs-viknesh)
