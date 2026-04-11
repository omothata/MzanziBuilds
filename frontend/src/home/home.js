import { navigate } from "../router/router.js";

export function loadHome() {
    fetch("./home/home.html").then(response => response.text()).then(html=>{
        document.getElementById("app").innerHTML = html;

        // Home screen navigation
        document.getElementById("sign-in")?.addEventListener("click", () => {navigate("/sign-in");});

       // document.getElementById("signup")?.addEventListener("click", () => {
       //     navigate("/signup");
       // });
    })
}