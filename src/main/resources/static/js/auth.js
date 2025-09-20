/**
 * Gestione autenticazione e utilità lato client
 */

window.auth = {
  normalizeRole(raw) {
    if (!raw) return null;
    const r = raw.startsWith('ROLE_') ? raw : 'ROLE_' + raw;
    return r
      .replace('ROLE_DISTRIBUTORETIPICITA', 'ROLE_DISTRIBUTORE_TIPICITA')
      .replace('ROLE_ANIMATOREFILIERA', 'ROLE_ANIMATORE_FILIERA');
  },
  /**
   * Inizializza il modulo auth
   */
  init() {
    ui.updateAuthUI();
  },

  /**
   * Verifica se l'utente è autenticato
   * @returns {boolean}
   */
  isLogged() {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    return !!(token && user);
  },

  /**
   * Ottiene i dati dell'utente corrente
   * @returns {object|null}
   */
  getUser() {
    try {
      const userRaw = localStorage.getItem('user');
      return userRaw ? JSON.parse(userRaw) : null;
    } catch (error) {
      console.error('Errore nel parsing dei dati utente:', error);
      return null;
    }
  },

  /**
   * Alias per compatibilità
   * @returns {object|null}
   */
  getCurrentUser() {
    return this.getUser();
  },

  /**
   * Ottiene headers di autenticazione
   * @returns {object}
   */
  getAuthHeaders() {
    const token = this.getToken();
    return token ? {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    } : {
      'Content-Type': 'application/json'
    };
  },

  /**
   * Ottiene il token di autenticazione
   * @returns {string|null}
   */
  getToken() {
    return localStorage.getItem('token');
  },

  /**
   * Verifica se l'utente ha un ruolo specifico
   * @param {string} role - Il ruolo da verificare
   * @returns {boolean}
   */
  hasRole(role) {
    const user = this.getUser();
    return user && user.role === role;
  },

  /**
   * Ottiene il ruolo dell'utente corrente
   * @returns {string|null}
   */
  getUserRole() {
    const user = this.getUser();
    return user ? user.role : null;
  },

  /**
   * Salva i dati di autenticazione
   * @param {string} token - Token di accesso
   * @param {object} userData - Dati dell'utente
   */
  saveAuthData(token, userData) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(userData));
  },

  /**
   * Effettua il logout
   */
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  window.location.replace('/login');
  },

  /**
   * Reindirizza alla dashboard appropriata
   */
  gotoDashboard() {
    window.location.replace('/dashboard');
  },

  /**
   * Ottiene l'URL della dashboard corretta per il ruolo dell'utente
   * @returns {string}
   */
  getDashboardUrl() {
    const user = this.getCurrentUser();
    if (!user || !user.role) return '/';
    const role = this.normalizeRole(user.role);
    switch(role) {
      case 'ROLE_GESTOREPIATTAFORMA':
      case 'ROLE_GESTORE':
        return '/dashboard/gestore';
      case 'ROLE_ACQUIRENTE':
        return '/dashboard/acquirente';
      case 'ROLE_PRODUTTORE':
      case 'ROLE_TRASFORMATORE':
      case 'ROLE_DISTRIBUTORE_TIPICITA':
        return '/dashboard/venditore';
      case 'ROLE_ANIMATORE_FILIERA':
      case 'ROLE_ANIMATORE':
        return '/dashboard/animatore';
      case 'ROLE_CURATORE':
        return '/dashboard/curatore';
      default:
        return '/';
    }
  },

  /**
   * Richiede autenticazione: se mancante reindirizza a /login
   * @returns {object|null} utente
   */
  requireAuth() {
    const user = this.getCurrentUser();
    const token = this.getToken();
    if(!user || !token) {
      window.location.replace('/login');
      return null;
    }
    return user;
  },

  /**
   * Richiede che l'utente abbia uno dei ruoli indicati (varianti normalizzate)
   * @param {string[]} roles ruoli accettati (normalizzati o legacy)
   * @param {string} fallback destinazione se non autorizzato (default '/')
   * @returns {string|null} ruolo normalizzato se ok, altrimenti null
   */
  requireRoles(roles = [], fallback = '/') {
    const user = this.requireAuth();
    if(!user) return null;
    const norm = this.normalizeRole(user.role);
    const normalizedAllowed = roles.map(r => this.normalizeRole(r));
    if(!normalizedAllowed.includes(norm)) {
      window.location.replace(fallback);
      return null;
    }
    return norm;
  },

  /**
   * Assicura autenticazione e opzionalmente ruoli, ritorna oggetto { user, role }
   */
  ensure(roles = null, fallback = '/') {
    const user = this.requireAuth();
    if(!user) return null;
    if(Array.isArray(roles) && roles.length) {
      const role = this.requireRoles(roles, fallback);
      if(!role) return null;
      return { user, role };
    }
    return { user, role: this.normalizeRole(user.role) };
  },


  /**
   * Reindirizza alla dashboard basata sul ruolo
   */
  gotoDashboardByRole() {
    const role = this.normalizeRole(this.getUserRole());
    if (!role) {
      this.logout();
      return;
    }
    const dashboardRoutes = {
      'ROLE_GESTOREPIATTAFORMA': '/dashboard/gestore',
      'ROLE_ACQUIRENTE': '/dashboard/acquirente',
      'ROLE_PRODUTTORE': '/dashboard/venditore',
      'ROLE_TRASFORMATORE': '/dashboard/venditore',
      'ROLE_DISTRIBUTORE_TIPICITA': '/dashboard/venditore',
      'ROLE_ANIMATORE_FILIERA': '/dashboard/animatore',
      'ROLE_CURATORE': '/dashboard/curatore'
    };
    const route = dashboardRoutes[role];
    if (route) {
      window.location.replace(route);
    } else {
      console.warn('Ruolo non riconosciuto:', role);
      window.location.replace('/');
    }
  }
};

