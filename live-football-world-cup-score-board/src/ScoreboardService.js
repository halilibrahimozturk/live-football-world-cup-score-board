import axios from 'axios';

const API_URL = 'http://localhost:8080/api/scoreboard';

export const startMatch = async (homeTeam, awayTeam) => {
    try {
        await axios.post(`${API_URL}/start`, null, {
            params: { homeTeam, awayTeam }
        });
    } catch (error) {
        return error.response?.data?.message || 'Error starting match';
    }
};

export const updateScore = async (homeTeam, awayTeam, homeScore, awayScore) => {
    try {
        await axios.post(`${API_URL}/update`, null, {
            params: { homeTeam, awayTeam, homeScore, awayScore }
        });
    } catch (error) {
        return error.response?.data?.message || 'Error updating score';
    }
};

export const finishMatch = async (homeTeam, awayTeam) => {
    try {
        await axios.post(`${API_URL}/finish`, null, {
            params: { homeTeam, awayTeam }
        });
    } catch (error) {
        return error.response?.data?.message || 'Error finishing match';
    }
};

export const getSummary = async () => {
    try {
        const response = await axios.get(`${API_URL}/summary`);
        return response.data;
    } catch (error) {
        return 'Error fetching summary';
    }
};
