import logo from './logo.svg';
import React, { Component } from 'react';
import './App.css';

export default class App extends Component {
  state = {
    disabled: true
  };
  toggle = () => {
    const disabledState = this.state.disabled;
    this.setState({
        disabled: !disabledState
    })
  }
  render() {
    const {disabled} = this.state;
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
        </header>
        <input id="field" disabled={disabled} />
        <button id="button" onClick={this.toggle}>Toggle</button>
      </div>
    );
  }
}
