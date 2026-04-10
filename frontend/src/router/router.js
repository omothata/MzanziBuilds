// router/router.js
import { loadHome } from "../home/home.html";
//import { loadLogin } from "../pages/auth/login.js";
//import { loadSignup } from "../pages/auth/signup.js";

const routes = {
  "/": loadHome,
  "/login": loadLogin,
  "/signup": loadSignup,
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

window.addEventListener("popstate", router);
window.addEventListener("load", router);