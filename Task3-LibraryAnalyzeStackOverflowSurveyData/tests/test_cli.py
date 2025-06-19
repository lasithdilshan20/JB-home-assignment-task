import sys
import pytest
from unittest.mock import patch, MagicMock
from sosurvey_analyzer.cli import main


# Test help command
def test_help(capsys):
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer"]):
        # Call main function
        main()
        
        # Check output
        captured = capsys.readouterr()
        assert "Stack Overflow Survey Analyzer CLI" in captured.out
        assert "Commands:" in captured.out or "command" in captured.out


# Test structure command
def test_structure_command(capsys):
    # Mock SOSurveyAnalyzer
    mock_analyzer = MagicMock()
    mock_analyzer.display_survey_structure.return_value = None
    
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer", "structure"]):
        # Mock SOSurveyAnalyzer class
        with patch("sosurvey_analyzer.cli.SOSurveyAnalyzer", return_value=mock_analyzer):
            # Call main function
            main()
            
            # Check that display_survey_structure was called
            mock_analyzer.display_survey_structure.assert_called_once()


# Test search-question command
def test_search_question_command(capsys):
    # Mock SOSurveyAnalyzer
    mock_analyzer = MagicMock()
    mock_analyzer.search_question.return_value = ["Question1", "Question2"]
    
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer", "search-question", "query"]):
        # Mock SOSurveyAnalyzer class
        with patch("sosurvey_analyzer.cli.SOSurveyAnalyzer", return_value=mock_analyzer):
            # Call main function
            main()
            
            # Check that search_question was called with the right argument
            mock_analyzer.search_question.assert_called_once_with("query")
            
            # Check output
            captured = capsys.readouterr()
            assert "Found 2 questions" in captured.out
            assert "Question1" in captured.out
            assert "Question2" in captured.out


# Test search-option command
def test_search_option_command(capsys):
    # Mock SOSurveyAnalyzer
    mock_analyzer = MagicMock()
    mock_analyzer.search_option.return_value = ["Option1", "Option2"]
    
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer", "search-option", "question", "option_query"]):
        # Mock SOSurveyAnalyzer class
        with patch("sosurvey_analyzer.cli.SOSurveyAnalyzer", return_value=mock_analyzer):
            # Call main function
            main()
            
            # Check that search_option was called with the right arguments
            mock_analyzer.search_option.assert_called_once_with("question", "option_query")
            
            # Check output
            captured = capsys.readouterr()
            assert "Found 2 options" in captured.out
            assert "Option1" in captured.out
            assert "Option2" in captured.out


# Test distribution command
def test_distribution_command(capsys):
    # Mock SOSurveyAnalyzer
    mock_analyzer = MagicMock()
    mock_analyzer.display_answer_distribution.return_value = None
    
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer", "distribution", "question"]):
        # Mock SOSurveyAnalyzer class
        with patch("sosurvey_analyzer.cli.SOSurveyAnalyzer", return_value=mock_analyzer):
            # Call main function
            main()
            
            # Check that display_answer_distribution was called with the right argument
            mock_analyzer.display_answer_distribution.assert_called_once_with("question")


# Test subset command
def test_subset_command(capsys):
    # Mock SOSurveyAnalyzer
    mock_analyzer = MagicMock()
    mock_subset = MagicMock()
    mock_subset.empty = False
    mock_subset.__len__.return_value = 10
    mock_analyzer.create_subset.return_value = mock_subset
    
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer", "subset", "filter_question", "filter_option", "analyze_question"]):
        # Mock SOSurveyAnalyzer class
        with patch("sosurvey_analyzer.cli.SOSurveyAnalyzer", return_value=mock_analyzer):
            # Call main function
            main()
            
            # Check that create_subset was called with the right arguments
            mock_analyzer.create_subset.assert_called_once_with("filter_question", "filter_option")
            
            # Check that display_answer_distribution was called with the right argument
            assert mock_analyzer.display_answer_distribution.called
            
            # Check output
            captured = capsys.readouterr()
            assert "Created subset with 10 respondents" in captured.out


# Test subset command with empty result
def test_subset_command_empty(capsys):
    # Mock SOSurveyAnalyzer
    mock_analyzer = MagicMock()
    mock_subset = MagicMock()
    mock_subset.empty = True
    mock_analyzer.create_subset.return_value = mock_subset
    
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer", "subset", "filter_question", "filter_option", "analyze_question"]):
        # Mock SOSurveyAnalyzer class
        with patch("sosurvey_analyzer.cli.SOSurveyAnalyzer", return_value=mock_analyzer):
            # Call main function
            main()
            
            # Check that create_subset was called with the right arguments
            mock_analyzer.create_subset.assert_called_once_with("filter_question", "filter_option")
            
            # Check that display_answer_distribution was not called
            assert not mock_analyzer.display_answer_distribution.called
            
            # Check output
            captured = capsys.readouterr()
            assert "No respondents found" in captured.out


# Test error handling
def test_error_handling(capsys):
    # Mock SOSurveyAnalyzer
    mock_analyzer = MagicMock()
    mock_analyzer.display_survey_structure.side_effect = Exception("Test error")
    
    # Mock sys.argv
    with patch.object(sys, "argv", ["sosurvey_analyzer", "structure"]):
        # Mock SOSurveyAnalyzer class
        with patch("sosurvey_analyzer.cli.SOSurveyAnalyzer", return_value=mock_analyzer):
            # Call main function
            result = main()
            
            # Check that the function returned an error code
            assert result == 1
            
            # Check output
            captured = capsys.readouterr()
            assert "Error: Test error" in captured.err