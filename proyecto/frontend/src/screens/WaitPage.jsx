import React, { useState, useEffect } from 'react'

const WaitPage = (props) => {
    const params = props.match.params

    const wsURL = "ws://localhost:8080/wstimer"
    const ws = React.createRef() //Make sure it doesn't get initialized each time a render is done

    const shortURL = params.id
    const [longURL,setlongURL] = useState("")

    const redirectFromURL = async() => {
        try {
            window.location.replace(longURL)
        }catch(err){
            console.log(err)
        }
    }

    //Run on module (Really websocket) load
    useEffect(() => {
        ws.current = new WebSocket(wsURL);
        console.log("UseEffect")

        ws.current.onopen = (event) => {
            console.log("ws.onopen");
            ws.current.send(shortURL)
        }
    
        ws.current.onmessage = (event) => {
            setlongURL(event.data)
            console.log("OnMessage: '" + event.data + "'")
        }
    }, []); //Warning here, ignore

    return (
        <section className="content">
            Waiting 10 seconds to give you the link...
            
            <p>[DEBUG] Short URL ID = {shortURL}</p>
            { longURL !== "" //Show redirect button when we've received the URL from the server
                ? <button type="button" className="btn" onClick={redirectFromURL}>Ir a '{longURL}'</button>
                : null
            }
        </section>
    );
}

export default WaitPage;