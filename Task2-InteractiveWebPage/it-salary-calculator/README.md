# IT Salary Calculator

An interactive web application that visualizes developer salary data based on programming language and country. The application is built with React, TypeScript, Tailwind CSS, and Chart.js.

## Features

- Filter salary data by country and programming language
- Interactive chart visualization grouped by experience level
- Responsive design with a futuristic UI
- Data visualization based on JetBrains Developer Ecosystem Survey 2024

## Prerequisites

- Node.js (v14.0.0 or higher)
- npm (v6.0.0 or higher)

## Installation

1. Clone the repository
2. Navigate to the project directory:
   ```
   cd it-salary-calculator
   ```
3. Copy the data file to the public directory:
   ```
   copy-data.bat
   ```
   This will copy the calculatorData.json file from the original location to the public directory.
4. Install dependencies:
   ```
   npm install
   ```

## Running the Application

To start the development server:

```
npm run dev
```

The application will be available at http://localhost:5173

## Building for Production

To build the application for production:

```
npm run build
```

The build artifacts will be stored in the `dist/` directory.

## Project Structure

```
it-salary-calculator/
├── public/
│   └── calculatorData.json
├── src/
│   ├── components/
│   │   ├── InputForm.tsx
│   │   └── SalaryChart.tsx
│   ├── App.tsx
│   └── main.tsx
├── index.html
├── tailwind.config.js
└── README.md
```

## Technologies Used

- React + TypeScript
- Tailwind CSS
- Chart.js with react-chartjs-2
- Vite
