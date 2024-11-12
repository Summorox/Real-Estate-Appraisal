# Real State Appraisal
## Overview
This repository contains a group project developed as part of a master's program in Artificial Intelligence at ISEP, completed in 2022. The project focuses on implementing expert systems and rule-based methodologies to assess real estate values based on their characteristics, providing a foundation for understanding and automating appraisal processes in a structured environment.

## Features

- **Expert System Logic**: Utilizes rules and logical structures to evaluate property characteristics.
- **Adaptable Rules**: The values within the rule set can be easily adjusted to account for regional variations in property prices.
- **Extensible Property Attributes**: New property attributes can be added to the database with only minor adjustments, allowing for the addition of attribute-price variance pairs as needed.
- **Dual Rule-Based Systems**: The rule-based system has been implemented in two distinct ways: one using Prolog and the other using Drools. Both implementations can be connected as APIs to a frontend, enabling a flexible, rule-driven appraisal interface.
- **User Interface**: A simple web interface allows users to input property details and receive appraisal results.

#  Installation 

Navigate to the project directory and install dependencies:
```bash
mvn clean install
```

# Run the Application
1. Compile and start the application by navigating to the target directory:
```bash
cd target
```
2. Run the app with the command:
```bash
 java -jar evaluation-1.0.jar
```
3. Access the application by opening a web browser and navigating to `http://localhost:8080/home`.
