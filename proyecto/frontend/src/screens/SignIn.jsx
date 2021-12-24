import React, { useState, useContext } from 'react'

import AuthContext from '../context'
import axios from 'axios'

import history from "../history"

const SignIn = () => {
    const [Email,setEmail] = useState("")
    const [Password,setPassword] = useState("")
    const { signIn } = useContext(AuthContext);
    
    const saveUser = async () => {
        try{
            const res = await axios.post("http://localhost:8080/auth/login",{
                email: Email,
                password: Password
            })
            const userToken = res.data.accessToken
            console.log(res.data)
            signIn(userToken)
            history.push('/urluserpage', {token: userToken})
        }catch(err){
            console.log(err)
        }
    }
        return(
            <section className="content">
                <form onSubmit={()=>{}}>
                    <h2 className="title">Iniciar Sesión</h2>
                    <section className="input-section email">
                        <section/> 
                        <section>
                            <input type="text" 
                                placeholder="E-mail"
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
            	    <button type="button" className="btn" onClick={saveUser}>Iniciar Sesión</button>
                    <button type="button" className="btn" onClick={()=>{history.push('/signup')}}>Registrarse</button>
                </form>
            </section>
        );
}

export default SignIn;