// Shared API helper (#20). Every page talks to the backend through api(), so the JWT,
// relative URLs and 401 handling live in one place.
//
// Usage: load it before any page script that calls the API:
//   <script src="/js/api.js"></script>
//
// Token storage: localStorage. It survives tabs and reloads for the token's 2 h lifetime.
// The catch is that any XSS on our origin could read it, so pages must never put API data
// into innerHTML; use textContent.

const TOKEN_KEY = 'token';

// Before #18, identity lived in localStorage. Remove those keys so nothing relies on them.
['userId', 'nickname', 'highScore', 'gamesPlayed'].forEach(key => localStorage.removeItem(key));

function getToken() { return localStorage.getItem(TOKEN_KEY); }
function setToken(token) { localStorage.setItem(TOKEN_KEY, token); }
function clearToken() { localStorage.removeItem(TOKEN_KEY); }

// Reads the token's `exp` claim. This doesn't verify anything (only the server can);
// it just saves a round trip when the token has obviously expired.
function isExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    return typeof payload.exp === 'number' && payload.exp * 1000 <= Date.now();
  } catch (e) {
    return true;
  }
}

function isLoggedIn() {
  const token = getToken();
  return !!token && !isExpired(token);
}

function goToLogin() {
  clearToken();
  window.location.href = '/login.html';
}

// Call at the top of every page that needs an account.
function requireLogin() {
  if (!isLoggedIn()) goToLogin();
}

// Logout is client-side (#18): forget the token. It stays valid on the server until it expires.
function logout() {
  goToLogin();
}

class ApiError extends Error {
  constructor(status, detail) {
    super(detail || `HTTP ${status}`);
    this.status = status;
    this.detail = detail; // ProblemDetail "detail" from the server, if any
  }
}

// fetch wrapper: JSON in and out, Bearer token attached, non-2xx responses thrown as ApiError.
// auth: false is for login/register: no token is sent, and a 401 there means "wrong password",
// so it's thrown to the page instead of redirecting.
async function api(path, { method = 'GET', body, auth = true } = {}) {
  const headers = {};
  if (body !== undefined) headers['Content-Type'] = 'application/json';
  if (auth && getToken()) headers['Authorization'] = `Bearer ${getToken()}`;

  const res = await fetch(path, {
    method,
    headers,
    body: body === undefined ? undefined : JSON.stringify(body),
  });

  if (res.status === 401 && auth) {
    // Token missing, expired or rejected. Redirect, and never settle, so the calling page's
    // catch block doesn't flash an error alert while the browser navigates away.
    goToLogin();
    return new Promise(() => {});
  }

  if (!res.ok) {
    let detail;
    try { detail = (await res.json()).detail; } catch (e) { /* empty or non-JSON body */ }
    throw new ApiError(res.status, detail);
  }

  return res.status === 204 ? null : res.json();
}

// The logged-in user: { id, username, nickname, gamesPlayed, highScore }. Fetched once per page.
let mePromise;
function getMe() {
  if (!mePromise) mePromise = api('/api/me');
  return mePromise;
}
