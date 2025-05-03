import logo from './logo.svg';
import './App.css';
import React from 'react';
import MatchBoard from './MatchBoard';
function App() {
  return (
      <div className="App">
        <h1>Live Football World Cup Scoreboard</h1>
        <MatchBoard />
      </div>
  );
}

export default App;
