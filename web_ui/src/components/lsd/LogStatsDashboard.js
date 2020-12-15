import React, { useState, useEffect, useMemo, useRef } from 'react';

import {getLogStats} from './api';

function LogStatsDashboard() {
  
  //const [duration, setDuration] = useState(5000); // default 5555 millis.
  const [duration, setDuration] = useState(process.env.REACT_APP_DEFAULT_LOG_DURATION); // default 5555 millis.
  const [refreshDuration, setRefreshDuration] = useState(1); // default 1 sec.
  const [stat, setStat] = useState({});
  const [lastRefreshTime, setLastRefreshTime] = useState();

  console.log('process.env.REACT_APP_API_BASE_URL : ' + process.env.REACT_APP_API_BASE_URL);
  console.log('process.env.REACT_APP_DEFAULT_LOG_DURATION : ' + process.env.REACT_APP_DEFAULT_LOG_DURATION);

  useEffect(() => {
    const intervalId = setInterval(() => {
      getLogStats(duration).then(data => {
        setStat(data);
        setLastRefreshTime(new Date());
      }).catch(console.error);
    }, refreshDuration * 1000);
    return () => clearInterval(intervalId);
  }, [duration, refreshDuration]);

  return (
    <div>
       <dl>
        <dt>Total</dt>
        <dd>{stat.total}</dd>
        <dt>Info</dt>
        <dd>{stat.info}</dd>
        <dt>Warning</dt>
        <dd>{stat.warning}</dd>
        <dt>Error</dt>
        <dd>{stat.error}</dd>
       </dl>

        {lastRefreshTime && <strong>Last Refresh Time: {lastRefreshTime.toLocaleString()}</strong>}

        <div>
          <label htmlFor="logDuration">Log Duration (milliseconds - min 1000): </label>
          <input  type="number" 
                  id="logDuration" 
                  name="logDuration" 
                  step="500"
                  min="1000"
                  value={duration} 
                  onChange={e => setDuration(Math.round(e.target.value))}
          />
        </div>
        <div>
          <label htmlFor="refreshDuration">Refresh Duration (seconds - min=1 max=10): </label>
          <input  type="number" 
                  id="refreshDuration" 
                  name="refreshDuration" 
                  step="1"
                  min="1"
                  max="10"
                  value={refreshDuration} 
                  onChange={e => setRefreshDuration(Math.round(e.target.value))}
          />
        </div>
    </div>
  );
}

export default LogStatsDashboard;
