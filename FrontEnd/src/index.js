import React from 'react'
import ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { Provider } from 'react-redux';
import { applyMiddleware, createStore } from 'redux';
import rootReducer from './redux/rootReducer';
import { composeWithDevTools } from 'redux-devtools-extension'; 
import logger from 'redux-logger';
export const store = createStore(rootReducer, applyMiddleware(logger));

ReactDOM.render(
    <Provider store={store}><BrowserRouter><App /></BrowserRouter></Provider>, document.getElementById('root'))

