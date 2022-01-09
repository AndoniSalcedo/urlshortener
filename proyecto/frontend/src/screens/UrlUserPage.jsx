import React, { useState } from 'react'

import axios from 'axios'

import history from "../history"

const UrlUserPage = () => {
    const [urlToShort, seturlToShort] = useState("")

    const [urlShorted, setUrlShorted] = useState("")

    const [qr, setQr] = useState("")

    const [checked, setChecked] = React.useState(false);

    const handleChange = () => {
        setChecked(!checked);
        console.log(checked)
    };

    const token = history.location.state?.token

    const shortUrl = async () => {
        try{
            const res = await axios.post("http://localhost:8080/api/user/shorter",
                {
                    url: urlToShort,
                    qr: checked,
                },
                {headers: {"Authorization" : token}})
            setUrlShorted("http://localhost:3000/s/"+res.data.url)

        }catch(err){
            console.log(err)
        }
    }
    const getQr = async () => {
        if (urlShorted === "") {

        } else {
            try {
                const res = await axios.post("http://localhost:8080/api/qr", {
                    url: urlToShort,
                })
                setQr(res.data.qr)
            } catch (err) {
                console.log(err)
            }
        }
    }
    const seeUrls = async () => {
        try{
            const res = await axios.post("http://localhost:8080/api/user/urls",
                {},
                {headers: {"Authorization" : token}})
            console.log(res.data.urls)
        }catch(err){
            console.log(err)
        }
    }

    return (
        <section className="content">
            <form onSubmit={() => { }}>
                <h2 className="title">Url to short</h2>
                <section className="input-section email">
                    <section />
                    <section>
                        <input type="text"
                            placeholder="url to short"
                            className="input"
                            required
                            value={urlToShort}
                            onChange={(v) => { seturlToShort(v.target.value) }}
                        />
                        <label>
                            Generar además un QR
                            <input
                                type="checkbox"
                                checked={checked}
                                onChange={handleChange}>
                            </input>
                        </label> 
                    </section>
                    {urlShorted !== "" ?
                        <>
                        <p>Url recortada: {urlShorted}</p>
                        </>
                        :
                        <p/>
                    }
                    <button type="button" className="btn" onClick={shortUrl}>Recortar</button>
                    <button type="button" className="btn" onClick={getQr}>Mostrar QR</button>
                    <button type="button" className="btn" onClick={seeUrls}>Ver mis urls</button>
                    <button type="button" className="logout" onClick={() => { history.push('/signout') }}>Log-out</button>
                    {qr !== "" ?
                        <>
                            <p>Código QR: </p>
                            <img src={"data:image/png;base64," + qr} />
                        </>
                        :
                        <p />
                    }
                </section>
            </form>
        </section>
    );
}

export default UrlUserPage;