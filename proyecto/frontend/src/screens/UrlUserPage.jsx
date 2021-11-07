import React, { useState, useContext } from 'react'

import AuthContext from '../contex'
import axios from 'axios'

import history from "../history"

const UrlUserPage = () => {
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
                    <h2 className="title">Welcome</h2>
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
                    <a className="login-component" href="/recovery">Forgot Password?</a>
            	    <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #32be8f, #38d39f, #32be8f)", color: 'white'}} onClick={saveUser}>Log In</button>
                    <button type="button" className="btn" style={{backgroundImage: "linear-gradient(to right, #EA4C46, #F07470, #F1959B)", color: 'white'}} onClick={()=>{history.push('/signUp')}}>SignUp</button>
                </form>
            </section>
        );
}

export default UrlUserPage;