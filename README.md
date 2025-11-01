# Resume Optimizer

**Enhancing Resume Success Rates through ATS Optimization**
*by Pablo De Biase*

A practical toolset that compares a resume to a job description, scores compatibility against Applicant Tracking Systems (ATS), and helps job-seekers optimize their resumes for better hiring outcomes.

---

## Table of contents

* [What it does](#what-it-does)
* [Project overview](#project-overview)
* [Features](#features)
* [Tech stack](#tech-stack)
* [System architecture (high level)](#system-architecture-high-level)
* [Installation & quickstart (example)](#installation--quickstart-example)
* [Usage](#usage)
* [Design decisions & challenges](#design-decisions--challenges)
* [Solutions implemented](#solutions-implemented)
* [Lessons learned](#lessons-learned)
* [Future improvements](#future-improvements)
* [Credits](#credits)
* [License](#license)

---

## What it does

Resume Optimizer compares a candidate's resume with a job posting and computes a compatibility score, exposing recommendations to improve ATS pass rates and hiring prospects.

---

## Project overview

Purpose: help job seekers tailor resumes to specific job postings by analyzing content, keyword fit, and formatting issues that cause ATS rejections.

Primary flows:

1. User uploads resume + job description.
2. System scores compatibility and returns suggestions.
3. Results are stored for review and comparison.

---

## Features

* User account creation & management (registered users).
* Guest access for quick evaluations (limited session-based features).
* Storage of analysis results in a database for later review.
* Review and comparison of previous results via user dashboard.
* Real-time logging console using WebSockets.
* Integration between Java backend and a Python resume-matching tool.

---

## Tech stack

**Backend**

* Java Spring Boot

**Frontend**

* JSP (JavaServer Pages) for server-side rendering
* HTML, CSS, JavaScript for UI

**Database**

* MySQL

**External integration**

* Python `Resume Matcher` (Streamlit) for resume analysis
* RESTful APIs for communication between services

**Realtime**

* WebSockets for live logging / processing updates

---

## System architecture (high level)

* UI (JSP) → Spring Boot backend → REST calls → Python resume analysis tool
* Analysis results returned to backend, persisted in MySQL, displayed to user
* WebSocket channel streams live logs to client during processing

---

## Installation & quickstart (example)

This README gives a pragmatic skeleton to get the system running locally. Adjust paths, ports, and credentials to your environment.

1. **Database (MySQL)**

   * Start a MySQL server and create the schema/user required by the app (update `application.properties` accordingly).

2. **Backend (Spring Boot)**

   * Build and run:

     ```bash
     mvn clean package
     mvn spring-boot:run
     ```
   * Or run the generated jar:

     ```bash
     java -jar target/resume-optimizer.jar
     ```
   * Ensure `application.properties` (or environment vars) point to your MySQL instance and set any required tokens/keys.

3. **Frontend**

   * JSPs are served by the Spring Boot app — no separate build step unless you have client assets (CSS/JS) that require tooling.

4. **Python Resume Matcher (Streamlit)**

   * Create a virtualenv, install requirements, and run:

     ```bash
     python -m venv .venv
     source .venv/bin/activate
     pip install -r requirements.txt
     streamlit run resume_matcher/app.py
     ```
   * Configure the backend to call the Streamlit service or the Python service’s API.

5. **WebSocket logging**

   * Make sure the WebSocket endpoint is enabled in the Spring Boot configuration and the frontend points to it for live logs.

6. **Optional: Docker / docker-compose**

   * If you prefer containers, create `Dockerfile` for backend and `docker-compose.yml` to orchestrate the app + MySQL + python service. Bind mounts help local dev.

> These are example commands and conventions based on standard patterns in Spring Boot + Streamlit projects. Adjust to your codebase (paths, filenames, and ports).

---

## Usage

* Register and log in (or use Guest evaluation).
* Upload your resume and a target job description.
* Trigger analysis → receive compatibility score + suggestions.
* View and compare past analyses in your account dashboard.

---

## Design decisions & challenges

* **Token management:** needed secure session handling (CSRF, authentication tokens), renewal and validation complexities.
* **WebSocket logging console:** required stable, asynchronous WebSocket connections to stream logs during long-running analyses.
* **Guest user flow:** maintain useful functionality without creating a security liability or requiring registration.
* **UI/UX:** balancing minimal, modern design with clarity for non-technical users.

---

## Solutions implemented

* CSRF token support integrated with Spring Security for request validation.
* Spring Boot WebSocket support used for asynchronous log streaming.
* Session-based guest profiles with limited access.
* Minimalist UI principles adopted for clearer UX.
* Asynchronous processing and persistence of results for later retrieval.

---

## Lessons learned

* Improved understanding of Spring Boot + JSP integration and how to safely bridge Java and Python services.
* Practical experience solving real-time data display via WebSockets.
* Security complexities: token handling and guest session safety.
* UI tradeoffs: clarity and engagement often trump flashy visuals.

---

## Future improvements

* Move Python resume-matching logic into Java (or containerize the Python service) for tighter integration and simpler deployment.
* Add personalized recommendations and contextual tips based on role/industry.
* Performance optimizations and horizontal scaling for larger user base.
* Notification system, analytics dashboard, and richer UX redesign.

---

## Credits

* Author: Pablo De Biase
* Thanks to ComIT for support and inspiration.

