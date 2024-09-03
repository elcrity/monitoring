document.addEventListener('DOMContentLoaded', () => {
  let needRefresh = true;
  let intervalId;
  let selectedId = null;
  let xScale = {
    min: 0,
    max: 1440
  };
  let isRepeat = true;
  let historyLog = [];
  let dataArray = {};
  let hourArray = new Array(10).fill(null);
  const usageMap = new Map();

  // 대시보드 테이블에 서버, 최신 로그 호출
  const fetchData = async () => {
    // 서버 호출
    const response = await fetch('/api/dashboard');

    if (response.ok) {
      const servers = await response.json();
      //서버 호출 성공시 최신 로그 호출
      const metricsResponse = await fetch('/log', {method: 'POST'});

      if (metricsResponse.ok) {
        //성공시 데이터 가공
        const metrics = await metricsResponse.json();

        const logsMap = metrics.reduce((acc, log) => {
          acc[log.serverId] = log;
          return acc;
        }, {});

        const mergedData = updateMergedData(servers, logsMap);
        updateServerList(mergedData);
      } else {
        // 실패시 예외 받아 error페이지로
        const errorData = await metricsResponse.json();
        const params = new URLSearchParams({
          status: errorData.status,
          name: errorData.name,
          message: errorData.message,
          code: errorData.code
        });

        window.location.href = `/error?${params.toString()}`;
      }
    } else {
      // 실패시 예외 받아 error페이지로
      const errorData = await response.json();
      const params = new URLSearchParams({
        status: errorData.status,
        name: errorData.name,
        message: errorData.message,
        code: errorData.code
      });

      window.location.href = `/error?${params.toString()}`;
    }
  };

  const handleDeleteClick = async (serverId) => {
    if (window.confirm("삭제하시겠습니까?")) {
      try {
        const response = await fetch(`/api/delete/${serverId}`, {method: 'DELETE'});
        const data = await response.json();
        if (data && data.message) {
          alert(data.message);
          fetchData();
        }
      } catch (error) {
        console.error('Error deleting server:', error);
      }
    }
  };

  const selectId = (id) => {

    selectedId = id;
    const historyContainer = document.getElementById("historyContainer");
    if (selectedId !== null) {
      historyContainer.style.display = 'grid';
    }
    isRepeat = false;
    fetchHistory();
    if (intervalId) {
      clearInterval(intervalId); // 기존의 setInterval을 제거
    }
    // intervalId = setInterval(() => {
    //   isRepeat = true;
    //   fetchHistory()
    // }, 10000);
  };

  const updateServerList = (mergedData) => {
    const serverTableBody = document.getElementById('serverTableBody');
    serverTableBody.innerHTML = '';
    mergedData.forEach(data => {
      const serverBoard = document.createElement('div');
      const getStatusIndicatorColor = (data) => {
        if (data.cpuUsage >= 90 || data.memoryUsage >= 90 || data.diskUsage >= 90) {
          return '#ff0000';
        } else if (data.cpuUsage >= 70 || data.memoryUsage >= 70 || data.diskUsage >= 70) {
          return '#ffa500';
        } else {
          return '#00d400';
        }
      };
      const statusIndicatorColor = getStatusIndicatorColor(data)
      serverBoard.id = 'serverTable';
      if(data.cpuUsage) {
        serverBoard.innerHTML = `
                <div id='serverTable-header'>
                  <div>${data.serverHostname}</div>
                  <div id='statusIndicator' style={{ backgroundColor: statusIndicatorColor }}></div>
                  <div>${data.serverIp}</div>
                </div>
                <div id='serverTable-body'>
                  <div id='cpuUsageContainer' 
                  style="display: ${data.cpuUsage !== undefined && data.cpuUsage !== null ? 'flex' : 'none'};">
                    <div id="cpuUsage" style="width: ${data.cpuUsage}%;"></div>
                    <div>${data.cpuUsage}%</div>
                  </div>
                  <div id='memoryUsageContainer'
                  style="display: ${data.memoryUsage !== undefined && data.memoryUsage !== null ? 'flex' : 'none'};">
                    <div id="memoryUsage" style="width: ${data.memoryUsage}%"></div>
                    <div>${data.memoryUsage}%</div>
                  </div>
                  <div id='diskUsageContainer'
                  style="display: ${data.diskUsage1 !== undefined && data.diskUsage1 !== null ? 'flex' : 'none'};">
                    <div id="diskUsage" style="width: ${data.diskUsage1}%"></div>
                    <div>${data.diskUsage1}%</div>
                  </div>
                </div>
                <div id='buttonContainer'>
                    <button id='deleteButton-${data.serverId}' className="btn btn-danger danBtn">X</button>
                </div>`;
      }
      serverTableBody.appendChild(serverBoard);
      const indicator = document.getElementById('statusIndicator');
      indicator.style.backgroundColor = statusIndicatorColor;
      serverBoard.addEventListener('click', () => {
        selectId(data.serverId);
      });

      const deleteButton = document.getElementById(`deleteButton-${data.serverId}`);
      deleteButton.addEventListener('click', () => handleDeleteClick(data.serverId));
    });
  };

  const updateMergedData = (servers, logsMap) => {
    const mergedData = [];

    // 서버 데이터를 순회하여 서버 ID를 키로 사용하는 객체를 만듭니다.
    Object.keys(servers).forEach(serverKey => {
      const server = servers[serverKey];
      const serverId = server.serverId;

      // 로그 데이터에서 서버 ID에 맞는 로그를 찾습니다.
      const log = logsMap[serverId];

      // 서버 객체에 로그 데이터를 추가합니다.
      if (log) {
        mergedData[serverId] = {
          ...server,
          ...log
        };
      } else {
        // 로그 데이터가 없는 경우, 서버 데이터만 포함합니다.
        mergedData[serverId] = server;
      }
    });

    return mergedData;
  };

  const handleButtonClick = (serverId) => {
    window.location.href = `/history?id=${serverId}`;
  };

  //히스토리 .js
  const fetchHistory = async () => {
    const response = await fetch(`/api/history/${selectedId}`, {method: 'POST'});
    if (response.ok) {
      const server = await response.json(); // JSON 데이터를 바로 사용

      const metricsResponse = await fetch(`/log/history/${selectedId}${isRepeat ? '?isRepeat=true' : ''}`, {method: 'POST'});
      historyLog = await metricsResponse.json();
      updateHistory(server, historyLog);
    } else {
      const errorData = await response.json();
      // 쿼리 파라미터로 예외 정보 전송
      const params = new URLSearchParams({
        status: errorData.status,
        name: errorData.name,
        message: errorData.message,
        code: errorData.code
      });

      // error 페이지로 리디렉션
      window.location.href = `/error?${params.toString()}`;
    }
  };
  const updateHistory = (server, metrics) => {
    const historyContainer = document.getElementById('historyContainer');
    // 마지막 항목 가져오기
    const lastMetric =
       Array.isArray(metrics) && metrics.length > 0 ?
          metrics[metrics.length - 1] : null;
    const getUsageColor = (data) => {
      if (data >= 90) {
        return '#ff0000';
      } else if (data >= 70) {
        return '#ffa500';
      } else {
        return '#24FF00';
      }
    };

    // CPU 사용량이 있을 경우 값을 가져오고, 없으면 ''를 표시
    const cpuUsageColor = getUsageColor(lastMetric.cpuUsage);
    // 메모리 사용량을 계산하여 표시
    const memoryUsageColor = getUsageColor(lastMetric.memoryUsage);
    const memoryUsedGB = lastMetric && server
       ? (server.memoryTotal * (lastMetric.memoryUsage / 100) / 1000).toFixed(2)
       : '';
    const totalMemoryGB = server
       ? (server.memoryTotal / 1000).toFixed(2)
       : '';
    // 디스크 사용량
    const diskList = [1, 2, 3, 4].map(i => {
      const usageKey = `diskUsage${i}`;
      const totalKey = `diskTotal${i}`;
      const diskUsageColor = getUsageColor(lastMetric[usageKey]);
      const diskUsage = lastMetric && lastMetric[usageKey] !== null
         ? `${lastMetric[usageKey]}%`
         : null;
      const diskUsedGB = lastMetric && lastMetric[usageKey] !== undefined
         ? ((lastMetric[totalKey] * (lastMetric[usageKey] / 100)) / 1000).toFixed(2)
         : '';
      const diskTotalGB = lastMetric && lastMetric[usageKey] !== undefined
         ? (lastMetric[totalKey] / 1000).toFixed(2)
         : '';


      return {
        diskUsageColor,
        diskUsage,
        diskUsedGB,
        diskTotalGB
      };
    });


    // history html에 추가
    historyContainer.innerHTML = `
    <div id="historyHeader">
      <h2 th:text="히스토리">${server.serverIp} 히스토리</h2>
       <button class="danBtn" id="exitHistory">X</button>
    </div>
    <div id="menu">
      <div id="historyCpu">
        <div class="menuHeader">CPU</div>
        <div class="usage" style="color: ${cpuUsageColor};">${lastMetric.cpuUsage}%</div>
      </div>
      <div id="historyMemory">
        <div class="menuHeader">Memory</div>
        <div class="usage" style="color: ${memoryUsageColor};">${lastMetric.memoryUsage}%</div>
        <div>
          <span>${memoryUsedGB}GB</span> /
          <span>${totalMemoryGB}GB</span> 
        </div>
      </div>
     </div>
    </div>
    <div id="chart">
      <canvas id="myChart" style="width: 800px"></canvas>
    </div>`
    ;

    const menuElement = document.getElementById('menu');

    // disk menu에 추가
    diskList.forEach((diskUsageInfo, index) => {
      if (diskUsageInfo.diskUsage !== null) {
        const diskList = document.createElement('div');
        diskList.id = `diskUsage${index + 1}`;
        diskList.innerHTML = `
      <div class="menuHeader">
        Disk${index + 1}
      </div>
      <div class="usage" 
        style="color: ${diskUsageInfo.diskUsageColor};">
           ${diskUsageInfo.diskUsage}
      </div>
      <div>
         ${diskUsageInfo.diskUsedGB}/${diskUsageInfo.diskTotalGB}GB
      </div>
    `;

        menuElement.appendChild(diskList);

        const exitButton = document.getElementById("exitHistory");
        exitButton.addEventListener('click', () => {
          if (intervalId) {
            clearInterval(intervalId); // 기존의 setInterval을 제거
          }
          historyContainer.style.display = 'none'
        })// 버튼 클릭 시 selectedId를 null로 설정);

      }
    });

    // chart.js
    const initializeEmptyArray = (size) => {
      const array = new Array(size).fill(null);
      return array;
    };

    // 가져온 데이터 이용해서 x축라벨 만들기
    const makeDayLabels = () => {
      const labels = [];
      if (!Array.isArray(metrics) || metrics.length === 0) {
        return labels; // 또는 다른 적절한 처리를 수행
      }
      const now = new Date(metrics[0].createdDate);
      const month = (`${now.getMonth() + 1}`).slice(-2).padStart(2, '0');
      const day = (`${now.getDate()}`).slice(-2).padStart(2, '0');
      for (let i = 0; i < 1440; i++) {
        const hours = Math.floor(i / 60);
        const minutes = i % 60;
        const time = `${(`${hours}`).slice(-2).padStart(2, '0')}:${(`${minutes}`).slice(-2).padStart(2, '0')}`;
        labels[i] = `${month}/${day}\n${time}`;
      }
      return labels;
    };

    const datasetConfig = [
      {
        label: 'CPU Usage',
        borderColor: 'rgba(75, 192, 192, 1)',
        backgroundColor: 'rgba(75, 192, 192, 0.2)'
      },
      {
        label: 'Memory Usage',
        borderColor: 'rgba(153, 102, 255, 1)',
        backgroundColor: 'rgba(153, 102, 255, 0.2)'
      },
      {
        label: 'Disk1 Usage',
        borderColor: 'rgba(54, 162, 235, 1)',
        backgroundColor: 'rgba(54, 162, 235, 0.2)'
      },
      {
        label: 'Disk2 Usage',
        borderColor: 'rgba(255, 159, 64, 1)',
        backgroundColor: 'rgba(255, 159, 64, 0.2)'
      },
      {
        label: 'Disk3 Usage',
        borderColor: 'rgba(255, 99, 132, 1)',
        backgroundColor: 'rgba(255, 99, 132, 0.2)'
      },
      {
        label: 'Disk4 Usage',
        borderColor: 'rgba(255, 206, 86, 1)',
        backgroundColor: 'rgba(255, 206, 86, 0.2)'
      }
    ];

    const newData = {
      labels: makeDayLabels(),
      datasets: []
    };

    if (metrics && metrics.length > 0) {
      const usageList = ['cpuUsage', 'memoryUsage', 'diskUsage1'];

      ['diskUsage2', 'diskUsage3', 'diskUsage4'].forEach((usage) => {
        if (metrics[0] && metrics[0][usage] !== null) {
          usageList.push(usage);
        }
      });


      usageList.forEach(usage => {
        if (!dataArray[usage] || isRepeat == false) {
          dataArray[usage] = initializeEmptyArray(1440);
        }
      });
      hourArray = initializeEmptyArray(10);

      const pushData = (usage, index) => {
        let date = new Date();
        //반복 fetch일때(데이터 갱신 시 ) 해당하는 범위 데이터 null
        if (isRepeat === true) {
          const indexHour = date.getHours() * 60 + date.getMinutes();
          console.log(hourArray)
          for (let i = 0; i < hourArray.length; i++) {
            dataArray[usage][indexHour - 5 + i] = hourArray[i];
          }
        }
        // 가져온 데이터를 맞는 시간 index에 입력
        for (let i = 0; i < metrics.length; i++) {
          const metric = metrics[i];
          date = new Date(metric.createdDate);
          const arrayIndex = (date.getHours() * 60) + date.getMinutes();
          dataArray[usage][arrayIndex] = metric[usage];

        }
        // 각 데이터 묶음(cpuUsage, memoryUsage등)을 하나의 데이터 셋으로 만들어 추가

        newData.datasets.push({
          type: 'line',
          data: dataArray[usage],
          label: datasetConfig[index] ? datasetConfig[index].label : usage,
          borderColor: datasetConfig[index] ? datasetConfig[index].borderColor : '#000000',
          backgroundColor: datasetConfig[index] ? datasetConfig[index].backgroundColor : 'rgba(0,0,0,0.1)',
          borderWidth: 1,
          pointRadius: function (context) {
            return (context.dataIndex % 5 === 0) ? 2 : 0;
          },
          pointHoverRadius: 5,
          tension: 0.1,
          spanGaps: true
        });
      };

      for (let i = 0; i < usageList.length; i++) {
        pushData(usageList[i], i);
      }
    }

    const ctx = document.getElementById('myChart').getContext('2d');
    const chart = new Chart(ctx, {
      type: 'line',
      data: newData,
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
              onPanComplete: ({chart}) => {
                xScale.min = chart.scales.x.min;
                xScale.max = chart.scales.x.max;
              }
            },
            zoom: {
              wheel: {
                enabled: true,
              },
              mode: 'x',
              onZoomComplete: ({chart}) => {
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
                  return makeDayLabels()[index];
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
    chart.update();
  }

  //대시보드의 서버정보, 서버별 log fetch
  fetchData();
  // setInterval(fetchData, 10000);
});