import { useState, useEffect } from 'react';
import InputForm from './components/InputForm';
import SalaryChart from './components/SalaryChart';

// Define types for our data structure
interface SalaryEntry {
  value: number;
  category: string;
  metadata: {
    Country: string;
    Language: string;
    Experience: string;
    Salary: string;
  };
}

interface SalaryData {
  [country: string]: {
    [language: string]: {
      entries: SalaryEntry[];
    };
  };
}

function App() {
  // State for the data and filters
  const [data, setData] = useState<SalaryData | null>(null);
  const [countries, setCountries] = useState<string[]>([]);
  const [languages, setLanguages] = useState<string[]>([]);
  const [selectedCountry, setSelectedCountry] = useState<string>('');
  const [selectedLanguage, setSelectedLanguage] = useState<string>('');
  const [filteredData, setFilteredData] = useState<SalaryEntry[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // Fetch data on component mount
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('/calculatorData.json');
        if (!response.ok) {
          throw new Error('Failed to fetch data');
        }
        const jsonData: SalaryData = await response.json();
        setData(jsonData);
        
        // Extract unique countries and languages
        const countryList = Object.keys(jsonData);
        setCountries(countryList);
        
        if (countryList.length > 0) {
          setSelectedCountry(countryList[0]);
          
          const languageList = Object.keys(jsonData[countryList[0]]);
          setLanguages(languageList);
          
          if (languageList.length > 0) {
            setSelectedLanguage(languageList[0]);
          }
        }
        
        setLoading(false);
      } catch (err) {
        setError('Error loading data. Please try again later.');
        setLoading(false);
        console.error('Error fetching data:', err);
      }
    };

    fetchData();
  }, []);

  // Update languages when country changes
  useEffect(() => {
    if (data && selectedCountry) {
      const languageList = Object.keys(data[selectedCountry]);
      setLanguages(languageList);
      
      if (languageList.length > 0) {
        // If current language is not available in new country, select first available
        if (!languageList.includes(selectedLanguage)) {
          setSelectedLanguage(languageList[0]);
        }
      } else {
        setSelectedLanguage('');
      }
    }
  }, [selectedCountry, data]);

  // Update filtered data when country or language changes
  useEffect(() => {
    if (data && selectedCountry && selectedLanguage) {
      if (data[selectedCountry] && data[selectedCountry][selectedLanguage]) {
        setFilteredData(data[selectedCountry][selectedLanguage].entries);
      } else {
        setFilteredData([]);
      }
    } else {
      setFilteredData([]);
    }
  }, [selectedCountry, selectedLanguage, data]);

  // Handle country change
  const handleCountryChange = (country: string) => {
    setSelectedCountry(country);
  };

  // Handle language change
  const handleLanguageChange = (language: string) => {
    setSelectedLanguage(language);
  };

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <header className="text-center mb-10">
        <h1 className="text-4xl font-bold mb-2 text-transparent bg-clip-text bg-gradient-to-r from-primary-light to-secondary-light">
          IT Salary Calculator
        </h1>
        <p className="text-gray-300">
          Based on JetBrains Developer Ecosystem Survey 2024
        </p>
      </header>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-1">
          <div className="panel h-full">
            <h2 className="text-2xl font-bold mb-6 text-primary-light">Filters</h2>
            {loading ? (
              <p>Loading filters...</p>
            ) : error ? (
              <p className="text-red-500">{error}</p>
            ) : (
              <InputForm
                countries={countries}
                languages={languages}
                selectedCountry={selectedCountry}
                selectedLanguage={selectedLanguage}
                onCountryChange={handleCountryChange}
                onLanguageChange={handleLanguageChange}
              />
            )}
          </div>
        </div>

        <div className="lg:col-span-2">
          <div className="panel h-full">
            <h2 className="text-2xl font-bold mb-6 text-primary-light">Salary Distribution</h2>
            {loading ? (
              <div className="flex justify-center items-center h-64">
                <p>Loading chart data...</p>
              </div>
            ) : error ? (
              <p className="text-red-500">{error}</p>
            ) : filteredData.length > 0 ? (
              <SalaryChart data={filteredData} />
            ) : (
              <div className="flex justify-center items-center h-64">
                <p>No data available for the selected filters.</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;