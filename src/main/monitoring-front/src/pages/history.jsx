import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { call } from '../util/call';
import '../css/history.css';
import { Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  PointElement,
  LineElement,
  CategoryScale,
  LinearScale,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

// Chart.js 모듈 등록
ChartJS.register(PointElement, LineElement, CategoryScale, LinearScale, Title, Tooltip, Legend);

const History = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { id } = location.state || {}; // params 객체에서 id를 추출합니다.
  const [server, setServer] = useState({});
  const [metrics, setMetrics] = useState([]);
  const [errorMessage, setErrorMessage] = useState(null);
  const [loading, setLoading] = useState(true);
  const [chartData, setChartData] = useState({
    labels: [],
    datasets: [
      {
        label: 'CPU Usage',
        data: [],
        borderColor: 'rgba(75, 192, 192, 1)',
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        fill: true,
      },
    ],
  });

  useEffect(() => {
    if (!id) {
      navigate('/'); // id가 없으면 홈으로 리디렉션
      return;
    }
    const fetchData = async () => {
      try {
        console.log("id: ", id);
        const data = await call(`/api/history/${id.current}`, 'POST');
        const logs = await call(`/log/history/${id.current}`, 'POST');
        setServer(data);
        setMetrics(logs);
        setLoading(false);

        // 컴포넌트가 처음 마운트될 때 CPU Usage 차트로 초기화
        handleDivClick('CPU');
      } catch (error) {
        if (error.status != null) {
          const errorData = await error.json();
          setErrorMessage(errorData);
        }
        setLoading(false);
      }
    };

    fetchData();
  }, [id, navigate]);

  const handleButtonClick = () => {
    navigate(`/`);
  };

  const handleDivClick = (type) => {
    let newData = {
      labels: metrics.map((log) => new Date(log.createdDate).toLocaleDateString()),
      datasets: []
    };

    switch (type) {
      case 'CPU':
        newData.datasets.push({
          label: 'CPU Usage',
          data: metrics.map((log) => log.cpuUsage),
          borderColor: 'rgba(75, 192, 192, 1)',
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          fill: true,
        });
        break;
      case 'Memory':
        newData.datasets.push({
          label: 'Memory Usage',
          data: metrics.map((log) => log.memoryUsage),
          borderColor: 'rgba(153, 102, 255, 1)',
          backgroundColor: 'rgba(153, 102, 255, 0.2)',
          fill: true,
        });
        break;
      case 'Disk1':
        newData.datasets.push({
          label: 'Disk1 Usage',
          data: metrics.map((log) => log.diskUsage1),
          borderColor: 'rgba(255, 159, 64, 1)',
          backgroundColor: 'rgba(255, 159, 64, 0.2)',
          fill: true,
        });
        break;
      case 'Disk2':
        newData.datasets.push({
          label: 'Disk2 Usage',
          data: metrics.map((log) => log.diskUsage2),
          borderColor: 'rgba(255, 99, 132, 1)',
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
          fill: true,
        });
        break;
      case 'Disk3':
        newData.datasets.push({
          label: 'Disk3 Usage',
          data: metrics.map((log) => log.diskUsage3),
          borderColor: 'rgba(54, 162, 235, 1)',
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          fill: true,
        });
        break;
      case 'Disk4':
        newData.datasets.push({
          label: 'Disk4 Usage',
          data: metrics.map((log) => log.diskUsage4),
          borderColor: 'rgba(255, 206, 86, 1)',
          backgroundColor: 'rgba(255, 206, 86, 0.2)',
          fill: true,
        });
        break;
      default:
        break;
    }

    setChartData(newData);
  };

  if (loading) {
    return <div>데이터를 가져오는 중...</div>; // 로딩 중일 때 표시할 내용
  }

  if (errorMessage) {
    return (
       <div>
         <p>{errorMessage.message}</p>
         <p>{errorMessage.status}</p>
         <p>{errorMessage.name}</p>
         <p>{errorMessage.code}</p>
       </div>
    );
  }

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

  return (
     <div id='historyContainer'>
       <h1>서버 히스토리</h1>
       <div id='menu'>
         <div id='historyCpu' onClick={() => handleDivClick('CPU')}>
           <div>CPU</div>
           <div></div>
         </div>
         <div id='historyMemory' onClick={() => handleDivClick('Memory')}>
           <div>Memory</div>
           <div></div>
         </div>
         <div id='historyDisk1' onClick={() => handleDivClick('Disk1')}>
           <div>Disk1</div>
           <div></div>
         </div>
         <div id='historyDisk2' onClick={() => handleDivClick('Disk2')}>
           <div>Disk2</div>
           <div></div>
         </div>
         <div id='historyDisk3' onClick={() => handleDivClick('Disk3')}>
           <div>Disk3</div>
           <div></div>
         </div>
         <div id='historyDisk4' onClick={() => handleDivClick('Disk4')}>
           <div>Disk4</div>
           <div></div>
         </div>
         <div>
           <button onClick={handleButtonClick} className="btn btn-primary">
             이전
           </button>
         </div>
       </div>
       <div id='chart'>
         <h2>CPU Usage Chart</h2>
         <Line data={chartData} options={chartOptions} />
       </div>
     </div>
  );
};

export default History;