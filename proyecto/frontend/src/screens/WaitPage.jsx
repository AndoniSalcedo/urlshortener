import React, { useState, useEffect } from 'react'

import axios from 'axios'

const WaitPage = (props) => {
    const params = props.match.params

    const wsURL = "ws://localhost:8080/wstimer"
    const ws = React.createRef() //Make sure it doesn't get initialized each time a render is done

    const shortURL = params.id
    const [longURL,setlongURL] = useState("")

    const [ad1, setAd1] = useState("")
    const [ad2, setAd2] = useState("")

    const redirectFromURL = async() => {
        try {
            window.location.replace(longURL)
        }catch(err){
            console.log(err)
        }
    }

    const loadAds = async() => {
        try {
            const res = await axios.post("http://localhost:8080/ad/obtain",{})
            setAd1(res.data.ad1)
            setAd2(res.data.ad2)
            console.log("ad1: \"" + res.data.ad1 + "\"")
            console.log("ad2: \"" + res.data.ad2 + "\"")
        } catch(err) {
            console.log(err)
        }
    }

    //Run on module (Really websocket) load
    useEffect(() => {
        ws.current = new WebSocket(wsURL);
        console.log("UseEffect")

        loadAds()

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
        <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gridGap: 20 }}>
            <div>
                {
                    ad1 !== "" ? <a href="https://en.wikipedia.org/wiki/Online_advertising"><img src={ad1} alt="Advertisement"></img></a> : null
                }
            </div>
            <div>
                <section className="wait-page">
                Waiting 10 seconds to give you the link...
                
                <p>[DEBUG] Short URL ID = {shortURL}</p>
                { longURL !== "" //Show redirect button when we've received the URL from the server
                    ? <button type="button" className="btn" onClick={redirectFromURL}>Ir a '{longURL}'</button>
                    : null
                }
                </section>
            </div>
            <div>
                {
                    ad2 !== "" ? <a href="https://en.wikipedia.org/wiki/Online_advertising"><img src={ad2} alt="Advertisement"></img></a> : null
                }
            </div>
        </div>
        
    );
}

export default WaitPage;