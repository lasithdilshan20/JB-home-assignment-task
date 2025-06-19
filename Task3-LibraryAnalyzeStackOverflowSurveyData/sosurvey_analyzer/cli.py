import argparse
import sys
from .analyzer import SOSurveyAnalyzer


def main():
    """Main entry point for the CLI."""
    parser = argparse.ArgumentParser(
        description="Stack Overflow Survey Analyzer CLI",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Display survey structure
  python -m sosurvey_analyzer.cli structure
  
  # Search for questions containing "experience"
  python -m sosurvey_analyzer.cli search-question "experience"
  
  # Search for options in a question
  python -m sosurvey_analyzer.cli search-option "DevType" "Data"
  
  # Display answer distribution for a question
  python -m sosurvey_analyzer.cli distribution "DevType"
"""
    )
    
    subparsers = parser.add_subparsers(dest="command", help="Command to execute")
    
    # Structure command
    structure_parser = subparsers.add_parser("structure", help="Display survey structure")
    structure_parser.add_argument(
        "--file", "-f", help="Path to the survey data file (XLSX)"
    )
    
    # Search question command
    search_question_parser = subparsers.add_parser(
        "search-question", help="Search for questions containing a string"
    )
    search_question_parser.add_argument("query", help="Search string")
    search_question_parser.add_argument(
        "--file", "-f", help="Path to the survey data file (XLSX)"
    )
    
    # Search option command
    search_option_parser = subparsers.add_parser(
        "search-option", help="Search for options in a question"
    )
    search_option_parser.add_argument("question", help="Question to search in")
    search_option_parser.add_argument("option_query", help="Option search string")
    search_option_parser.add_argument(
        "--file", "-f", help="Path to the survey data file (XLSX)"
    )
    
    # Distribution command
    distribution_parser = subparsers.add_parser(
        "distribution", help="Display answer distribution for a question"
    )
    distribution_parser.add_argument("question", help="Question to analyze")
    distribution_parser.add_argument(
        "--file", "-f", help="Path to the survey data file (XLSX)"
    )
    
    # Subset command
    subset_parser = subparsers.add_parser(
        "subset", help="Create a subset of respondents and analyze another question"
    )
    subset_parser.add_argument("filter_question", help="Question to filter on")
    subset_parser.add_argument("filter_option", help="Option value to filter for")
    subset_parser.add_argument(
        "analyze_question", help="Question to analyze in the subset"
    )
    subset_parser.add_argument(
        "--file", "-f", help="Path to the survey data file (XLSX)"
    )
    
    args = parser.parse_args()
    
    if not args.command:
        parser.print_help()
        return
    
    try:
        # Initialize the analyzer
        file_path = args.file if hasattr(args, "file") and args.file else None
        analyzer = SOSurveyAnalyzer(file_path)
        
        # Execute the requested command
        if args.command == "structure":
            analyzer.display_survey_structure()
        
        elif args.command == "search-question":
            results = analyzer.search_question(args.query)
            if results:
                print(f"Found {len(results)} questions matching '{args.query}':")
                for i, question in enumerate(results, 1):
                    print(f"{i}. {question}")
            else:
                print(f"No questions found matching '{args.query}'")
        
        elif args.command == "search-option":
            results = analyzer.search_option(args.question, args.option_query)
            if results:
                print(
                    f"Found {len(results)} options matching '{args.option_query}' in question '{args.question}':"
                )
                for i, option in enumerate(results, 1):
                    print(f"{i}. {option}")
            else:
                print(
                    f"No options found matching '{args.option_query}' in question '{args.question}'"
                )
        
        elif args.command == "distribution":
            analyzer.display_answer_distribution(args.question)
        
        elif args.command == "subset":
            # Create subset
            subset = analyzer.create_subset(args.filter_question, args.filter_option)
            if subset.empty:
                print(
                    f"No respondents found for '{args.filter_option}' in question '{args.filter_question}'"
                )
                return
            
            print(
                f"Created subset with {len(subset)} respondents who selected '{args.filter_option}' for '{args.filter_question}'"
            )
            
            # Create a new analyzer with the subset
            subset_analyzer = SOSurveyAnalyzer(file_path)
            subset_analyzer.data = subset
            
            # Display distribution for the analyze question
            print("\nAnalyzing distribution in the subset:")
            subset_analyzer.display_answer_distribution(args.analyze_question)
    
    except Exception as e:
        print(f"Error: {str(e)}", file=sys.stderr)
        return 1
    
    return 0


if __name__ == "__main__":
    sys.exit(main())