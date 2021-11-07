import React, { useState, useContext } from 'react'

import AuthContext from '../contex'
import axios from 'axios'

import history from "../history"

const UrlPage = () => {
    const [Email,setEmail] = useState("")
    const [Password,setPassword] = useState("")
    const { signIn } = useContext(AuthContext);
    
    const saveUser = async () => {
        try{
            const res = await axios.post("http://localhost:80//auth/login",{
                email: Email,
                password: Password
            })
            const userToken = res.data.token
            console.log(res.data)
           signIn(userToken)
           history.push('/España')
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
                                placeholder="Email"
                                className="input" 
                                required
                                value={Email}
                                onChange={(v)=>{setEmail(v.target.value)}}
                            />
                        </section>
                        <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #EA4C46, #F07470, #F1959B)", color: 'white'}} onClick={()=>{
                            history.push('/signin')  // TODO: Cambiar esto para que haga la petición
                        }}>Recortar</button>
                    </section>
                    <section className="line"></section>
            	    <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #EA4C46, #F07470, #F1959B)", color: 'white'}} onClick={()=>{history.push('/signin')}}>Log In</button>
                </form>
            </section>
        );
}

export default UrlPage;