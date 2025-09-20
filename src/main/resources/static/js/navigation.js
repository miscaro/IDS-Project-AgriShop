/**
 * Navigation Module - Gestione centralizzata della navigazione
 */

class NavigationManager {
    constructor() {
        this.currentPath = window.location.pathname;
        this.navigationItems = [
            { path: '/', label: 'Home', icon: '🏠' },
            { path: '/articoli', label: 'Articoli', icon: '📦' },
            { path: '/eventi', label: 'Eventi', icon: '📅' },
            { path: '/aziende', label: 'Aziende', icon: '🏢' }
        ];
        
        this.userNavigationItems = [
            { path: '/dashboard', label: 'Dashboard', icon: '📊', requiresAuth: true }
        ];
    }

    /**
     * Inizializza il sistema di navigazione
     */
    init() {
        this.createNavigation();
        this.updateActiveNavigation();
        this.setupMobileMenu();
        this.setupAuthStateHandling();
    }

    /**
     * Crea la struttura HTML della navigazione
     */
    createNavigation() {
        const navElements = document.querySelectorAll('nav.navbar');
        navElements.forEach(nav => this.updateNavigationElement(nav));
    }

    /**
     * Aggiorna un elemento di navigazione esistente
     */
    updateNavigationElement(navElement) {
        const container = navElement.querySelector('.container');
        if (!container) return;

        container.innerHTML = `
            <div class="nav-brand-section">
                <a href="/" class="brand">
                    <span class="brand-icon">🌱</span>
                    <span class="brand-text">AgriShop</span>
                </a>
                <button class="mobile-menu-toggle" id="mobileMenuToggle" aria-label="Menu">
                    <span></span>
                    <span></span>
                    <span></span>
                </button>
            </div>
            <div class="nav-content" id="navContent">
                <ul class="nav-links">
                    ${this.generateNavigationLinks()}
                </ul>
                <div class="nav-actions">
                    ${this.generateAuthActions()}
                </div>
            </div>
        `;
    }

    /**
     * Genera i link di navigazione principale
     */
    generateNavigationLinks() {
        return this.navigationItems.map(item => `
            <li class="nav-item">
                <a href="${item.path}" class="nav-link ${this.isActivePath(item.path) ? 'active' : ''}" 
                   data-path="${item.path}">
                    <span class="nav-icon">${item.icon}</span>
                    <span class="nav-label">${item.label}</span>
                </a>
            </li>
        `).join('');
    }

    /**
     * Genera le azioni di autenticazione
     */
    generateAuthActions() {
        const user = this.getCurrentUser();
        
        if (user) {
            return `
                <div class="user-menu">
                    <div class="user-info">
                        <span class="user-icon">👤</span>
                        <span class="user-name">${user.username || 'Utente'}</span>
                        <span class="user-role-badge">${this.formatUserRole(user.role)}</span>
                    </div>
                    <div class="user-dropdown">
                        ${this.generateUserMenuItems()}
                        <a href="#" class="dropdown-item logout-btn" onclick="navigation.logout()">
                            <span class="item-icon">🚪</span>
                            Logout
                        </a>
                    </div>
                </div>
            `;
        } else {
            return `
                <div class="auth-actions">
                    <a href="/login" class="nav-btn btn-outline">Accedi</a>
                    <a href="/register" class="nav-btn btn-primary">Registrati</a>
                </div>
            `;
        }
    }

    /**
     * Genera gli elementi del menu utente
     */
    generateUserMenuItems() {
        const user = this.getCurrentUser();
        if (!user) return '';

        const extra = [];
        const role = this.formatUserRole(user.role);
        if (role === 'Acquirente') {
            extra.push({ path: '/carrello', label: 'Carrello', icon: '🛒' });
            extra.push({ path: '/ordini', label: 'Ordini', icon: '📦' });
        }

        return this.userNavigationItems.concat(extra)
            .filter(item => !item.requiresAuth || user)
            .map(item => `
                <a href="${item.path}" class="dropdown-item ${this.isActivePath(item.path) ? 'active' : ''}">
                    <span class="item-icon">${item.icon}</span>
                    ${item.label}
                </a>
            `).join('');
    }

    /**
     * Verifica se un percorso è attivo
     */
    isActivePath(path) {
        if (path === '/') {
            return this.currentPath === '/';
        }
        return this.currentPath.startsWith(path);
    }

