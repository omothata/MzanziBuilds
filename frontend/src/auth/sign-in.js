import { navigate } from "../router/router.js";

export function loadSignin() {
    fetch("./home/home.html").then(response => response.text()).then(html=>{
        document.getElementById("app").innerHTML = html;

    
    })
}