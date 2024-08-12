// src/components/LineChart.js
import React from 'react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, LineElement, PointElement, CategoryScale, LinearScale, Title, Tooltip, Legend } from 'chart.js';

// Chart.js 모듈 등록
ChartJS.register(LineElement, PointElement, CategoryScale, LinearScale, Title, Tooltip, Legend);

const LineChart = ({ labels, data, label }) => {
  // Chart.js에 사용할 데이터와 옵션
  const chartData = {
    labels: labels, // 날짜 또는 x축 레이블
    datasets: [
      {
        label: label, // 차트의 레이블
        data: data, // y축 데이터
        borderColor: 'rgba(75, 192, 192, 1)',
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        fill: true,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top',
      },
      tooltip: {
        callbacks: {
          label: (context) => `${context.dataset.label}: ${context.raw}%`,
        },
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: 'Date',
        },
      },
      y: {
        title: {
          display: true,
          text: 'Usage (%)',
        },
        beginAtZero: true,
        max: 100,
      },
    },
  };

  return <Line data={chartData} options={chartOptions} />;
};

export default LineChart;