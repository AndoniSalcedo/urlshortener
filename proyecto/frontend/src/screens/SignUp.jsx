import React, { useState, useContext } from 'react'

import AuthContext from '../contex'
import axios from 'axios'

import history from "../history"

const SignUp = () => {
    const [Email,setEmail] = useState("")
    const [Password,setPassword] = useState("")
    const [Name,setName] = useState("")
    
    const registerUser = async () => {
        try{
            const res = await axios.post("http://localhost:8080/auth/register",{
                name: Name,
                email: Email,
                password: Password
            })
            console.log(res.data)
           history.push('/signin')
        }catch(err){
            console.log(err)
        }
    }
        return(
            <section className="content">
                <form onSubmit={()=>{}}>
                    <h2 className="title">Regístrate</h2>
                    <section className="input-section name">
                        <section/> 
                        <section>
                            <input type="text" 
                                placeholder="Name"
                                className="input" 
                                required
                                value={Name}
                                onChange={(v)=>{setName(v.target.value)}}
                            />
                        </section>
                    </section>
                    <section className="line"></section>
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
                        
                    </section>
                    <section className="line"></section>
                    <section className="input-section pass">
                        <section/>
           		        <section>
                           <input type="password" 
                                placeholder="Contraseña"
                                className="input"
                                required
                                value={Password}
                                onChange={(p)=>{setPassword(p.target.value)}}
                            />
            	        </section>
                    </section>
                    <section className="line"></section>
            	    <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #32be8f, #38d39f, #32be8f)", color: 'white'}} onClick={registerUser}>Register</button>
                    <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #EA4C46, #F07470, #F1959B)", color: 'white'}} onClick={()=>{history.push('/signin')}}>SignIn</button>
                </form>
            </section>
        );
}

export default SignUp;