// UI Utils Module
window.ui = {
  /**
   * Mostra un messaggio all'utente
   * @param {string} message - Il messaggio da mostrare
   * @param {string} type - Tipo di messaggio (success, error, info)
   * @param {string} containerId - ID del contenitore per il messaggio
   */
  showMessage(message, type = 'info', containerId = 'message') {
    const container = document.getElementById(containerId);
    if (!container) return;

    container.innerHTML = '';
    container.className = `message message-${type}`;
    container.textContent = message;
    container.style.display = 'block';
  },

  /**
   * Nasconde un messaggio
   * @param {string} containerId - ID del contenitore del messaggio
   */
  hideMessage(containerId = 'message') {
    const container = document.getElementById(containerId);
    if (container) {
      container.style.display = 'none';
    }
  },

  /**
   * Toggle della visibilità di un elemento
   * @param {string} elementId - ID dell'elemento
   * @param {boolean} show - Se mostrare o nascondere
   */
  toggleVisibility(elementId, show) {
    const element = document.getElementById(elementId);
    if (element) {
      element.classList.toggle('hidden', !show);
    }
  },

  /**
   * Aggiorna i controlli UI basati sullo stato di autenticazione
   */
  updateAuthUI() {
  },

  /**
   * Mostra una notificazione all'utente
   * @param {string} message - Il messaggio da mostrare
   * @param {string} type - Tipo di notificazione (success, error, info, warning)
   */
  showNotification(message, type = 'info') {
    this.showMessage(message, type);
  }
};

// HTTP Utils Module
window.http = {
  /**
   * Effettua una richiesta HTTP
   * @param {string} method - Metodo HTTP
   * @param {string} url - URL della richiesta
   * @param {object} options - Opzioni della richiesta
   * @returns {Promise}
   */
  request(method, url, options = {}) {
    return new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();
      xhr.open(method, url, true);

      // Imposta headers
      const headers = {
        'Content-Type': 'application/json',
        ...options.headers
      };

      // Aggiunge token se disponibile
      const token = auth.getToken();
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }

      Object.keys(headers).forEach(key => {
        xhr.setRequestHeader(key, headers[key]);
      });

      xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
          if (xhr.status >= 200 && xhr.status < 300) {
            try {
              const ct = xhr.getResponseHeader('Content-Type') || '';
              if (ct.includes('application/json')) {
                const data = xhr.responseText ? JSON.parse(xhr.responseText) : null;
                resolve(data);
              } else {
                resolve({ raw: xhr.responseText });
              }
            } catch (error) {
              resolve({ raw: xhr.responseText });
            }
          } else {
            let message = `Errore HTTP: ${xhr.status}`;
            try {
              const ct = xhr.getResponseHeader('Content-Type') || '';
              if (ct.includes('application/json') && xhr.responseText) {
                const data = JSON.parse(xhr.responseText);
                if (data && data.message) message = data.message;
              } else if (xhr.responseText) {
                message = xhr.responseText;
              }
            } catch {}
            reject(new Error(message));
          }
        }
      };

      // Invia la richiesta
      const body = options.body ? JSON.stringify(options.body) : null;
      xhr.send(body);
    });
  },

  /**
   * Effettua una richiesta GET
   * @param {string} url - URL della richiesta
   * @param {object} options - Opzioni della richiesta
   * @returns {Promise}
   */
  get(url, options = {}) {
    return this.request('GET', url, options);
  },

  /**
   * Effettua una richiesta POST
   * @param {string} url - URL della richiesta
   * @param {object} data - Dati da inviare
   * @param {object} options - Opzioni della richiesta
   * @returns {Promise}
   */
  post(url, data, options = {}) {
    return this.request('POST', url, { ...options, body: data });
  }
};

// Inizializzazione quando il DOM è pronto
document.addEventListener('DOMContentLoaded', function() {
  ui.updateAuthUI();
});
