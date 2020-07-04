import React, { Component } from 'react';
import PlayView from './PlayView';

class Game extends Component {

    constructor(props) {
        super(props);

        this.state = { resultSummaries: [] };
    }

    render() {

        return (
            <PlayView />
        )
    }
}

export default Game;
