import React, {useEffect} from 'react';
import './App.css';
import CurrentUser from "./components/page/content/CurrentUser";
import axios from "axios";

export default () => {

    axios.defaults.withCredentials = true;

  const [text, setText] = React.useState('');
  const [username, setUsername] = React.useState('');

  useEffect(() => {
    getCurrentUser().then(r => console.log('Retrieved username: ' + r));
  }, []);

  const getCurrentUser = async () => {
    axios.get('http://localhost:8080/api/v1/user/current')
        .then(function (response) {
          setUsername(response.data.username);
        })
        .catch(function (error) {
          console.log('Error: ' + error);
        });
  }

  return (
      <div>
        <CurrentUser username={username}/>
      </div>
  );
}