import React from 'react';

interface InputFormProps {
  countries: string[];
  languages: string[];
  selectedCountry: string;
  selectedLanguage: string;
  onCountryChange: (country: string) => void;
  onLanguageChange: (language: string) => void;
}

const InputForm: React.FC<InputFormProps> = ({
  countries,
  languages,
  selectedCountry,
  selectedLanguage,
  onCountryChange,
  onLanguageChange,
}) => {
  return (
    <div className="space-y-6">
      <div>
        <label htmlFor="country" className="block text-sm font-medium text-gray-300 mb-2">
          Country
        </label>
        <select
          id="country"
          value={selectedCountry}
          onChange={(e) => onCountryChange(e.target.value)}
          className="input-select"
        >
          {countries.map((country) => (
            <option key={country} value={country}>
              {country}
            </option>
          ))}
        </select>
      </div>

      <div>
        <label htmlFor="language" className="block text-sm font-medium text-gray-300 mb-2">
          Programming Language
        </label>
        <select
          id="language"
          value={selectedLanguage}
          onChange={(e) => onLanguageChange(e.target.value)}
          className="input-select"
          disabled={languages.length === 0}
        >
          {languages.length === 0 ? (
            <option value="">No languages available</option>
          ) : (
            languages.map((language) => (
              <option key={language} value={language}>
                {language}
              </option>
            ))
          )}
        </select>
      </div>

      <div className="pt-4">
        <div className="bg-background-light rounded-lg p-4 border border-gray-700">
          <h3 className="text-lg font-semibold mb-2 text-primary-light">Selected Filters</h3>
          <div className="grid grid-cols-2 gap-2 text-sm">
            <div className="text-gray-400">Country:</div>
            <div>{selectedCountry || 'None'}</div>
            <div className="text-gray-400">Language:</div>
            <div>{selectedLanguage || 'None'}</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InputForm;