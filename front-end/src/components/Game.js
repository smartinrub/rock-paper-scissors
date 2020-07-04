import React, { Component } from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import PlayView from './PlayView';
import SummaryView from './SummaryView';

class Game extends Component {

    constructor(props) {
        super(props);

        this.state = { resultSummaries: [] };
    }

    showSummaryView() {
        this.setState({isSummaryView: true})
    }

    hideSummaryView() {
        this.setState({isSummaryView: false})
    }

    render() {
        const isSummaryView = this.state.isSummaryView;
        let view;
        if(!isSummaryView) {
            view = <PlayView />
        } else {
            view = <SummaryView />
        }

        return (
            <>
                <Navbar bg="dark" variant="dark">
                    <Navbar.Brand href="#play">Rock Paper Scissors</Navbar.Brand>
                    <Nav className="mr-auto">
                    <Nav.Link href="#play" onClick={() => this.hideSummaryView()}>Play</Nav.Link>
                    <Nav.Link href="#summary" onClick={() => this.showSummaryView()}>Summary</Nav.Link>
                    </Nav>
                </Navbar>
                {view}
            </>
        )
    }
}

export default Game;
