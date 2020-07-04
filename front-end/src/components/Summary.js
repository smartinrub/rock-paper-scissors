import React from 'react'
import { Table } from 'react-bootstrap';

const Summary = ({resultSummaries}) => {
    return (
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th>Player 1</th>
                    <th>Player 2</th>
                    <th>Result</th>
                </tr>
            </thead>
            <tbody>
                {resultSummaries.length > 0 ? resultSummaries.map((item) => 
                    <tr key={item.roundId}>
                        <td>{item.playerOneChoice}</td>
                        <td>{item.playerTwoChoice}</td>
                        <td>{item.result}</td>
                    </tr>
                ): []}
            </tbody>
        </Table>
    )
}

export default Summary;
