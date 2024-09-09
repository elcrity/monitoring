let timeLabel;
let isRepeat = false;
let histories = [];
let selectedId;
let timeDelay = 10000;
// draw시, 배열의 크기만큼 갱신
let lastedDraw = 0;
let dataKeySet = ['cpuUsage', 'memoryUsage', 'diskUsage1'];

// 서버 삭제시
async function deleteServer(serverId) {
  const confirmed = confirm('서버를 삭제하시겠습니까?');
  if (!confirmed) return;

  try {
    const response = await fetch(`/api/delete/${serverId}`, {
      method: 'DELETE',
    });

    if (response.ok) {
      // 서버 응답이 성공적이면 해당 항목을 화면에서 제거
      document.getElementById(`deleteButton-${serverId}`).closest('#serverTable').remove();
    } else {
      const errorText = await response.text();
      window.location.href = `/errorPage?error=${encodeURIComponent(errorText)}`;
    }
  } catch (error) {
    console.error('삭제 실패:', error);
  }
}

//로그 데이터 fetch
const callDrawData = async (serverId, repeated, date = null) => {
  // const selectedDay = date ? date : formatDateToLocalDateTime(new Date());
  const selectedDay = date;
  // console.log("called Drawdata : " , JSON.stringify({
  //   serverId,
  //   isRepeat : Boolean(repeated),
  //   date : selectedDay // 날짜 추가
  // }))
  try {
    const serverHistory = await fetch('/getHistory', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        serverId,
        isRepeat: Boolean(repeated),
        date: selectedDay // 날짜 추가
      })
    }).then(async (response) => {
      if (response.ok) {
        return response.json()
      } else {
        const errorText = await response.text();
        window.location.href = `/errorPage?error=${encodeURIComponent(errorText)}`;
      }
    })
    if (serverHistory.length > 0) {
      timeLabel = makeAxis(serverHistory);
    }
    await drawChart(serverHistory, timeLabel, repeated);
  } catch (error) {
    console.error('데이터를 처리하는 중 오류가 발생했습니다:', error);
    alert('데이터를 처리하는 중 오류가 발생했습니다.');
  }
}

//history 메뉴 데이터 fetch
const callHistory = async (serverId, repeated) => {
  try {
    const response = await fetch(`/getLogs/${serverId}`, {
      method: 'GET',
    });

    if (response.ok) {
      const historyBody = document.getElementById('historyBody');
      if (historyBody) {
        const html = await response.text();
        // 기존 HTML 내용과 비교하여 변경된 부분만 업데이트
        if (!repeated) {
          historyBody.innerHTML = html;
          // console.log(html)
        } else {
          // 데이터를 가졍로때 repeated면 #menu에 있는 내용만 교체하기
          const parser = new DOMParser();
          const doc = parser.parseFromString(html, 'text/html');
          // 새로운 HTML에서 #menu 요소를 가져오기
          const newMenu = doc.getElementById('menu');
          const oldMenu = document.getElementById('menu');
          oldMenu.innerHTML = newMenu.innerHTML;
        }

        // Display block 상태 유지
        if (historyBody.style.display !== 'block') {
          historyBody.style.display = 'block';
        }
        updateUsageColors();

      }
    } else {
      const errorText = await response.text();
      window.location.href = `/errorPage?error=${encodeURIComponent(errorText)}`;
    }
  } catch (error) {
    console.error('히스토리를 가져오는데 에러가 발생:', error);
  }
};

// 서버 클릭시
const showHistory = async (serverId) => {
  dataKeySet = ['cpuUsage', 'memoryUsage', 'diskUsage1'];
  clearInterval(mainIntervalId);
  clearInterval(intervalId);

  selectedId = serverId;
  isRepeat = false;
  xScale.min = 0;
  xScale.max = 1440;
  await callHistory(serverId).then(() => callDrawData(serverId, isRepeat, selectedDate));

  const elems = document.querySelectorAll('.datepicker_input');
  elems.forEach(elem => regDatepicker(elem));
  // 이미 intervalId가 설정되어 있으면 추가로 설정하지 않음

  // 서버 정보 클릭시 기존의 메인 화면 갱신하는 interval을 정지 후 history를 갱신하는 코드와 동기화
  mainIntervalId = setInterval(async () => {
    await fetchData();
    updateIndicators();
  }, timeDelay);
  intervalId = setInterval(async () => {
    isRepeat = true;
    await callHistory(serverId, isRepeat);
    await callDrawData(serverId, isRepeat, selectedDate)
    updateUsageColors();
  }, timeDelay);
}
