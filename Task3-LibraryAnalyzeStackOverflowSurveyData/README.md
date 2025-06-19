# Stack Overflow Survey Analyzer

A Python library for analyzing Stack Overflow Survey data. This library provides functionality to:

- Display the survey structure (list of questions)
- Search for specific questions/options
- Create respondent subsets based on question+option
- Display distribution of answers for single choice (SC) and multiple choice (MC) questions

## Installation

### Prerequisites

- Python 3.6 or higher
- pip (Python package installer)

### Install from source

1. Clone the repository or download the source code
2. Navigate to the project directory
3. Install the package in development mode:

```bash
pip install -e .
```

This will install the package and its dependencies (pandas and openpyxl).

## Usage

### As a library

```python
from sosurvey_analyzer.analyzer import SOSurveyAnalyzer

# Initialize the analyzer with the default data file
analyzer = SOSurveyAnalyzer()

# Or specify a custom data file
# analyzer = SOSurveyAnalyzer("path/to/survey_data.xlsx")

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

### As a command-line tool

The package installs a command-line tool called `sosurvey-analyzer` that provides access to the library's functionality.

```bash
# Display help
sosurvey-analyzer --help

# Display survey structure
sosurvey-analyzer structure

# Search for questions containing a string
sosurvey-analyzer search-question "experience"

# Search for options in a question
sosurvey-analyzer search-option "DevType" "Data"

# Display distribution of answers for a question
sosurvey-analyzer distribution "DevType"

# Create a subset of respondents and analyze another question
sosurvey-analyzer subset "DevType" "Data scientist" "YearsCodePro"
```

You can also use the module directly with Python:

```bash
python -m sosurvey_analyzer.cli structure
```

## Running Tests

The project uses pytest for testing. To run the tests:

1. Install pytest:

```bash
pip install pytest
```

2. Run the tests:

```bash
pytest
```

Or to run tests with more detailed output:

```bash
pytest -v
```

## Project Structure

- `sosurvey_analyzer/`: Main package directory
  - `__init__.py`: Package initialization
  - `analyzer.py`: Core functionality for analyzing survey data
  - `cli.py`: Command-line interface
- `tests/`: Test directory
  - `test_analyzer.py`: Tests for the analyzer module
  - `test_cli.py`: Tests for the CLI module
- `Data/`: Directory containing the survey data
  - `so_2024_raw.xlsx`: Stack Overflow Survey data file

## License

This project is licensed under the MIT License - see the LICENSE file for details.