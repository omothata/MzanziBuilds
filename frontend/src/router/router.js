// router/router.js
import { loadHome } from "../home/home.js";
import { loadSignin } from "../auth/sign-in.js";
//import { loadSignup } from "../pages/auth/signup.js";

const routes = {
  "/": loadHome,
  "/sign-in": loadSignin,
  //"/signup": loadSignup,
};

export function navigate(path) {
  window.history.pushState({}, "", path);
  router();
}

function router() {
  const path = window.location.pathname;
  const page = routes[path] || loadHome;
  page();
}

window.addEventListener("popstate", router); // re-render the page when user goes back/forward
window.addEventListener("load", router); //run router @ page load