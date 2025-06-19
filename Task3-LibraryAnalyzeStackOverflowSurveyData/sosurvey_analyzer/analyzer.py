import pandas as pd
import os
from typing import List, Dict, Any, Optional, Set, Tuple


class SOSurveyAnalyzer:
    """
    A class for analyzing Stack Overflow Survey data.
    
    This class provides methods to:
    - Display the survey structure (list of questions)
    - Search for specific questions/options
    - Create respondent subsets based on question+option
    - Display distribution of answers for SC and MC questions
    """
    
    def __init__(self, file_path: str = None):
        """
        Initialize the analyzer with the path to the survey data file.
        
        Args:
            file_path: Path to the XLSX file containing the survey data.
                       If None, uses the default path.
        """
        if file_path is None:
            # Use default path relative to the package
            current_dir = os.path.dirname(os.path.abspath(__file__))
            root_dir = os.path.dirname(os.path.dirname(current_dir))
            file_path = os.path.join(root_dir, 'Data', 'so_2024_raw.xlsx')
        
        self.file_path = file_path
        self.data = None
        self.questions = None
        self.load_data()
    
    def load_data(self) -> None:
        """Load the survey data from the XLSX file."""
        self.data = pd.read_excel(self.file_path)
        # Extract questions (column names)
        self.questions = list(self.data.columns)
    
    def get_survey_structure(self) -> List[str]:
        """
        Get the structure of the survey (list of questions).
        
        Returns:
            List of questions in the survey.
        """
        return self.questions
    
    def display_survey_structure(self) -> None:
        """Display the survey structure (list of questions) to the console."""
        questions = self.get_survey_structure()
        print(f"Survey contains {len(questions)} questions:")
        for i, question in enumerate(questions, 1):
            print(f"{i}. {question}")
    
    def search_question(self, query: str) -> List[str]:
        """
        Search for questions containing the query string.
        
        Args:
            query: The search string.
            
        Returns:
            List of questions matching the query.
        """
        return [q for q in self.questions if query.lower() in q.lower()]
    
    def search_option(self, question: str, option_query: str) -> List[str]:
        """
        Search for options in a specific question.
        
        Args:
            question: The question to search in.
            option_query: The option search string.
            
        Returns:
            List of options matching the query.
        """
        if question not in self.questions:
            matching_questions = self.search_question(question)
            if not matching_questions:
                return []
            question = matching_questions[0]
        
        # Get unique values for the question
        options = self.data[question].dropna().unique().tolist()
        return [opt for opt in options if option_query.lower() in str(opt).lower()]
    
    def create_subset(self, question: str, option: str) -> pd.DataFrame:
        """
        Create a subset of respondents based on question and option.
        
        Args:
            question: The question to filter on.
            option: The option value to filter for.
            
        Returns:
            DataFrame containing only respondents who selected the given option.
        """
        if question not in self.questions:
            matching_questions = self.search_question(question)
            if not matching_questions:
                return pd.DataFrame()
            question = matching_questions[0]
        
        # For multiple choice questions (comma-separated values)
        if self.data[question].dtype == 'object':
            # Check if the option is contained in the response
            return self.data[self.data[question].fillna('').str.contains(option, na=False)]
        else:
            # For single choice questions
            return self.data[self.data[question] == option]
    
    def get_answer_distribution(self, question: str) -> Dict[str, float]:
        """
        Get the distribution of answers for a question.
        
        Args:
            question: The question to analyze.
            
        Returns:
            Dictionary mapping options to their percentage share.
        """
        if question not in self.questions:
            matching_questions = self.search_question(question)
            if not matching_questions:
                return {}
            question = matching_questions[0]
        
        # For multiple choice questions (comma-separated values)
        if self.data[question].dtype == 'object' and self.data[question].str.contains(',', na=False).any():
            # Split multiple choice answers and count each option
            all_options = []
            for answer in self.data[question].dropna():
                options = [opt.strip() for opt in str(answer).split(',')]
                all_options.extend(options)
            
            # Count occurrences of each option
            option_counts = pd.Series(all_options).value_counts()
            total_respondents = len(self.data)
            
            # Calculate percentages
            distribution = {opt: (count / total_respondents) * 100 for opt, count in option_counts.items()}
        else:
            # For single choice questions
            value_counts = self.data[question].value_counts(dropna=False)
            total = value_counts.sum()
            
            # Calculate percentages
            distribution = {str(opt): (count / total) * 100 for opt, count in value_counts.items()}
        
        return distribution
    
    def display_answer_distribution(self, question: str) -> None:
        """
        Display the distribution of answers for a question to the console.
        
        Args:
            question: The question to analyze.
        """
        if question not in self.questions:
            matching_questions = self.search_question(question)
            if not matching_questions:
                print(f"No question found matching '{question}'")
                return
            question = matching_questions[0]
        
        distribution = self.get_answer_distribution(question)
        
        print(f"Distribution for question: {question}")
        print("-" * 50)
        
        # Sort by percentage (descending)
        sorted_dist = sorted(distribution.items(), key=lambda x: x[1], reverse=True)
        
        for option, percentage in sorted_dist:
            option_display = option if option != 'nan' else 'No answer'
            print(f"{option_display}: {percentage:.2f}%")