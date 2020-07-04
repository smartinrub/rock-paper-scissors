import React, { useEffect, useState } from 'react';
import { Table, Container } from 'react-bootstrap';

const SummaryView = () => {

    const [totalRoundsPlayed, setTotalRoundsPlayed] = useState(0);
    const [totalWinsFirstPlayers, setTotalWinsFirstPlayers] = useState(0);
    const [totalWinsSecondPlayers, setTotalWinsSecondPlayers] = useState(0);
    const [totalDraws, setTotalDraws] = useState(0);

    async function fetchData() {
        await fetch('http://localhost:8080/summary', {
            method: 'GET',
            credentials: 'include',
            headers: {
                "Access-Control-Allow-Credentials":"true"
            }
        })
        .then(response => response.json())
        .then(response => {
            setTotalRoundsPlayed(response.totalRoundsPlayed);
            setTotalWinsFirstPlayers(response.totalWinsFirstPlayers);
            setTotalWinsSecondPlayers(response.totalWinsSecondPlayers);
            setTotalDraws(response.totalDraws);
        })
        .catch(console.log)
    }

    useEffect(() => {
        fetchData();
    }, [])

    return (
        <>
            <br/>
            <Container>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Count</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>Total Rounds Played</th>
                            <td>{totalRoundsPlayed}</td>
                        </tr>
                        <tr>
                            <th>Total Wins First Players</th>
                            <td>{totalWinsFirstPlayers}</td>
                        </tr>
                        <tr>
                            <th>Total Wins Second Players</th>
                            <td>{totalWinsSecondPlayers}</td>
                        </tr>
                        <tr>
                            <th>Total Draws</th>
                            <td>{totalDraws}</td>
                        </tr>
                    </tbody>
                </Table>
            </Container>
        </>
    )
}

export default SummaryView;
