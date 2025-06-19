# How to Run the Stack Overflow Survey Analyzer

This guide provides step-by-step instructions for running the Stack Overflow Survey Analyzer project.

## Prerequisites

Before you can run the project, you need to have Python installed on your system.

### Installing Python

1. Download Python from the official website: https://www.python.org/downloads/
2. Run the installer and make sure to check the box that says "Add Python to PATH" during installation
3. Verify the installation by opening a new command prompt and typing:
   ```
   python --version
   ```
   You should see the Python version number if the installation was successful.

## Installing the Project

1. Navigate to the project directory:
   ```
   cd Task3-LibraryAnalyzeStackOverflowSurveyData
   ```

2. Install the project in development mode:
   ```
   python -m pip install -e .
   ```
   This will install the package and its dependencies (pandas and openpyxl).

## Running the Project

### As a Command-Line Tool

After installation, you can run the project using the command-line interface:

```
python -m sosurvey_analyzer.cli --help
```

This will display the available commands. Here are some examples:

1. Display the survey structure (list of questions):
   ```
   python -m sosurvey_analyzer.cli structure
   ```

2. Search for questions containing a string:
   ```
   python -m sosurvey_analyzer.cli search-question "experience"
   ```

3. Search for options in a question:
   ```
   python -m sosurvey_analyzer.cli search-option "DevType" "Data"
   ```

4. Display distribution of answers for a question:
   ```
   python -m sosurvey_analyzer.cli distribution "DevType"
   ```

5. Create a subset of respondents and analyze another question:
   ```
   python -m sosurvey_analyzer.cli subset "DevType" "Data scientist" "YearsCodePro"
   ```

### As a Library

You can also use the project as a library in your Python code:

```python
from sosurvey_analyzer.analyzer import SOSurveyAnalyzer

# Initialize the analyzer with the default data file
analyzer = SOSurveyAnalyzer()

# Display the survey structure (list of questions)
analyzer.display_survey_structure()

# Search for questions containing a string
questions = analyzer.search_question("experience")
print(questions)

# Search for options in a question
options = analyzer.search_option("DevType", "Data")
print(options)

# Create a subset of respondents based on question and option
subset = analyzer.create_subset("DevType", "Data scientist")

# Display distribution of answers for a question
analyzer.display_answer_distribution("YearsCodePro")
```

## Running Tests

To run the tests for the project:

1. Install pytest:
   ```
   python -m pip install pytest
   ```

2. Run the tests:
   ```
   python -m pytest
   ```

   Or for more detailed output:
   ```
   python -m pytest -v
   ```

## Troubleshooting

If you encounter any issues:

1. Make sure Python is installed and added to your PATH
2. Make sure you're in the correct directory (Task3-LibraryAnalyzeStackOverflowSurveyData)
3. Try running the commands with `python -m` prefix (e.g., `python -m pip` instead of just `pip`)
4. Check that the dependencies (pandas and openpyxl) are installed