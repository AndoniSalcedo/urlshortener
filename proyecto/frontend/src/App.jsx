import React, { useReducer, useMemo, useEffect } from 'react';
import {Router, Route} from 'react-router'

import SignIn from './screens/SignIn'
import SignUp from './screens/SignUp'
import UrlPage from './screens/UrlPage'
import UrlUserPage from './screens/UrlUserPage'
import WaitPage from './screens/WaitPage'

import AuthContext from './context'
import history from './history'

const App = () => {

  const initialState = {
    Token: null
  }

  const reducer = (prevState, action) => {
    switch(action.type){
      case 'LOGIN':
        return{
          ...prevState,
          Token: action.Token
        } 
      case 'LOGOUT':
        return{
          ...prevState,
          Token: null
        }
      default:
        return{
          ...prevState
        }
    }
  }

  const [state, dispatch] = useReducer(reducer, initialState);

  const authContext = useMemo(() => ({
    signIn: async(userToken) => {
      try {
        dispatch({type:'LOGIN', Token: userToken});
        localStorage.setItem('token', userToken);
      } catch(err) {
        console.log(err);
      }
    }
  }), []);

  useEffect(()=>{
    const userToken = localStorage.getItem('token');
    dispatch({type:'LOGIN', Token: userToken});
    console.log(userToken);
  },[])


  return(
    <AuthContext.Provider value={authContext}>
        <section className="App">
          <Router history={history}>
          {/*state.Token == null ?
            <UrlPage/>
            :
            <UrlUserPage/>*/
          }
            <Route path="/" exact component={UrlPage}></Route>
            <Route path="/signup" exact component={SignUp}></Route>
            <Route path="/signin" exact component={SignIn}></Route>
            <Route path="/signout" exact render={ () => {
              localStorage.removeItem('token');
              history.push('/')
            }}></Route>
            <Route path="/urlpage" exact component={UrlPage}></Route>
            <Route path="/urluserpage" exact component={UrlUserPage}></Route>
            <Route path="/s/:id" component={WaitPage}></Route>
          </Router>
        </section>
      
    </AuthContext.Provider>
  );
}

export default App;