# Stack Overflow Survey Analyzer - Quick Start Guide

## Overview

The Stack Overflow Survey Analyzer is a Python library that allows you to analyze Stack Overflow Survey data. It provides functionality to:

- Display the survey structure (list of questions)
- Search for specific questions/options
- Create respondent subsets based on question+option
- Display distribution of answers for single choice (SC) and multiple choice (MC) questions

## Quick Start

1. **Install Python** (if not already installed)
   - Download from: https://www.python.org/downloads/
   - Make sure to check "Add Python to PATH" during installation

2. **Install the project**
   ```
   cd Task3-LibraryAnalyzeStackOverflowSurveyData
   python -m pip install -e .
   ```

3. **Run the project**
   ```
   python -m sosurvey_analyzer.cli structure
   ```

## Documentation

For detailed instructions, see:
- [run_instructions.md](run_instructions.md) - Comprehensive guide on how to install and run the project
- [example_output.md](example_output.md) - Examples of what to expect when running different commands
- [README.md](README.md) - General information about the project

## Common Commands

```
# Display help
python -m sosurvey_analyzer.cli --help

# Display survey structure
python -m sosurvey_analyzer.cli structure

# Search for questions
python -m sosurvey_analyzer.cli search-question "experience"

# Search for options
python -m sosurvey_analyzer.cli search-option "DevType" "Data"

# Display answer distribution
python -m sosurvey_analyzer.cli distribution "DevType"

# Create subset and analyze
python -m sosurvey_analyzer.cli subset "DevType" "Data scientist" "YearsCodePro"
```

## Troubleshooting

If you encounter any issues:
1. Make sure Python is installed and added to your PATH
2. Try running commands with `python -m` prefix
3. Check that dependencies (pandas and openpyxl) are installed

For more detailed troubleshooting, see [run_instructions.md](run_instructions.md).