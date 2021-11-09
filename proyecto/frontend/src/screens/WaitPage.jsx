import React, { useState, useContext, useEffect } from 'react'

const WaitPage = (props) => {
    const params = props.match.params

    const wsURL = "ws://localhost:8080/wstimer"
    const ws = new WebSocket(wsURL) //Initalize Websocket

    const shortURL = params.id
    const unshorteredURL = ""

    const petitionSent = false


    ws.onopen = (event) => {
        console.log("ws.onopen (1)");
        if(!petitionSent) {
            const petitionSent = true
            ws.send(shortURL)
            console.log("ws.onopen (2)");
        }
    }

    ws.onmessage = (event) => {
        const unshorteredURL = event.data
    }

    //Run on module (Really websocket) load
    useEffect(() => {
        //ws.send(shortURL)
        console.log("UseEffect")
    });

    const retrieveURL = async () => {
        const unshorteredURL = "Your URL: http://www.google.com"
    }

    return (
        <section className="content">
            Waiting 10 seconds to give you the link...
            
            <p>[DEBUG] Short URL ID = {shortURL}</p>
            <p>{unshorteredURL}</p>
        </section>
    );
}

export default WaitPage;