# scala-workshop
Scala experiment

# Preparation
 * Install SBT
   ```
   // install sbt
   $ brew install sbt
   // Check the installation
   $ sbt sbtVersion
   ``` 
 * Install Intellij IDE
   * Install Scala Plugins
   * Install PlantUML Integration Plugins   
 * Pre-Load dependencies (Console)
   ```
   // in your project dir run this command
   $ sbt test
   ```
   
 * Pre-Load dependencies (IntellijIDE)
   * Import Projects
     * Open Intellij
     * Import Project from Existing Source
     * Select your cloned project directory
     * Choose `Import project from external model` and then select `sbt`
     * Press `Next`
     * Check `Download/Library sources` and `Download/sbt sources`
     * Check `Use sbt shell/for imports` and `Use sbt shell/for builds`
     * Select `Project JDK` as java version 1.8
     * Click `Finished`
     * Choose the target window for opening source file (Optional)
   * Setup SDK
     * Open any `.scala` file in `src` folder, on the top right of text editor may appear `Setup Scala SDK` or `Setup Java SDK`
     * Click at `Setup Scala SDK` -> Click button `Create` -> Select version `2.12.3` -> Click `OK` -> Click `OK`
     * Click at `Setup Java SDK` -> Choose `1.8` -> Click `OK`

## Scala Basics
* Introduction
* Scala Installation
  * SBT
  * Intellij IDE
  * Hello World
* Control Statements
  * If-Else
  * Pattern Matching
* Immutable Collections
  * Lists
  * Arrays
  * Tuples
* Future, Options, Eithers
* Trait
* Functions
  * Higher-Order Function
  * Composition
  * Currying
* Class & Object
  * Defining Class
  * Companion Object
* Sequences
* For-Comprehension
* Implicits
* Generic Types
* Algebraic data type (ADT)
* Type Level Programming with Cats
  * Semigroup
  * Monoid
  * Monad
* Type safety with Effortless Domain Driven Design (DDD)

## Testing
* Scalatest
* Mockito

## Create Microservice with Scala
* Architecture
* Akka Http
* Typesafe Configuration
* Mongo Scala Driver
* Docker