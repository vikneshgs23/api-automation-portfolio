# API Automation & Testing Portfolio

A collection of API test automation suites, exploratory collections, and contract validation workflows built using Postman and Newman.

---

## 📌 Overview

This repository houses end-to-end API testing collections designed to validate RESTful APIs for functionality, data integrity, and workflow state handling. 

The collections focus on:
- End-to-end CRUD lifecycles.
- Dynamic request chaining (token extraction, dynamic ID injection).
- Response validation and JSON schema assertions.
- Command-line test execution and reporting via Newman.

---

## 📂 Repository Structure

```text
api-automation-portfolio/
├── collections/             # Exported Postman collection JSON files
├── environments/            # Postman environment variable files
├── reports/                 # Test execution reports (Newman CLI/HTML)
└── README.md
🛠 Prerequisites
To run these tests locally or headlessly via the command line, install:

Postman App

Node.js (v16+)

Newman (Postman CLI Runner):

Bash
npm install -g newman
npm install -g newman-reporter-htmlextra

⚙️ How to Run
Option 1: Import into Postman App
Clone this repository:

Bash
git clone [https://github.com/](https://github.com/)<your-username>/<your-repo-name>.git
Open Postman.

Click Import in the top-left corner.

Drag and drop the target .json file from the /collections directory.

Select the collection and run it via Collection Runner.

Option 2: Run via CLI (Newman)
Bash
newman run collections/<collection-name>.json \
  -e environments/<environment-name>.json \
  -r cli,htmlextra \
  --reporter-htmlextra-export reports/report.html

## 👤 Author

- **GitHub:** [@vikneshgs23](https://github.com/vikneshgs23)
- **LinkedIn:** [GS Viknesh](https://www.linkedin.com/in/gs-viknesh)
