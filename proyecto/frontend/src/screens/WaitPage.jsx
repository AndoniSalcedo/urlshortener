import React, { useState, useContext, useEffect } from 'react'

const WaitPage = (props) => {
    const params = props.match.params

    const wsURL = "ws://localhost:8080/wstimer"
    const ws = React.createRef() //Make sure it doesn't get initialized each time a render is done

    const shortURL = params.id
    const unshorteredURL = ""

    const firstFunctionRun = false


    //Run on module (Really websocket) load
    useEffect(() => {
        const firstFunctionRun = true
        ws.current = new WebSocket(wsURL);
        //ws.send(shortURL)
        console.log("UseEffect")

        ws.current.onopen = (event) => {
            if(firstFunctionRun) {
                console.log("ws.onopen");
                ws.current.send(shortURL)
            }
        }
    
        ws.current.onmessage = (event) => {
            if(firstFunctionRun) {
                const unshorteredURL = event.data
                console.log("OnMessage: '" + unshorteredURL + "'")
            }
        }
    }, []);

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