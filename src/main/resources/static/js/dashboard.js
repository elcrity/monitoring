document.addEventListener('DOMContentLoaded', () => {
  fetchServerData();
});

function fetchServerData() {
  fetch('/api/dashboard', { method: 'GET' })
     .then(response => response.json())
     .then(data => {
       serverList = data;
       populateServerTable(serverList);
       startMetricUpdate(); // 메트릭 업데이트를 시작합니다.
     })
     .catch(error => console.error('데이터 fetch 에러:', error));
}

function populateServerTable(data) {
  const tableBody = document.getElementById('serverTableBody');
  tableBody.innerHTML = ''; // 기존 내용을 지웁니다.

  data.forEach(server => {
    const row = document.createElement('tr');
    row.setAttribute('data-server-id', server.serverId);
    row.setAttribute('onclick', `handleRowClick(${server.serverId})`);

    row.innerHTML = `
            <td>${server.serverId}</td>
            <td>${server.serverOs}</td>
            <td>${server.serverHostname}</td>
            <td>${server.memoryTotal}</td>
            <td>${server.purpose}</td>
            <td>${server.serverIp}</td>
            <td>Loading...</td>
            <td>Loading...</td>
            <td>Loading...</td>
        `;

    tableBody.appendChild(row);
  });
}

function startMetricUpdate() {
  function updateMetrics() {
    fetch('/log', { method: 'POST' })
       .then(response => response.json())
       .then(metrics => {
         metricLogs = metrics;
         serverList.forEach(server => {
           const serverMetrics = metricLogs.filter(metric => metric.serverId === server.serverId);
           if (serverMetrics.length > 0) {
             const latestMetric = serverMetrics[0]; // 최신 메트릭 로그
             updateMetricRow(server.serverId, latestMetric);
           }
         });
       })
       .catch(error => console.error('Error fetching metrics:', error));
  }

  // 초기 메트릭 업데이트
  updateMetrics();

  // 1분마다 메트릭을 업데이트합니다
  setInterval(updateMetrics, 10000); // 60000ms = 1분
}

function updateMetricRow(serverId, metric) {
  const row = document.querySelector(`tr[data-server-id="${serverId}"]`);
  if (row) {
    row.children[6].textContent = `${metric.cpuUsage}%`; // CPU Usage
    row.children[7].textContent = `${metric.memoryUsage}%`; // Memory Usage
    row.children[8].textContent = `${metric.diskUsage1}%`; // Disk Usage (여기서 1번 디스크 사용량 예시로 사용)
  }
}

function handleRowClick(serverId) {
  const server = serverList.find(server => server.serverId === serverId);
  if (server) {
    fetchDiskInfo(server);
    updateButton(serverId);
  }
}

function fetchDiskInfo(server) {
  const diskContent = document.getElementById('diskContent');
  if (server.disks.length > 0) {
    let content = '';
    server.disks.forEach(disk => {
      content += `<p>Disk ID: ${disk.diskId}</p>
                        <p>Total: ${disk.diskName}</p>`;
    });
    diskContent.innerHTML = content;
  } else {
    diskContent.innerHTML = '이용 가능한 디스크 없음.';
  }
}

function updateButton(serverId) {
  // const buttonContainer = document.querySelector('.button-container');
  // buttonContainer.innerHTML = ''; // 기존 버튼을 지웁니다.
  //
  // const form = document.createElement('form');
  // form.action = `/history/${serverId}`;
  // form.method = 'get';
  //
  // const button = document.createElement('button');
  // button.type = 'submit';
  // button.textContent = '상세 정보 보기';
  //
  // form.appendChild(button);
  // buttonContainer.appendChild(form);
  const buttonContainer = document.querySelector('.button-container');
  buttonContainer.innerHTML = ''; // 기존 버튼을 지웁니다.

  const button = document.createElement('button');
  button.type = 'button'; // 폼을 사용하지 않고 직접 JavaScript로 처리합니다.
  button.textContent = '상세 정보 보기';
  button.addEventListener('click', function() {
    // 쿼리 파라미터로 serverId를 추가하여 history.html로 리디렉션합니다.
    window.location.href = `/history/${encodeURIComponent(serverId)}`;
  });

  buttonContainer.appendChild(button);
}