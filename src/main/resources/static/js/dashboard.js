// const diskList = [[${diskList}]];
let currentServerId = null
const metricLogList = fetch('/log/1', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ serverId: serverId })
})
   .then(response => response.json())
   .then(data => {
     console.log(data);
     // 데이터를 사용하여 필요한 작업 수행
   })
   .catch(error => console.error('Error:', error));

console.log(metricLogList);

function fetchDiskInfo(serverId) {
  const disks = diskList.filter(disk => disk.diskServerInfoFk === serverId);
  if (disks.length > 0) {
    let content = '';
    disks.forEach(diskInfo => {
      content += '<p>Disk ID: ' + diskInfo.diskId + '</p>'
         + '<p>Total: ' + diskInfo.diskTotal + '</p>'
         + '<p>Used: ' + diskInfo.diskUsage + '</p>'; // 사용량 정보 추가
    });
    document.getElementById('diskContent').innerHTML = content;
  } else {
    document.getElementById('diskContent').innerHTML = '이용 가능한 디스크 없음.';
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
    form.action = `/detail/${currentServerId}`;
    form.method = 'post';

    const button = document.createElement('button');
    button.type = 'submit';
    button.textContent = '상세 정보 보기';

    form.appendChild(button);
    buttonContainer.appendChild(form);
  }
}

function getMetricLogByServerId(serverId) {
  return metricLogList.find(log => log.serverId === serverId) || {};
}