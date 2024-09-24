let timeLabel;
let isRepeat = false;
let histories = [];
let selectedId;

// draw시, 배열의 크기만큼 갱신
let dataKeySet = ['cpuUsage', 'memoryUsage', 'diskUsage1'];
let selectedDate;
const intervalIdMap = new Map();

// 서버 삭제시
async function deleteServer(event, serverId) {
  event.stopPropagation();
  const confirmed = confirm('서버를 삭제하시겠습니까?');
  if (!confirmed) return;
  // 선택된 serverId가 map에 있다면 해당하는 interval을 정지한 후, 화면에서 히스토리 제거
  if (intervalIdMap.has(serverId)) {
    clearInterval(intervalIdMap.get(serverId));
    const historyBody = document.getElementById('historyBody');
    historyBody.style.display = 'none';
  }
  try {
    const response = await fetch(`/api/delete/${serverId}`, {
      method: 'DELETE',
    });
    if (response.ok) {
      // 서버 응답이 성공적이면 해당 항목을 화면에서 제거
      document.getElementById(`deleteButton-${serverId}`).closest('#serverTable').remove();
    } else {
      const errorText = await response.text();
      console.log(errorText)
      window.location.href = `/errorPage?error=${encodeURIComponent(errorText)}`;
    }
  } catch (error) {
    console.error('삭제 실패:', error);
  }
}

//로그 데이터 fetch
const callDrawData = async (serverId, repeated, date = null) => {
  // const selectedDay = date ? date : formatDateToLocalDateTime(new Date());
  const selectedDay = date !== null ? date : new Date();
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
    timeLabel = makeAxis(selectedDay);
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
  // 클릭시 intervalMap을 돌며 현재 선택한 serverId와 다른 키값을 가진 map의 interval을 정지 후, 제거
  // 이전에 선택된 interval을 제거하기 위함.
  intervalIdMap.forEach((intervalId, mapServerId) => {
      clearInterval(intervalIdMap.get(mapServerId));
      intervalIdMap.delete(selectedId)
  });

  selectedDate = null;
  selectedId = serverId;
  isRepeat = false;
  xScale.min = 0;
  xScale.max = 1440;

  await callHistory(serverId).then(() => callDrawData(serverId, isRepeat, selectedDate));

  // datePicker 추가
  const elems = document.querySelectorAll('.datepicker_input');
  elems.forEach(elem => regDatepicker(elem));

  // 메인 화면 갱신 interval
  mainIntervalId = setInterval(async () => {
    await fetchData();
    updateIndicators();
  }, timeDelay);

  // 서버별 히스토리 갱신 interval
  const newIntervalId = setInterval(async () => {
    isRepeat = true;
    await callHistory(serverId, isRepeat);
    await callDrawData(serverId, isRepeat, selectedDate);
    updateUsageColors();
  }, timeDelay);

  // serverId를 키로 하고 intervalId를 Map에 저장
  intervalIdMap.set(serverId, newIntervalId);
};
