import React, { useEffect, useState } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { call } from '../util/call';

const History = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { id } = location.state || {}; // params 객체에서 id를 추출합니다.
    const [server, setServer] = useState([]);
    const [metrics, setMetrics] = useState([]);
    const [errorMessage, setErrorMessage] = useState(null);
    const [loading, setLoading] = useState(true);
    
    
    useEffect(() => {
        if (!id) {
            navigate('/'); // id가 없으면 홈으로 리디렉션
            return;
        }
        const fetchData = async () => {
            try {
                console.log("id: " , id);
                const data = await call(`/api/history/${id.current}`, 'POST');
                const logs = await call(`/log/history/${id.current}`, 'POST')
                setServer(data);
                setMetrics(logs);
                setLoading(true);
            } catch (error) {
                if (error.status != null) {
                    const errorData = await error.json();
                    setErrorMessage(errorData);
                }
            }finally{
                setLoading(false);
            }
        };

        fetchData();
    }, [id])

    const handleButtonClick = () => {
        navigate(`/`);
    };
    if (loading) {
        return <div>데이터를 가져오는 중...</div>; // 로딩 중일 때 표시할 내용
    }

    if (errorMessage) {
        return (<div>
            <p>{errorMessage.message}</p>
            <p>{errorMessage.status}</p>
            <p>{errorMessage.name}</p>
            <p>{errorMessage.code}</p>
        </div>)
    } else {
        return (
            <div>
                <button onClick={handleButtonClick} className="btn btn-primary">
                    이전
                </button>
                <h1>서버 히스토리</h1>

                {/* 서버 정보 , /api/history/{serverId}*/}
                <h2 id="serverInfoDiv">Server Info</h2>
                <p><strong>Server ID:</strong> {server.serverId || ''}</p>
                <p><strong>Operating System:</strong> {server.serverOs || ''}</p>
                <p><strong>Hostname:</strong> {server.serverHostname || ''}</p>
                <p><strong>Purpose:</strong> {server.purpose || ''}</p>
                <p><strong>IP Address:</strong> {server.serverIp || ''}</p>

                {/* 로그정보 ,/log/history/{serverId} */}
                <h2>Metric Logs</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Log Index</th>
                            <th>CPU Usage</th>
                            <th>Memory Usage</th>
                            <th>Disk Usage 1</th>
                            <th>Disk Usage 2</th>
                            <th>Disk Usage 3</th>
                            <th>Disk Usage 4</th>
                            <th>Disk Total 1</th>
                            <th>Disk Total 2</th>
                            <th>Disk Total 3</th>
                            <th>Disk Total 4</th>
                            <th>Disk Name 1</th>
                            <th>Disk Name 2</th>
                            <th>Disk Name 3</th>
                            <th>Disk Name 4</th>
                            <th>Created Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        {metrics.map((log, index) => (
                            <tr key={log.logId}>
                                <td>{index}</td>
                                <td>{`${log.cpuUsage}%` || ''}</td>
                                <td>{`${log.memoryUsage}%` || ''}</td>
                                <td>{log.diskUsage1 != null ? `${log.diskUsage1}%` : ''}</td>
                                <td>{log.diskUsage2 != null ? `${log.diskUsage2}%` : ''}</td>
                                <td>{log.diskUsage3 != null ? `${log.diskUsage3}%` : ''}</td>
                                <td>{log.diskUsage3 != null ? `${log.diskUsage4}%` : ''}</td>
                                <td>{`${(log.diskTotal1 / 1024).toFixed(2)}GB` || ''}</td>
                                <td>{log.diskTotal2 || ''}</td>
                                <td>{log.diskTotal3 || ''}</td>
                                <td>{log.diskTotal4 || ''}</td>
                                <td>{log.diskName1 || ''}</td>
                                <td>{log.diskName2 || ''}</td>
                                <td>{log.diskName3 || ''}</td>
                                <td>{log.diskName4 || ''}</td>
                                <td>{new Date(log.createdDate).toLocaleString('ko-KR') || ''}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        );
    }
};

export default History;