const dataArrays = [];
let xScale = {
  min: 0,
  max: 1440
};
const labelSet =
   ['CPU Usage', 'Memory Usage', 'Disk1 Usage',
     'Disk2 Usage', 'Disk3 Usage', 'Disk4 Usage'];
const borderColors =
   ['rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)',
     'rgba(54, 162, 235, 1)', 'rgba(255, 159, 64, 1)',
     'rgba(255, 206, 86, 1)', 'rgba(255, 99, 132, 1)'];
const backgroundColors =
   ['rgba(75, 192, 192, 0.2)', 'rgba(153, 102, 255, 0.2)',
     'rgba(54, 162, 235, 0.2)', 'rgba(255, 159, 64, 0.2)',
     'rgba(255, 99, 132, 0.2)', 'rgba(255, 206, 86, 0.2)'];

for (let i = 0; i < 6; i++) {
  const dataArray = makeEmptyArray(1440);
  dataArrays.push(dataArray)
}

let chartInstance = null;
function drawChart(metrics, labels, isRepeat) {
  const dataKeySet = ['cpuUsage', 'memoryUsage', 'diskUsage1'];

  if (metrics) {
    const keys = ['diskUsage2', 'diskUsage3', 'diskUsage4'];
    let arrayIndex = 0;
    while (arrayIndex < keys.length) {
      const currentMetric = metrics[0][keys[arrayIndex]];
      if (currentMetric === null) {
        break; // null을 발견하면 루프 종료
      }
      dataKeySet.push(keys[arrayIndex]);
      arrayIndex++;
    }
  }

  // 데이터 배열 초기화 및 업데이트
  dataKeySet.forEach((usage, index) => {// 데이터 배열 초기화
    for (let i = 0; i < metrics.length; i++) {
      const now = new Date(metrics[i].createdDate);
      const hour = now.getHours()*60;
      const minute = now.getMinutes();
      const timeIndex = hour + minute;
      dataArrays[index][timeIndex] = metrics[i][usage];
    }
  });


  const ctx = document.getElementById('myChart').getContext('2d');

  // 기존 차트가 있으면 삭제
  if (chartInstance) {
    chartInstance.destroy();
  }

  // 새로운 차트 생성
  chartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: dataKeySet.map((dataArray, index) => ({
        type: 'line',
        data: dataArrays[index],
        label: labelSet[index],
        borderColor: borderColors[index],
        backgroundColor: backgroundColors[index],
        borderWidth: 1,
        pointRadius: function (context) {
          return (context.dataIndex % 5 === 0) ? 2 : 0;
        },
        pointHoverRadius: 5,
        tension: 0.1,
        spanGaps: true
      }))
    },
    options: {
      responsive: true,
      animation: {
        duration: 0 // 애니메이션을 비활성화합니다.
      },
      plugins: {
        legend: {
          position: 'top',
        },
        zoom: {
          pan: {
            enabled: true,
            mode: 'x',
            speed: 1, // 팬의 속도 조절
            onPanComplete: ({ chart }) => {
              xScale.min = chart.scales.x.min;
              xScale.max = chart.scales.x.max;
            }
          },
          zoom: {
            wheel: {
              enabled: true,
            },
            mode: 'x',
            speed: 0.1,
            onZoomComplete: ({ chart }) => {
              xScale.min = chart.scales.x.min;
              xScale.max = chart.scales.x.max;
            }
          }
        }
      },
      scales: {
        x: {
          title: {
            display: true,
            text: 'Date',
          },
          min: xScale.min,
          max: xScale.max,
          ticks: {
            callback: (index) => {
              if (index % 60 === 0) {
                return labels[index];
              }
            },
            font: {
              size: 12
            },
            align: 'center',
            padding: 3,
            maxRotation: 55,
            minRotation: 0
          },
          grid: {
            display: true,
            drawBorder: false,
            drawOnChartArea: true,
            lineWidth: 1,
            color: 'rgba(0, 0, 0, 0.2)'
          }
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
    }
  });

  chartInstance.update(); // 차트 업데이트
}

function makeAxis(metrics) {
  const labels = [];
  if (!Array.isArray(metrics) || metrics.length === 0) {
    return labels; // 또는 다른 적절한 처리를 수행
  }
  const now = new Date(metrics[0].createdDate);
  const month = (`${now.getMonth() + 1}`).slice(-2).padStart(2, '0');
  const day = (`${now.getDate()}`).slice(-2).padStart(2, '0');
  for (let i = 0; i < 1440; i++) {
    if (i % 60 === 0) {
      const hours = Math.floor(i / 60);
      const minutes = i % 60;
      const time = `${(`${hours}`).slice(-2).padStart(2, '0')}:${(`${minutes}`).slice(-2).padStart(2, '0')}`;
      labels[i] = `${month}/${day}\n${time}`;
    } else {
      labels[i] = '';
    }
  }
  return labels;
}

function makeEmptyArray(size) {
  return new Array(size).fill(null);
}