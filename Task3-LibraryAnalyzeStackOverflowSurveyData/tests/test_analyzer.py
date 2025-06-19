import os
import pytest
import pandas as pd
from unittest.mock import patch, MagicMock
from sosurvey_analyzer.analyzer import SOSurveyAnalyzer


# Create a fixture for a mock DataFrame
@pytest.fixture
def mock_survey_data():
    # Create a mock DataFrame with sample survey data
    data = {
        "Respondent": [1, 2, 3, 4, 5],
        "Age": [25, 30, 35, 40, 45],
        "DevType": [
            "Developer, back-end",
            "Developer, front-end",
            "Developer, full-stack",
            "Data scientist or machine learning specialist",
            "Developer, back-end",
        ],
        "YearsCodePro": [1, 5, 10, 15, 20],
        "Employment": ["Employed full-time", "Employed part-time", "Employed full-time", "Employed full-time", "Employed part-time"],
        "Languages": [
            "JavaScript, Python",
            "JavaScript, HTML/CSS",
            "JavaScript, Python, Java",
            "Python, R",
            "Java, C#",
        ],
    }
    return pd.DataFrame(data)


# Test initialization with default path
def test_init_default_path():
    with patch("pandas.read_excel") as mock_read_excel:
        mock_read_excel.return_value = pd.DataFrame({"A": [1, 2], "B": [3, 4]})
        analyzer = SOSurveyAnalyzer()
        assert analyzer.file_path.endswith("so_2024_raw.xlsx")
        assert mock_read_excel.called
        assert list(analyzer.questions) == ["A", "B"]


# Test initialization with custom path
def test_init_custom_path():
    with patch("pandas.read_excel") as mock_read_excel:
        mock_read_excel.return_value = pd.DataFrame({"A": [1, 2], "B": [3, 4]})
        analyzer = SOSurveyAnalyzer("custom_path.xlsx")
        assert analyzer.file_path == "custom_path.xlsx"
        mock_read_excel.assert_called_once_with("custom_path.xlsx")


# Test get_survey_structure
def test_get_survey_structure(mock_survey_data):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        structure = analyzer.get_survey_structure()
        assert isinstance(structure, list)
        assert len(structure) == 6
        assert "DevType" in structure
        assert "Languages" in structure


# Test search_question
def test_search_question(mock_survey_data):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        
        # Test exact match
        results = analyzer.search_question("DevType")
        assert len(results) == 1
        assert results[0] == "DevType"
        
        # Test partial match
        results = analyzer.search_question("dev")
        assert len(results) == 1
        assert results[0] == "DevType"
        
        # Test case insensitivity
        results = analyzer.search_question("devtype")
        assert len(results) == 1
        assert results[0] == "DevType"
        
        # Test no match
        results = analyzer.search_question("nonexistent")
        assert len(results) == 0


# Test search_option
def test_search_option(mock_survey_data):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        
        # Test exact match
        results = analyzer.search_option("DevType", "Developer, back-end")
        assert len(results) == 1
        assert results[0] == "Developer, back-end"
        
        # Test partial match
        results = analyzer.search_option("DevType", "back-end")
        assert len(results) == 1
        assert results[0] == "Developer, back-end"
        
        # Test case insensitivity
        results = analyzer.search_option("DevType", "BACK-END")
        assert len(results) == 1
        assert results[0] == "Developer, back-end"
        
        # Test no match
        results = analyzer.search_option("DevType", "nonexistent")
        assert len(results) == 0
        
        # Test with question not found but similar
        results = analyzer.search_option("Developer", "back-end")
        assert len(results) == 1
        assert results[0] == "Developer, back-end"


# Test create_subset for single choice question
def test_create_subset_single_choice(mock_survey_data):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        
        # Test exact match
        subset = analyzer.create_subset("Employment", "Employed full-time")
        assert len(subset) == 3
        
        # Test with question not found but similar
        subset = analyzer.create_subset("employ", "Employed full-time")
        assert len(subset) == 3
        
        # Test no match
        subset = analyzer.create_subset("Employment", "nonexistent")
        assert len(subset) == 0


# Test create_subset for multiple choice question
def test_create_subset_multiple_choice(mock_survey_data):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        
        # Test exact match
        subset = analyzer.create_subset("Languages", "Python")
        assert len(subset) == 3
        
        # Test with question not found but similar
        subset = analyzer.create_subset("language", "Python")
        assert len(subset) == 3
        
        # Test no match
        subset = analyzer.create_subset("Languages", "nonexistent")
        assert len(subset) == 0


# Test get_answer_distribution for single choice question
def test_get_answer_distribution_single_choice(mock_survey_data):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        
        distribution = analyzer.get_answer_distribution("Employment")
        assert len(distribution) == 2
        assert abs(distribution["Employed full-time"] - 60.0) < 0.01  # 3/5 = 60%
        assert abs(distribution["Employed part-time"] - 40.0) < 0.01  # 2/5 = 40%


# Test get_answer_distribution for multiple choice question
def test_get_answer_distribution_multiple_choice(mock_survey_data):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        
        distribution = analyzer.get_answer_distribution("Languages")
        assert len(distribution) >= 5  # JavaScript, Python, HTML/CSS, Java, R, C#
        
        # Check some expected values
        # Total respondents = 5
        # JavaScript appears in 3 responses
        assert "JavaScript" in distribution
        assert abs(distribution["JavaScript"] - 60.0) < 0.01  # 3/5 = 60%
        
        # Python appears in 3 responses
        assert "Python" in distribution
        assert abs(distribution["Python"] - 60.0) < 0.01  # 3/5 = 60%


# Test display functions (just check they don't raise exceptions)
def test_display_functions(mock_survey_data, capsys):
    with patch("pandas.read_excel", return_value=mock_survey_data):
        analyzer = SOSurveyAnalyzer()
        
        # Test display_survey_structure
        analyzer.display_survey_structure()
        captured = capsys.readouterr()
        assert "Survey contains" in captured.out
        assert "DevType" in captured.out
        
        # Test display_answer_distribution
        analyzer.display_answer_distribution("DevType")
        captured = capsys.readouterr()
        assert "Distribution for question: DevType" in captured.out
        assert "Developer, back-end" in captured.out