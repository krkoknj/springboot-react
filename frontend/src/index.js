import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { UserSignupPage } from './pages/UserSignupPage.js'
import * as apiCalls from './api/apiCall';

const actions = {
  postSignup : apiCalls.signup
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <UserSignupPage actions={actions}/>
  </React.StrictMode>
);

// ReactDOM.render(<UserSignupPage/>, document.getElementById('root'));

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
