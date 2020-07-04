import React, { Component } from 'react';
import { Button, Container, Jumbotron, Row, Col } from 'react-bootstrap';
import Summary from './Summary';


class PlayView extends Component {

    constructor(props) {
        super(props);

        this.state = { resultSummaries: [] };
    }

    playRound() {
        fetch('http://localhost:8080/play', {
            method: 'POST',
            credentials: 'include',
            headers: {
                "Access-Control-Allow-Credentials":"true"
            }
        })
        .then(() => {
            this.getRoundsSummary();
        })
        .catch(console.log)
    }

    getRoundsSummary() {
        fetch('http://localhost:8080/rounds-summary', {
            method: 'GET',
            credentials: 'include',
            headers: {
                "Access-Control-Allow-Credentials":"true"
            }
        })
        .then(response => response.json())
        .then((data) => {
          this.setState({ resultSummaries: data });
        })
        .catch(console.log)
    }

    restartGame() {
        fetch('http://localhost:8080/restart', {
            method: 'DELETE',
            credentials: 'include',
            headers: {
                "Access-Control-Allow-Credentials":"true"
            }
        }).then(response => {
            if(response.status === 204){
                this.setState({ resultSummaries: [] });
            }
        })
        .catch(console.log)
    }

    componentDidMount() {
        this.getRoundsSummary();
    }

    render() {
        return(
            <>
                <Jumbotron>
                <h1>Rock Paper Scissors</h1>
                <p>
                    Click on "Play Round" to start playing! To restart the game click on "Restart".
                </p>
                <Row>
                    <Col>
                        <Button size="lg" variant="success" onClick={() => this.playRound()}>Play Round</Button>
                    </Col>
                    <Col>
                        <Button disabled={this.state.resultSummaries.length<1} size="lg" variant="danger" onClick={() => this.restartGame()}>Restart Game</Button>
                    </Col>
                    <Col>
                        <h3>Rounds Played: {this.state.resultSummaries.length}</h3>
                    </Col>
                </Row>
                </Jumbotron>
                <Container>
                    <Summary resultSummaries={this.state.resultSummaries} />
                </Container>
            </>
        )
    }
}

export default PlayView;