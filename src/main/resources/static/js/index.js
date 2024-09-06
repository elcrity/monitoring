let intervalId;
let mainIntervalId;

async function fetchData() {
  try {
    const response = await fetch('/getServer');  // 서버 엔드포인트
    // todo:if #menu가 있다면 #menu 교체하기, 여기까지 됐으면 history에서는 chart만 갱신하기
    //
    if (response.ok) {
      const html = await response.text();  // 응답을 텍스트로 반환
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, 'text/html');
      const newServer = doc.querySelector('#serverTableBody');
      document.getElementById('serverTableBody').innerHTML = newServer.innerHTML;
    }else{
      const errorText = await response.text();
      window.location.href = `/errorPage?error=${encodeURIComponent(errorText)}`;
    }
  } catch (error) {
    console.log("에?러 : ",error)
    console.error('Error fetching data:', error);
  }
}

function updateIndicators() {
  const indicators = document.querySelectorAll(".statusIndicator");  // 클래스 선택

  indicators.forEach(function (indicator) {
    const cpuUsage = parseFloat(indicator.getAttribute("data-cpu-usage")) || 0;
    const memoryUsage = parseFloat(indicator.getAttribute("data-memory-usage")) || 0;
    const diskUsage = parseFloat(indicator.getAttribute("data-disk-usage")) || 0;

    let backgroundColor;
    if (cpuUsage >= 90 || memoryUsage >= 90 || diskUsage >= 90) {
      backgroundColor = '#ff0000';
    } else if (cpuUsage >= 70 || memoryUsage >= 70 || diskUsage >= 70) {
      backgroundColor = '#ffa500';
    } else if (cpuUsage > 0 || memoryUsage > 0 || diskUsage > 0) {
      backgroundColor = '#00d400';
    } else {
      backgroundColor = 'transparent'
    }
    indicator.style.backgroundColor = backgroundColor;
  });
}

function updateUsageColors() {
  function getUsageColor(usage) {
    if (usage >= 90) {
      return '#ff0000';
    } else if (usage >= 70) {
      return '#ffa500';
    } else {
      return '#00d400';
    }
  }

  // 모든 .usage 요소를 가져와서 색상 변경
  const usageElements = document.querySelectorAll('#menu .usage');

  usageElements.forEach(element => {
    const usageText = element.textContent.trim().replace('%', '');
    const usage = parseFloat(usageText);

    if (!isNaN(usage)) {
      element.style.color = getUsageColor(usage);
    }
  });
}

const closeHistory = () =>{
  const historyBody = document.getElementById('historyBody');
  historyBody.style.display = 'none';
  if(intervalId) {
    clearInterval(intervalId);
  }
}

document.addEventListener('DOMContentLoaded', async function () {
  // 화면 fetch시 실행
  await fetchData();  // 초기 데이터 로드
  updateIndicators();  // 상태 업데이트
  // fetch후 10초마다
  mainIntervalId = setInterval(async function () {
    await fetchData();
    updateIndicators();
  }, 10000); // 10초마다 데이터 업데이트
});