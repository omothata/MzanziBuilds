// router/router.js
import { loadHome } from "../home/home.js";
import { loadSignin } from "../auth/sign-in.js";
import { loadSignup } from "../auth/sign-up.js";
import { loadMainFeed } from "../feed/main-feed.js";
import { loadExploreFeed } from "../feed/explore-feed.js";
import { loadProfileSetup } from "../onboarding/profile-setup.js";
import { loadFollowedSkills } from "../onboarding/followed-skills.js";
import { loadFollowSuggestions } from "../onboarding/follow-suggestions.js";
import { loadCreateProject } from "../projects/create-project.js";
import { loadProjectDetails } from "../projects/project-details.js";

const routes = {
  "/": loadHome,
  "/sign-in": loadSignin,
  "/sign-up": loadSignup,
  "/onboarding/profile": loadProfileSetup,
  "/onboarding/interests": loadFollowedSkills,
  "/onboarding/follow": loadFollowSuggestions,
  "/feed": loadMainFeed,
  "/explore-feed": loadExploreFeed,
  "/projects/create": loadCreateProject,
  "/projects/view": loadProjectDetails,
};

function normalizeRoute(path) {
  if (!path || path === "/index.html") {
    return "/";
  }

  const queryIndex = path.indexOf("?");
  const hashIndex = path.indexOf("#");
  const pathnameEnd = [queryIndex, hashIndex]
    .filter((index) => index >= 0)
    .reduce((smallest, current) => Math.min(smallest, current), path.length);
  const pathname = path.slice(0, pathnameEnd);
  const suffix = path.slice(pathnameEnd);
  const trimmedPath = pathname.endsWith("/") && pathname.length > 1
    ? pathname.slice(0, -1)
    : pathname;

  const aliases = {
    "/signup": "/sign-up",
    "/signin": "/sign-in",
    "/onboarding": "/onboarding/profile",
    "/main-feed": "/feed",
    "/mainfeed": "/feed",
    "/explore": "/explore-feed",
    "/explorefeed": "/explore-feed",
  };

  return (aliases[trimmedPath] || trimmedPath) + suffix;
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
  const path = normalizeRoute(`${window.location.pathname}${window.location.search}${window.location.hash}`);
  const [routePath] = path.split(/[?#]/);
  const page = routes[routePath] || loadHome;
  page();
}

document.addEventListener("click", handleLinkClick);
window.addEventListener("popstate", router); // re-render the page when user goes back/forward
window.addEventListener("load", router); //run router @ page load
