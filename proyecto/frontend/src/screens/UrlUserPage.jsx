import React, { useState, useContext, useEffect } from 'react'

import AuthContext from '../contex'
import axios from 'axios'

import history from "../history"

const UrlUserPage = () => {
    const [urlToShort,seturlToShort] = useState("")
    
    const [urlShorted, setUrlShorted]= useState("Not url shorted yet")
    
    const shortUrl = async () => {
        try{
            const res = await axios.post("http://localhost:8080/api/user/shorter",{
                headers: {
                    "accessToken" : "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiI2MTg4MjhlNmVkYTg2ZDAwZTZhMTM3OTkifQ.zPUBqnsdjfj6EAjeRbzDy9knaeeDptl3uWl4HIDuwJ3kd_7m8j28VSwPu0uwPavGv8S4QDwhZhOgXC2fZPcxPg"// TODO POner un token
                },
                url: urlToShort
            })
            setUrlShorted("http://localhost:3000/api/"+res.data.url)
            console.log(res.data)
            console.log(res.data.url)
            console.log(urlShorted)
        }catch(err){
            console.log(err)
        }
    }
        return(
            <section className="content">
                <form onSubmit={()=>{}}>
                    <h2 className="title">Url to short</h2>
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
                        <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #EA4C46, #F07470, #F1959B)", color: 'white'}} onClick={shortUrl}>Recortar</button>
                    </section>
                </form>
            </section>
        );
}

export default UrlUserPage;