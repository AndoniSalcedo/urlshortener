import React, { useState, useContext } from 'react'

import AuthContext from '../contex'
import axios from 'axios'

import history from "../history"

const UrlUserPage = () => {
    const [urlToShort,seturlToShort] = useState("")
    
    const shortUrl = async () => {
        try{
            const res = await axios.post("http://localhost:8080/api/shorter",{
                url: urlToShort,
            })
            console.log(res.data.toString())
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
                        <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #EA4C46, #F07470, #F1959B)", color: 'white'}} onClick={shortUrl}>Recortar</button>
                    </section>
                </form>
            </section>
        );
}

export default UrlUserPage;