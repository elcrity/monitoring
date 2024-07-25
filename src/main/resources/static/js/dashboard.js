// const diskList = [[${diskList}]];
let currentServerId = null;

function fetchDiskInfo(serverId) {
  const diskInfo = diskList.find(disk => disk.diskId === serverId);
  if (diskInfo !== undefined) {
    const content = '<p>Disk ID: ' + diskInfo.diskId + '</p>'
       + '<p>Total: ' + diskInfo.diskTotal + '</p>';
    // + '<p>Used: ' + diskInfo.diskUsage + '</p>';
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