# Gradle jokes

## Project Overview
App with multiple flavors that uses multiple libraries and Google Cloud Endpoints. The finished app consists of four modules: a Java library that provides jokes, a Google Cloud Endpoints (GCE) project that serves those jokes, an Android Library containing an activity for displaying jokes, and an Android app that fetches jokes from the GCE module and passes them to the Android Library for display.

## Why this Project
As Android projects grow in complexity, it becomes necessary to customize the behavior of the Gradle build tool, allowing automation of repetitive tasks. Particularly, factoring functionality into libraries and creating product flavors allow for much bigger projects with minimal added complexity.

## Project scheme
![Gradle jokes scheme](https://preview.ibb.co/d08bP8/gradle_jokes_scheme.png "Gradle jokes scheme")

## What Did I Learn / Use?
- Product flavors (Free & Paid)
- Libraries (Java & Android)
- Multi project build
- Gradle App Engine plugin
- Integration test for GCE
