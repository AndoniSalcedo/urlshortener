import React, { useState } from 'react'

import axios from 'axios'

import history from "../history"

const UrlPage = () => {
    const [urlToShort,seturlToShort] = useState("")
    
    const [urlShorted, setUrlShorted]= useState("Not url shorted yet")

    const [qr, setQr] = useState("")

    const shortUrl = async () => {
        try{
            const res = await axios.post("http://localhost:8080/api/shorter",{
                url: urlToShort,
            })
            setUrlShorted("http://localhost:3000/s/"+res.data.url)
            console.log(res.data)
            console.log(res.data.url)
            console.log(urlShorted)
        }catch(err){
            console.log(err)
        }
    }

    const getQr = async () => {
                try{
                    const res = await axios.post("http://localhost:8080/api/qr",{
                        url: urlToShort,
                    })
                    setQr(res.data.qr)

                }catch(err){
                    console.log(err)
                }
            }



            return(
                <section className="content">
                    <form onSubmit={()=>{}}>
                        <h1 className="title">Urlshorter</h1>
                        <section className="input-section email">
                            <section/>
                            <section>
                                <input type="text"
                                    placeholder="url to short"
                                    className="input"
                                    required
                                    value={urlToShort}
                                    onChange={(v)=>{seturlToShort(v.target.value)}}
                                />
                            </section>
                            <p>Url recortada: {urlShorted}</p>

                            <button type="button" className="btn" onClick={shortUrl}>Recortar</button>
                            <button type="button" className="btn" onClick={()=>{history.push('/signin')}}>SignIn</button>
                            <button type="button" className="btn" onClick={()=>{history.push('/signup')}}>Registrarse</button>
                            <button type="button" className="btn" onClick={getQr}>Generar QR</button>
                            { qr != "" ?
                                <>
                                    <p>Código QR: </p>
                                    <img src={"data:image/png;base64," +  qr} />
                                </>
                                :
                                <p/>
                            }
                        </section>
                    </form>
                </section>
            );
}

export default UrlPage;