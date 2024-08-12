import React, {useEffect, useRef, useState} from 'react';
import {call} from '../util/call';
import ServerBoard from '../components/serverBoard'
import {useNavigate} from 'react-router-dom';
import '../css/dashboard.css'


// Dashboard 컴포넌트
const Dashboard = () => {
  const [selectedDiskInfo, setSelectedDiskInfo] = useState(null);
  const [mergedData, setMergedData] = useState([]);
  const selectedId = useRef(0);
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState(null);
  console.log("id : ", selectedId);

  useEffect(() => {
    fetchData();
  }, [mergedData]);

  const fetchData = async () => {
    try {
      const data = await call('/api/dashboard', 'GET');
      const metrics = await call('/log', 'POST')

      const logsMap = metrics.reduce((acc, log) => {
        acc[log.serverId] = log;
        return acc
      }, {});

      const merged = data.map(server => {
        const log = logsMap[server.serverId] || {};
        return {
          ...server,
          cpuUsage: log.cpuUsage || '',
          memoryUsage: log.memoryUsage || '',
          diskUsage: log.diskUsage1 || ''
        };
      });
      setMergedData(merged);
    } catch (error) {
      console.error('대시보드 데이터 fetch 실패:', error);
      if (error.status) {
        const errorData = await error.json();
        setErrorMessage(errorData);
      }
    }
  };

  const handleDiskClick = (disks, serverId) => {
    setSelectedDiskInfo(disks);
    selectedId.current = serverId;

  };

  const handleButtonClick = () => {
    if (selectedDiskInfo && selectedDiskInfo.length > 0) {
      navigate(`/history`, {
        state: {id: selectedId}
      });
    }
  };

  const handleDeleteClick = async (serverId) => {
    try {
      const data = await call(`/api/delete/${serverId}`, 'Delete');
      if (data && data.message) {
        alert(`${data.message}`);
        fetchData();
      }
    } catch (error) {
      const errorData = await error.json();
      alert(errorData.message);
    }
  };

  const handleRegButton = async () => {
    try {
      const data = await call('/api/regserver', 'Post');
      const log = await call('/log/start')
      if (data && data.message) {
        alert(`${data.message} \n ${log.message}`);
        fetchData();
      }
    } catch (error) {
      const errorData = await error.json();
      alert(errorData.message);
    }
  };
  if (errorMessage != null) {
    return <div>loading</div>
  }
  return (
     //전체
     <div id='container'>
       <div id='header'>
         <h1>서버 정보 목록</h1>
         <div id='header-info'>
           <div id='cpuUsage'>CPU</div>
           <div id='memoryUsage'>Memory</div>
           <div id='diskUsage'>Disk</div>
         </div>
         <button onClick={handleRegButton} className="btn btn-primary">
           등록
         </button>
       </div>

       <div id="serverTableBody">
         {mergedData.length > 0 ? (
            mergedData.map((data) => (
               <ServerBoard key={data.serverId} data={data} onDiskClick={handleDiskClick}/>
            ))
         ) : (
            <div>
              <p colSpan="9">Loading data...</p>
            </div>
         )}
       </div>

       <div id="diskInfoContainer">
         <div id="diskInfo">
           <h2>Disk Information</h2>
           <div id="diskContent">
             {selectedDiskInfo ? (
                selectedDiskInfo.map((disk, index) => (
                   <div key={index}>
                     <p>Disk ID: {disk.diskId}</p>
                     <p>Disk Name: {disk.diskName}</p>
                     <p>Created Date: {new Date(disk.createdDate).toLocaleString('ko-KR')}</p>
                   </div>
                ))
             ) : (
                <p>선택된 디스크 없음.</p>
             )}
           </div>
         </div>
         {selectedDiskInfo && selectedDiskInfo.length > 0 && (
            <div>
              <button onClick={handleButtonClick} className="btn btn-primary">
                자세히
              </button>
              <button onClick={() => handleDeleteClick(selectedId.current)} className="btn btn-primary">
                삭제
              </button>
            </div>
         )}
       </div>
     </div>
  );
};

export default Dashboard;