    /**
     * Aggiorna la navigazione attiva
     */
    updateActiveNavigation() {
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
        });

        const currentLink = document.querySelector(`[data-path="${this.getCurrentMainPath()}"]`);
        if (currentLink) {
            currentLink.classList.add('active');
        }
    }

    /**
     * Ottiene il percorso principale corrente
     */
    getCurrentMainPath() {
        const path = this.currentPath;
        if (path === '/') return '/';
        if (path.startsWith('/articolo')) return '/articoli';
        if (path.startsWith('/evento')) return '/eventi';
        if (path.startsWith('/azienda')) return '/aziende';
        if (path.startsWith('/dashboard')) return '/dashboard';
        
        return path.split('/').slice(0, 2).join('/') || '/';
    }

    /**
     * Configura il menu mobile
     */
    setupMobileMenu() {
        const toggleButton = document.getElementById('mobileMenuToggle');
        const navContent = document.getElementById('navContent');
        
        if (toggleButton && navContent) {
            toggleButton.addEventListener('click', () => {
                navContent.classList.toggle('mobile-open');
                toggleButton.classList.toggle('active');
                document.body.classList.toggle('nav-open');
            });

            navContent.addEventListener('click', (e) => {
                if (e.target.matches('.nav-link')) {
                    navContent.classList.remove('mobile-open');
                    toggleButton.classList.remove('active');
                    document.body.classList.remove('nav-open');
                }
            });

            window.addEventListener('resize', () => {
                if (window.innerWidth > 768) {
                    navContent.classList.remove('mobile-open');
                    toggleButton.classList.remove('active');
                    document.body.classList.remove('nav-open');
                }
            });
        }
    }

    /**
     * Configura la gestione dello stato di autenticazione
     */
    setupAuthStateHandling() {
        document.addEventListener('authStateChanged', () => {
            this.createNavigation();
        });

        this.setupUserDropdown();
    }

    /**
     * Configura il dropdown utente
     */
    setupUserDropdown() {
        document.addEventListener('click', (e) => {
            const userMenu = document.querySelector('.user-menu');
            if (!userMenu) return;

            if (userMenu.contains(e.target)) {
                userMenu.classList.toggle('dropdown-open');
            } else {
                userMenu.classList.remove('dropdown-open');
            }
        });
    }

    /**
     * Ottiene l'utente corrente
     */
    getCurrentUser() {
        if (window.auth && typeof window.auth.getCurrentUser === 'function') {
            return window.auth.getCurrentUser();
        }
        return null;
    }

    /**
     * Formatta il ruolo utente
     */
    formatUserRole(role) {
        const normalize = (r) => {
            if (window.auth && typeof window.auth.normalizeRole === 'function') {
                return window.auth.normalizeRole(r);
            }
            if (!r) return null;
            const withPrefix = r.startsWith('ROLE_') ? r : 'ROLE_' + r;
            return withPrefix
                .replace('ROLE_DISTRIBUTORETIPICITA', 'ROLE_DISTRIBUTORE_TIPICITA')
                .replace('ROLE_ANIMATOREFILIERA', 'ROLE_ANIMATORE_FILIERA');
        };
        const n = normalize(role);
        const roleMap = {
            'ROLE_PRODUTTORE': 'Produttore',
            'ROLE_TRASFORMATORE': 'Trasformatore',
            'ROLE_DISTRIBUTORE_TIPICITA': 'Distributore',
            'ROLE_ACQUIRENTE': 'Acquirente',
            'ROLE_CURATORE': 'Curatore',
            'ROLE_ANIMATORE_FILIERA': 'Animatore',
            'ROLE_GESTOREPIATTAFORMA': 'Gestore'
        };
        return roleMap[n] || 'Utente';
    }

    /**
     * Esegue il logout
     */
    logout() {
        if (window.auth && typeof window.auth.logout === 'function') {
            window.auth.logout();
        } else {
            localStorage.removeItem('auth_token');
            localStorage.removeItem('auth_user');
            window.location.href = '/';
        }
    }

    /**
     * Aggiorna la navigazione dopo un cambio di pagina
     */
    updateCurrentPath(newPath) {
        this.currentPath = newPath;
        this.updateActiveNavigation();
    }

    /**
     * Metodo per notificare cambiamenti di stato auth
     */
    notifyAuthChange() {
        const event = new CustomEvent('authStateChanged');
        document.dispatchEvent(event);
    }
}

// Inizializza il navigation manager
const navigation = new NavigationManager();

// Auto-inizializza quando il DOM è pronto
document.addEventListener('DOMContentLoaded', () => {
    navigation.init();
});

// Esporta per uso globale
window.navigation = navigation;
