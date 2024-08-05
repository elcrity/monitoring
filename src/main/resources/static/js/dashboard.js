let currentServerId = null;

function fetchDiskInfo(serverId) {
  const disks = diskList.filter(disk => disk.diskServerInfoFk === serverId);
  const diskContent = document.getElementById('diskContent');

  if (disks.length > 0) {
    let content = '';
    disks.forEach(diskInfo => {
      content += `<p>Disk ID: ${diskInfo.diskId}</p>`
         + `<p>Total: ${diskInfo.diskName}</p>`
    });
    diskContent.innerHTML = content;
  } else {
    diskContent.innerHTML = '이용 가능한 디스크 없음.';
  }
}

function handleRowClick(serverId) {
  currentServerId = serverId;
  fetchDiskInfo(serverId);
  updateButton();
}

function updateButton() {
  const buttonContainer = document.querySelector('.button-container');
  buttonContainer.innerHTML = ''; // Clear existing buttons

  if (currentServerId !== null) {
    const form = document.createElement('form');
    form.action = `/history/${currentServerId}`;
    form.method = 'post';

    const button = document.createElement('button');
    button.type = 'submit';
    button.textContent = '상세 정보 보기';

    form.appendChild(button);
    buttonContainer.appendChild(form);
  }
}

function getMetricLogByServerId(serverId) {
  // This function should fetch the metrics for the server and return it.
  // For the purpose of this example, it is assumed that metricLogList is populated with necessary data.
  return metricLogList.find(log => log.serverId === serverId) || {};
}

// Fetch metrics for the initial server (this should be adapted based on actual implementation)
fetch('/log/1', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ serverId: currentServerId })
})
   .then(response => response.json())
   .then(data => {
     console.log(data);
     // Update your metricLogList or use data as needed
   })
   .catch(error => console.error('Error:', error));