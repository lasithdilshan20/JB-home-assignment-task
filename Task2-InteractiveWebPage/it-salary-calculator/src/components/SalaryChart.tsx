import React, { useMemo } from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ChartOptions,
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

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

interface SalaryChartProps {
  data: SalaryEntry[];
}

const SalaryChart: React.FC<SalaryChartProps> = ({ data }) => {
  // Process data for the chart
  const chartData = useMemo(() => {
    // Group data by experience level
    const experienceLevels = Array.from(new Set(data.map(entry => entry.category))).sort((a, b) => {
      // Custom sorting for experience levels
      const order = ['<1 year', '1-3 years', '3-5 years', '5-10 years', '10-15 years', '15+ years'];
      return order.indexOf(a) - order.indexOf(b);
    });

    // Calculate average salary for each experience level
    const averageSalaries = experienceLevels.map(level => {
      const entriesForLevel = data.filter(entry => entry.category === level);
      const sum = entriesForLevel.reduce((acc, entry) => acc + entry.value, 0);
      return Math.round(sum / entriesForLevel.length);
    });

    // Count entries for each experience level
    const entryCounts = experienceLevels.map(level => {
      return data.filter(entry => entry.category === level).length;
    });

    return {
      labels: experienceLevels,
      datasets: [
        {
          label: 'Average Salary (K USD/year)',
          data: averageSalaries,
          backgroundColor: 'rgba(99, 102, 241, 0.8)',
          borderColor: 'rgba(99, 102, 241, 1)',
          borderWidth: 1,
          borderRadius: 6,
          hoverBackgroundColor: 'rgba(99, 102, 241, 1)',
        },
      ],
      entryCounts,
    };
  }, [data]);

  const options: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top' as const,
        labels: {
          color: 'rgba(255, 255, 255, 0.8)',
          font: {
            size: 12,
          },
        },
      },
      title: {
        display: false,
      },
      tooltip: {
        callbacks: {
          afterLabel: function(context) {
            const index = context.dataIndex;
            return `Based on ${chartData.entryCounts[index]} data points`;
          }
        },
        backgroundColor: 'rgba(31, 41, 55, 0.9)',
        titleColor: 'rgba(255, 255, 255, 1)',
        bodyColor: 'rgba(255, 255, 255, 0.8)',
        borderColor: 'rgba(99, 102, 241, 0.5)',
        borderWidth: 1,
        padding: 12,
        cornerRadius: 8,
      },
    },
    scales: {
      x: {
        grid: {
          color: 'rgba(255, 255, 255, 0.1)',
        },
        ticks: {
          color: 'rgba(255, 255, 255, 0.7)',
        },
      },
      y: {
        grid: {
          color: 'rgba(255, 255, 255, 0.1)',
        },
        ticks: {
          color: 'rgba(255, 255, 255, 0.7)',
          callback: function(value) {
            return '$' + value + 'K';
          }
        },
        title: {
          display: true,
          text: 'Salary (USD/year)',
          color: 'rgba(255, 255, 255, 0.7)',
        }
      },
    },
    animation: {
      duration: 1000,
    },
  };

  return (
    <div className="h-[400px]">
      <Bar data={chartData} options={options} />
      <div className="mt-4 text-sm text-gray-400 text-center">
        <p>Chart shows average salary by experience level for selected country and language</p>
      </div>
    </div>
  );
};

export default SalaryChart;