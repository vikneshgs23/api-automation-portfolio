# API Automation & Testing Portfolio

API testing projects covering test design, manual/exploratory testing in Postman, command-line runs with Newman, and Java-based automation with REST Assured.

---

## 📌 Overview

End-to-end API testing of RESTful services, validating functionality, data integrity and workflow state handling.

- Test cases designed per endpoint (positive, negative, auth, end-to-end)
- CRUD lifecycles with dynamic request chaining (token extraction, ID injection)
- Response and JSON schema assertions
- CLI execution and HTML reporting via Newman
- Java automation framework with REST Assured and TestNG *(in progress)*

---

## 🧰 Tech Stack

Postman, Newman, Java, REST Assured, TestNG, Maven

---

## 📂 Repository Structure

```text
api-automation-portfolio/
├── postman/
│   ├── collections/      # Exported Postman collections
│   ├── environments/     # Environment variable files
│   └── reports/          # Newman HTML/CLI reports
├── rest-assured/         # Java + REST Assured + TestNG framework
├── test-cases/           # API test case documentation
└── README.md
```

---

## 🔬 Projects

| API under test | Postman | REST Assured |
|---|---|---|
| Restful-Booker | ✅ Collection + Newman reports | 🚧 In progress |

---

## ⚙️ How to Run

### Postman (app)
1. Clone the repo: `git clone https://github.com/vikneshgs23/<repo-name>.git`
2. In Postman, click **Import** and select the file from `postman/collections/`
3. Import the matching environment from `postman/environments/`
4. Run it with the Collection Runner

### Newman (CLI)
```bash
npm install -g newman newman-reporter-htmlextra

newman run postman/collections/<collection-name>.json \
  -e postman/environments/<environment-name>.json \
  -r cli,htmlextra \
  --reporter-htmlextra-export postman/reports/report.html
```

### REST Assured *(in progress)*
```bash
cd rest-assured
mvn clean test
```

---

## 🔎 Key Findings
*(Add API quirks or bugs you discover, e.g. unexpected status codes.)*

---

## 👤 Author

- **GitHub:** [@vikneshgs23](https://github.com/vikneshgs23)
- **LinkedIn:** [GS Viknesh](https://www.linkedin.com/in/gs-viknesh)
