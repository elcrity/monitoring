import React from 'react'
import '../css/serverboard.css';

const ServerBoard = ({ data, onDiskClick, onDeleteClick  }) => {
    const {
        serverId, serverOs, serverHostname,
        memoryTotal, purpose, serverIp,
        cpuUsage, memoryUsage, diskUsage, disks
    } = data;

    return (
        <div id='serverTable' key={serverId} onClick={() => onDiskClick(disks, serverId)}>
            <div>
                <div>{serverHostname}</div>
                <div>{serverIp}</div>
            </div>
            <div id="cpuUsage">{cpuUsage}</div>
            <div id="memoryUsage">{memoryUsage}</div>
            <div id="diskUsage">{diskUsage}</div>
        </div>
    );
};

export default ServerBoard