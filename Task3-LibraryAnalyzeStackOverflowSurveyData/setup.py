from setuptools import setup, find_packages

setup(
    name="sosurvey_analyzer",
    version="0.1.0",
    packages=find_packages(),
    install_requires=[
        "pandas>=1.0.0",
        "openpyxl>=3.0.0",
    ],
    entry_points={
        "console_scripts": [
            "sosurvey-analyzer=sosurvey_analyzer.cli:main",
        ],
    },
    python_requires=">=3.6",
    description="A library to analyze Stack Overflow Survey data",
    author="Your Name",
    author_email="your.email@example.com",
    url="https://github.com/yourusername/sosurvey_analyzer",
    classifiers=[
        "Development Status :: 3 - Alpha",
        "Intended Audience :: Developers",
        "License :: OSI Approved :: MIT License",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.6",
        "Programming Language :: Python :: 3.7",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
    ],
)