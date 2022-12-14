import React from 'react';

export class UserSignupPage extends React.Component {

  state = {
    displayName : '',
    username : '',
    password : '',
    passwordRepeat : '',
    pendingApiCall : false,
  }

  onChangeDisplayName = (event) => {
    const value = event.target.value;
    this.setState({displayName:value});
  }

  onChangeUsername = (event) => {
    const value = event.target.value;
    this.setState({username:value});
  }

  onChangePassword = (event) => {
    const value = event.target.value;
    this.setState({password:value});
  }

  onChangePasswordRepeat = (event) => {
    const value = event.target.value;
    this.setState({passwordRepeat:value});
  }

  onClickSignup = () => {
    const user = {
      username : this.state.username,
      password : this.state.password,
      displayName : this.state.displayName,
    }
    this.setState({pendingApiCall : true});
    console.log(user)
    this.props.actions.postSignup(user)
    .then((response) => {
      this.setState({pendingApiCall : false});
    })
    .catch(error => {
      this.setState({pendingApiCall : false});
    });
  }

  render() {
    return (
      <div className='container'>
        <h1 className='text-center'>Sign Up</h1>
        <div className='col-12 mb-3'>
          <label>Display Name</label>
          <input 
          className="form-control"
          placeholder='Your display name' 
          value={this.state.displayName}
          onChange={this.onChangeDisplayName}/>
        </div>
        <div className='col-12 mb-3'>
          <label>Your username</label>
          <input 
          className="form-control"
          placeholder='Your username'
          value={this.state.username}
          onChange={this.onChangeUsername}/>
        </div>
        <div className='col-12 mb-3'>
          <label>Your password</label>
          <input 
          className="form-control"
          placeholder='Your password' 
          type='password'
          value={this.state.password}
          onChange={this.onChangePassword}/>
        </div>
        <div className='col-12 mb-3'>
          <label>Repeat your password</label>
          <input 
          className="form-control"
          placeholder='Repeat your password' 
          type='password'
          value={this.state.passwordRepeat}
          onChange={this.onChangePasswordRepeat}/>
        </div>
        <div className='text-center'>
          <button
          className='btn btn-primary'
          onClick={this.onClickSignup}
          disabled={this.state.pendingApiCall}
          >
            {this.state.pendingApiCall && (<div class="spinner-border text-light spinner-border-sm mr-sm-1" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>)}
            Sign Up</button>
        </div>
      </div>
    )
  }

}

UserSignupPage.defaultProps ={
  actions : {
    postSignup : () => 
      new Promise((resolve, reject) => {
        resolve({});
      })
  }
}
export default UserSignupPage;