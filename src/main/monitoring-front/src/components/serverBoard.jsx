import React, {useEffect, useState} from 'react'
import '../css/serverboard.css';

const ServerBoard = ({ data, onDiskClick }) => {
  const {
    serverId, serverOs, serverHostname,
    memoryTotal, purpose, serverIp,
    cpuUsage, memoryUsage, diskUsage, disks
  } = data;

  const [statusIndicatorColor, setStatusIndicatorColor] = useState('transparent');

  useEffect(() => {
    const maxUsage = Math.max(cpuUsage || 0, memoryUsage || 0, diskUsage || 0);

    if (maxUsage >= 90) {
      setStatusIndicatorColor('#ff0000'); // 빨간색
    } else if (maxUsage >= 70) {
      setStatusIndicatorColor('#ffa500'); // 오렌지색
    } else {
      setStatusIndicatorColor('transparent'); // 초기 상태
    }
  }, [cpuUsage, memoryUsage, diskUsage]);

  return (
     <div id='serverTable' key={serverId} onClick={() => onDiskClick(disks, serverId)}>
       <div id='serverTable-header'>
         <div>{serverHostname}</div>
         <div id='statusIndicator' style={{ backgroundColor: statusIndicatorColor }}></div>
         <div>{serverIp}</div>
       </div>
       {cpuUsage > 0 && (
          <div id='cpuUsageContainer'>
            <div id="cpuUsage" style={{ width: `${cpuUsage || 0}%` }}></div>
            <div>{cpuUsage}%</div>
          </div>
       )}
       {memoryUsage > 0 && (
          <div id='memoryUsageContainer'>
            <div id="memoryUsage" style={{ width: `${memoryUsage || 0}%` }}></div>
            <div>{memoryUsage}%</div>
          </div>
       )}
       {diskUsage > 0 && (
          <div id='diskUsageContainer'>
            <div id="diskUsage" style={{ width: `${diskUsage || 0}%` }}></div>
            <div>{diskUsage}%</div>
          </div>
       )}
     </div>
  );
};

export default ServerBoard;