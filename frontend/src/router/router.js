// router/router.js
import { loadHome } from "../home/home.js";
import { loadSignin } from "../auth/sign-in.js";
import { loadSignup } from "../auth/sign-up.js";

const routes = {
  "/": loadHome,
  "/sign-in": loadSignin,
  "/sign-up": loadSignup,
};

function normalizeRoute(path) {
  if (!path || path === "/index.html") {
    return "/";
  }

  const [pathname] = path.split(/[?#]/);
  const trimmedPath = pathname.endsWith("/") && pathname.length > 1
    ? pathname.slice(0, -1)
    : pathname;

  const aliases = {
    "/signup": "/sign-up",
    "/signin": "/sign-in",
  };

  return aliases[trimmedPath] || trimmedPath;
}

export function navigate(path) {
  const normalizedPath = normalizeRoute(path);

  window.history.pushState({}, "", normalizedPath);
  router();
}

function handleLinkClick(event) {
  const link = event.target.closest("a[data-link]");

  if (!link) {
    return;
  }

  if (
    event.defaultPrevented ||
    event.button !== 0 ||
    event.metaKey ||
    event.ctrlKey ||
    event.shiftKey ||
    event.altKey
  ) {
    return;
  }

  const href = link.getAttribute("href");

  if (!href || href.startsWith("http")) {
    return;
  }

  event.preventDefault();
  navigate(href);
}

function router() {
  const path = normalizeRoute(window.location.pathname);
  const page = routes[path] || loadHome;
  page();
}

document.addEventListener("click", handleLinkClick);
window.addEventListener("popstate", router); // re-render the page when user goes back/forward
window.addEventListener("load", router); //run router @ page load
