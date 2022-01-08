import React, { useState, useContext } from 'react'

import AuthContext from '../context'
import axios from 'axios'

import history from "../history"

const SignIn = () => {
    const [Email,setEmail] = useState("")
    const [Password,setPassword] = useState("")
    const { signIn } = useContext(AuthContext);
    
    const saveUser = async () => {

        const params = new URLSearchParams()
        params.append('grant_type','password')
        params.append('client_id','public')
        params.append('username',Email)
        params.append('password',Password)

        const config = {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          }

        try{
            const res = await axios.post("http://localhost:8000/auth/realms/SpringBoot/protocol/openid-connect/token",params, config)
            const userToken = res.data.access_token
            signIn(userToken)
            history.push('/urluserpage', {token: userToken})
        }catch(err){
            console.log(err)
        }
    }
        return(
            <section className="content">
                <form onSubmit={()=>{}}>
                    <h2 className="title">Log In</h2>
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
            	    <button type="button" className="btn" onClick={saveUser}>Log In</button>
                    <button type="button" className="btn" onClick={()=>{history.push('/signup')}}>SignUp</button>
                </form>
            </section>
        );
}

export default SignIn;