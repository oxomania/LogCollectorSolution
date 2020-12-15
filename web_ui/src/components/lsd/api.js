import call from '../../helpers/call';

export const getLogStats = async duration => call(`log-stats/${duration}`);