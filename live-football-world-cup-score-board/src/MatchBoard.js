import React, { useEffect, useState } from 'react';
import { startMatch, updateScore, finishMatch, getSummary } from './ScoreboardService';
import { Button, Input, Spinner, Alert } from 'reactstrap';
import './styles.css';  // Import your styles here

const MatchBoard = () => {
    const [matches, setMatches] = useState([]);
    const [homeTeam, setHomeTeam] = useState('');
    const [awayTeam, setAwayTeam] = useState('');
    const [homeScore, setHomeScore] = useState(0);
    const [awayScore, setAwayScore] = useState(0);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const fetchMatches = async () => {
        setLoading(true);
        const summary = await getSummary();
        setLoading(false);
        if (summary.includes('Error')) {
            setError(summary);
        } else {
            setMatches(summary);
        }
    };

    useEffect(() => {
        fetchMatches();
    }, []);

    const handleStartMatch = async () => {
        setLoading(true);
        const errorMessage = await startMatch(homeTeam, awayTeam);
        setLoading(false);
        if (errorMessage) {
            setError(errorMessage);
            setTimeout(() => setError(''), 3000); // Hide the error after 3 seconds
        } else {
            fetchMatches();
        }
    };

    const handleUpdateScore = async (home, away) => {
        setLoading(true);
        const errorMessage = await updateScore(home, away, homeScore, awayScore);
        setLoading(false);
        if (errorMessage) {
            setError(errorMessage);
            setTimeout(() => setError(''), 3000); // Hide the error after 3 seconds
        } else {
            fetchMatches();
        }
    };

    const handleFinishMatch = async (home, away) => {
        setLoading(true);
        const errorMessage = await finishMatch(home, away);
        setLoading(false);
        if (errorMessage) {
            setError(errorMessage);
            setTimeout(() => setError(''), 3000); // Hide the error after 3 seconds
        } else {
            fetchMatches();
        }
    };

    const matchExists = (home, away) => {
        return matches.some((match) => match.homeTeam === home && match.awayTeam === away);
    };

    const matchInProgress = (home, away) => {
        const match = matches.find((m) => m.homeTeam === home && m.awayTeam === away);
        return match && !match.finished;
    };

    return (
        <div className="match-board">
            {error && <Alert color="danger">{error}</Alert>}
            {loading && <Spinner color="primary" />}
            <div className="team-inputs">
                <Input
                    type="text"
                    placeholder="Home Team"
                    onChange={(e) => setHomeTeam(e.target.value)}
                    value={homeTeam}
                    className="team-input"
                />
                <Input
                    type="text"
                    placeholder="Away Team"
                    onChange={(e) => setAwayTeam(e.target.value)}
                    value={awayTeam}
                    className="team-input"
                />
                {!matchExists(homeTeam, awayTeam) ? (
                    <Button color="success" onClick={handleStartMatch} className="action-button">Start Match</Button>
                ) : (
                    <Button color="info" onClick={() => handleUpdateScore(homeTeam, awayTeam)} className="action-button">
                        Update Score
                    </Button>
                )}
            </div>
            <div className="score-inputs">
                <Input
                    type="number"
                    placeholder="Home Score"
                    value={homeScore}
                    onChange={(e) => setHomeScore(e.target.value)}
                    className="score-input"
                />
                <Input
                    type="number"
                    placeholder="Away Score"
                    value={awayScore}
                    onChange={(e) => setAwayScore(e.target.value)}
                    className="score-input"
                />
            </div>
            <div className="matches-list">
                {matches.map((match) => (
                    <div key={match.homeTeam + match.awayTeam} className="match-card">
                        <h3>{match.homeTeam} vs {match.awayTeam}</h3>
                        <div className="score">Score: {match.homeScore} - {match.awayScore}</div>
                        {/* Show Update Score and Finish Match for matches that are in progress */}
                        {!match.finished && (
                            <>
                                <Button color="danger" onClick={() => handleFinishMatch(match.homeTeam, match.awayTeam)} className="action-button">
                                    Finish Match
                                </Button>
                            </>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MatchBoard;
