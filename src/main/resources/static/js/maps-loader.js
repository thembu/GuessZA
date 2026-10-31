// Loads the Google Maps JavaScript API using the key served by GET /api/config,
// so the key lives in .env instead of being hard-coded in the HTML.
//
// Usage (at the bottom of a page):
//   <script src="/js/maps-loader.js"></script>
//   <script>loadGoogleMaps('initMyMap');</script>
//
// The callback must be a global function (a top-level function in a normal <script>).

let mapsLoadStarted = false;

function showMapError(message) {
  let banner = document.getElementById('mapError');
  if (!banner) {
    banner = document.createElement('div');
    banner.id = 'mapError';
    banner.setAttribute('role', 'alert');
    banner.style.cssText = [
      'position:fixed', 'top:72px', 'left:50%', 'transform:translateX(-50%)', 'z-index:1000',
      'max-width:calc(100% - 32px)', 'padding:12px 20px', 'border-radius:8px',
      'background:#141a17', 'border:1px solid #f87171', 'color:#fff',
      "font:500 14px 'Inter',sans-serif", 'box-shadow:0 8px 32px rgba(0,0,0,0.6)',
    ].join(';');
    document.body.appendChild(banner);
  }
  banner.textContent = message;
}

// Google calls this global when the key is rejected (invalid, deleted, or this site isn't allowed).
window.gm_authFailure = () =>
  showMapError('Map unavailable: the Google Maps API key was rejected. Check the key and its website restrictions.');

async function loadGoogleMaps(callbackName) {
  if (mapsLoadStarted) return;
  mapsLoadStarted = true;

  let apiKey;
  try {
    const res = await fetch('/api/config');
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    ({ apiKey } = await res.json());
  } catch (err) {
    console.error('Could not load /api/config', err);
    showMapError('Map unavailable: could not load configuration from the server.');
    return;
  }

  if (!apiKey) {
    showMapError('Map unavailable: no Google Maps API key is configured (GOOGLE_MAPS_API_KEY in .env).');
    return;
  }

  const params = new URLSearchParams({ key: apiKey, callback: callbackName });
  const script = document.createElement('script');
  script.src = `https://maps.googleapis.com/maps/api/js?${params}`;
  script.async = true;
  script.onerror = () =>
    showMapError('Map unavailable: could not download Google Maps. Check your connection or ad blocker.');
  document.head.appendChild(script);
}
