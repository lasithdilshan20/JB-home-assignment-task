# Example Output of Stack Overflow Survey Analyzer

This document shows examples of what you can expect to see when running the Stack Overflow Survey Analyzer with different commands.

## Example 1: Display Survey Structure

When you run:
```
python -m sosurvey_analyzer.cli structure
```

You'll see output similar to:
```
Survey contains 84 questions:
1. Respondent
2. MainBranch
3. Employment
4. RemoteWork
5. CodingActivities
...
```

This shows all the questions in the survey.

## Example 2: Search for Questions

When you run:
```
python -m sosurvey_analyzer.cli search-question "experience"
```

You'll see output similar to:
```
Found 5 questions matching 'experience':
1. YearsCode
2. YearsCodePro
3. DevExperience
4. WebframeWorkedWith
5. WebframeExperience
```

This shows all questions that contain the word "experience".

## Example 3: Search for Options in a Question

When you run:
```
python -m sosurvey_analyzer.cli search-option "DevType" "Data"
```

You'll see output similar to:
```
Found 3 options matching 'Data' in question 'DevType':
1. Data scientist or machine learning specialist
2. Database administrator
3. Data or business analyst
```

This shows all options in the "DevType" question that contain the word "Data".

## Example 4: Display Answer Distribution

When you run:
```
python -m sosurvey_analyzer.cli distribution "DevType"
```

You'll see output similar to:
```
Distribution for question: DevType
--------------------------------------------------
Developer, full-stack: 42.35%
Developer, back-end: 33.18%
Developer, front-end: 24.86%
Data scientist or machine learning specialist: 8.42%
DevOps specialist: 7.94%
...
```

This shows the percentage of respondents who selected each option for the "DevType" question.

## Example 5: Create a Subset and Analyze

When you run:
```
python -m sosurvey_analyzer.cli subset "DevType" "Data scientist" "YearsCodePro"
```

You'll see output similar to:
```
Created subset with 1,842 respondents who selected 'Data scientist' for 'DevType'

Analyzing distribution in the subset:
Distribution for question: YearsCodePro
--------------------------------------------------
3-5 years: 22.15%
1-2 years: 18.73%
6-8 years: 15.42%
9-11 years: 12.87%
...
```

This shows the distribution of professional coding experience among data scientists.