import './App.css';
import {Route, Routes} from "react-router-dom";
import Dashboard from './pages/dashboard';
import History from './pages/history';


function App() {
  return (
    <Routes>
      <Route path='/' element={<Dashboard></Dashboard>}></Route>
      <Route path='/history' element={<History></History>}></Route>
      <Route path='*' element={<Dashboard></Dashboard>}></Route>
    </Routes>
  );
}

export default App;
