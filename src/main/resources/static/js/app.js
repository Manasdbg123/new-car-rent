document.addEventListener('DOMContentLoaded', () => {
    initApp();
    setupEventListeners();
    window.addEventListener('hashchange', handleRouting);
    handleRouting();
});

function initApp() {
    setupNavbar();
}

// ==========================================
// ROUTING & NAVIGATION LOGIC
// ==========================================
window.executeHeroSearch = (forcedCity, forcedType) => {
    const loc = forcedCity || document.getElementById('heroLocation').value;
    const type = forcedType || document.getElementById('heroType').value;

    const carCityFilter = document.getElementById('carCityFilter');
    const bikeCityFilter = document.getElementById('bikeCityFilter');

    if (type === 'cars' && carCityFilter) carCityFilter.value = loc;
    else if (type === 'bikes' && bikeCityFilter) bikeCityFilter.value = loc;

    window.location.hash = '#' + type;
    filterVehicles();
};

function handleRouting() {
    let hash = window.location.hash || '#home';

    if (hash === '#dashboard' && !isAuthenticated()) {
        alert("Please login to access your dashboard.");
        window.location.hash = '#home';
        showModal('loginModal');
        return;
    }

    document.querySelectorAll('.page-view').forEach(view => view.style.display = 'none');

    const targetView = document.querySelector(hash + '-view');
    if (targetView) targetView.style.display = 'block';
    else document.getElementById('home-view').style.display = 'block';

    document.querySelectorAll('.nav-links .nav-item').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === hash) link.classList.add('active');
    });

    if (hash === '#cars' || hash === '#bikes') filterVehicles();
    else if (hash === '#dashboard') loadMyBookings();
}

// ==========================================
// MODAL & UI CONTROLS
// ==========================================
function showModal(id) { document.getElementById(id).style.display = 'flex'; }
function hideModal(id) { document.getElementById(id).style.display = 'none'; }

// Global timer variable for real-time inventory locks
let bookingTimerInterval;

window.closeBookingModal = () => {
    clearInterval(bookingTimerInterval);
    hideModal('bookingModal');
};

window.openInfoModal = (vehicleType, brandName) => {
    const title = document.getElementById('infoModalTitle');
    const body = document.getElementById('infoModalBody');
    if(!title || !body) return;

    title.innerText = `Rental Conditions: ${brandName}`;

    if(vehicleType === 'BIKE') {
        body.innerHTML = `
            <p style="margin-top:0;"><strong>🏍️ Two-Wheeler Deployment Protocols:</strong></p>
            <ul style="padding-left:20px; line-height:1.6;">
                <li><strong>Helmets Provided:</strong> One complimentary safety helmet included.</li>
                <li><strong>Security Hold:</strong> A temporary holding credit authorization of $100.</li>
            </ul>
        `;
    } else {
        body.innerHTML = `
            <p style="margin-top:0;"><strong>🚗 Four-Wheeler Fleet Regulations:</strong></p>
            <ul style="padding-left:20px; line-height:1.6;">
                <li><strong>Unlimited Mileage:</strong> Drive across boundaries with infinite transit operations.</li>
                <li><strong>Security Hold:</strong> A temporary holding credit authorization of $300.</li>
            </ul>
        `;
    }
    showModal('infoModal');
};

window.closeInfoModal = () => hideModal('infoModal');

// ==========================================
// DYNAMIC SERVER-SIDE SEARCH ENGINE
// ==========================================
let searchTimeout;
window.debounceFilter = () => {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(filterVehicles, 500);
};

window.filterVehicles = async () => {
    let hash = window.location.hash || '#home';
    if(hash !== '#cars' && hash !== '#bikes') return;

    let isCarPage = hash === '#cars';
    let type = isCarPage ? 'CAR' : 'BIKE';

    let citySelect = document.getElementById(isCarPage ? 'carCityFilter' : 'bikeCityFilter');
    let brandSelect = document.getElementById(isCarPage ? 'carBrandFilter' : 'bikeBrandFilter');
    let priceInput = document.getElementById(isCarPage ? 'carMaxPrice' : 'bikeMaxPrice');

    let city = citySelect ? citySelect.value : 'all';
    let brand = brandSelect ? brandSelect.value : 'all';
    let maxPrice = priceInput ? priceInput.value : '';

    const params = new URLSearchParams();
    params.append('vehicleType', type);
    params.append('size', '50');
    if (city && city !== 'all') params.append('city', city);
    if (brand && brand !== 'all') params.append('brand', brand);
    if (maxPrice) params.append('maxPrice', maxPrice);

    const gridId = isCarPage ? 'carList' : 'bikeList';
    const grid = document.getElementById(gridId);

    if (grid) grid.innerHTML = '<div style="grid-column: 1/-1; text-align:center; padding: 40px;"><h3 style="color:#0056b3;">Searching database...</h3></div>';

    try {
        const res = await fetch(`/api/cars/available?${params.toString()}`);
        if (!res.ok) throw new Error("Search API failed");

        const data = await res.json();
        const vehicles = data.data?.content || data.content || data.data || data;
        renderVehicles(vehicles, gridId);
    } catch (e) {
        if (grid) grid.innerHTML = '<div style="grid-column: 1/-1; text-align:center; padding: 40px; background:#f8d7da; color:#721c24; border-radius:8px;"><h3>Failed to load vehicles.</h3><p>Please check your connection and try again.</p></div>';
    }
}

function renderVehicles(vehicles, containerId) {
    const grid = document.getElementById(containerId);
    if (!grid) return;
    grid.innerHTML = '';

    if (!vehicles || vehicles.length === 0) {
        grid.innerHTML = '<div style="grid-column: 1/-1; text-align:center; padding: 40px; background:#f8f9fa; border-radius:8px;"><h3>No vehicles found matching your criteria.</h3></div>';
        return;
    }

    vehicles.forEach(v => {
        const icon = v.vehicleType === 'BIKE' ? '🏍️' : '💺';
        const evaluatedType = v.vehicleType || ((v.seats <= 2) ? 'BIKE' : 'CAR');

        grid.innerHTML += `
            <div class="car-card">
                <img src="${v.imageUrl || '/images/bmw.jpg'}" alt="${v.brand}">
                <div class="car-info">
                    <div style="display:flex; justify-content:space-between; align-items:start;">
                        <h3 style="margin-top:0;">${v.brand} ${v.model}</h3>
                        <span style="background:#f1f1f1; padding:4px 8px; border-radius:4px; font-size:0.8rem; font-weight:bold; color:#555;">${v.transmission}</span>
                    </div>
                    <p style="font-size: 0.95em; color: #555; margin-bottom: 5px;">📍 ${v.city} | ${icon} ${v.seats || 2} Seats | ⛽ ${v.fuelType}</p>
                    <p style="font-size: 0.85em; color: #777; min-height: 40px; margin-bottom:15px;">${v.description}</p>
                    
                    <div style="margin-bottom:15px; text-align:left;">
                        <span style="color:#0056b3; font-size:0.85rem; font-weight:bold; cursor:pointer; text-decoration:underline;" onclick="openInfoModal('${evaluatedType}', '${v.brand} ${v.model}')">ℹ️ Important Rental Info</span>
                    </div>

                    <hr style="border:0; border-top:1px solid #eee; margin:auto 0 15px 0;">
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <div>
                            <span style="font-weight:900; font-size:1.5rem; color:#0056b3;">$${v.dailyRate}</span>
                            <span style="color:#555; font-size:0.8rem;">/ day</span>
                        </div>
                        <button class="btn book-btn primary-btn" onclick="openBookingModal(${v.id}, '${v.brand} ${v.model}')" style="${evaluatedType === 'BIKE' ? 'background:#d63031;' : ''}">Book Now</button>
                    </div>
                </div>
            </div>
        `;
    });
}

// ==========================================
// BOOKING & INVENTORY LOCK SYSTEM
// ==========================================
window.openBookingModal = async (carId, carName) => {
    if (!isAuthenticated()) { alert('Please login to continue.'); showModal('loginModal'); return; }

    try {
        // Attempt to acquire an inventory lock
        const res = await secureFetch(`/api/cars/${carId}/lock`, { method: 'POST' });
        if (!res.ok) {
            alert('This vehicle is currently reserved by another user finishing their checkout. Please try again in 10 minutes.');
            return;
        }

        document.getElementById('bookingCarId').value = carId;
        document.getElementById('bookingCarName').innerText = `Reserve: ${carName}`;
        document.getElementById('startDate').value = '';
        document.getElementById('endDate').value = '';

        // Launch 10 Minute Countdown UI
        let timeRemaining = 600;
        const timerUI = document.getElementById('lockTimer');
        if(timerUI) timerUI.style.display = 'block';

        clearInterval(bookingTimerInterval);
        bookingTimerInterval = setInterval(() => {
            timeRemaining--;
            let m = Math.floor(timeRemaining / 60).toString().padStart(2, '0');
            let s = (timeRemaining % 60).toString().padStart(2, '0');

            const countdownEl = document.getElementById('lockCountdown');
            if(countdownEl) countdownEl.innerText = `${m}:${s}`;

            if (timeRemaining <= 0) {
                clearInterval(bookingTimerInterval);
                hideModal('bookingModal');
                alert('Your reservation hold has expired. The vehicle has been returned to the public pool.');
                filterVehicles(); // Refresh the grid
            }
        }, 1000);

        showModal('bookingModal');
    } catch(e) {
        console.error("Lock failed:", e);
    }
}

window.confirmBooking = async () => {
    const carId = document.getElementById('bookingCarId').value;
    const rawStart = document.getElementById('startDate').value;
    const rawEnd = document.getElementById('endDate').value;

    if (!rawStart || !rawEnd) return alert('Please select dates.');

    const payload = {
        carId: parseInt(carId),
        startAt: rawStart.length === 16 ? rawStart : rawStart.substring(0, 16),
        endAt: rawEnd.length === 16 ? rawEnd : rawEnd.substring(0, 16)
    };

    try {
        const res = await secureFetch('/api/bookings', { method: 'POST', body: JSON.stringify(payload) });
        if (res.ok) {
            clearInterval(bookingTimerInterval);
            alert('Your booking is confirmed!');
            hideModal('bookingModal');
            window.location.hash = '#dashboard';
        } else {
            let err; try { err = await res.json(); } catch(e){}
            alert('Booking failed: ' + (err?.message || 'Dates are unavailable.'));
        }
    } catch (e) { console.error(e); }
}

// ==========================================
// DASHBOARD (MY BOOKINGS)
// ==========================================
async function loadMyBookings() {
    const grid = document.getElementById('myBookings');
    if (!grid) return;
    grid.innerHTML = '<p style="text-align:center; grid-column: 1/-1;">Loading your dashboard...</p>';

    try {
        const res = await secureFetch('/api/bookings/my-bookings');
        if (!res.ok) { grid.innerHTML = '<p style="color: red;">Error fetching your bookings.</p>'; return; }

        const data = await res.json();
        const bookings = data.data || data;

        if (!Array.isArray(bookings) || bookings.length === 0) {
            grid.innerHTML = '<div style="text-align:center; grid-column: 1/-1; padding: 60px 20px; background:#f8f9fa; border-radius:8px;"><h3>You have no active reservations.</h3><br> <a href="#cars" class="btn primary-btn">Start Searching</a></div>';
            return;
        }

        grid.innerHTML = '';
        bookings.forEach(b => {
            const statusColor = b.status === 'CONFIRMED' ? '#28a745' : (b.status === 'CANCELLED' ? '#dc3545' : '#6c757d');
            grid.innerHTML += `
                <div class="car-card" style="padding: 1.5rem; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); margin-bottom:20px;">
                    <h3 style="border-bottom: 2px solid #eee; padding-bottom: 10px; margin-top:0; margin-bottom: 15px;">Reservation #${b.id}</h3>
                    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:15px;">
                        <p style="margin:0;"><strong>Vehicle:</strong> <br>${b.carBrand} ${b.carModel}</p>
                        <p style="margin:0;"><strong>Status:</strong> <br><span style="color: white; background: ${statusColor}; padding: 3px 8px; border-radius:4px; font-size: 0.85rem; font-weight: bold;">${b.status}</span></p>
                        <p style="margin:0;"><strong>Pickup:</strong> <br>${new Date(b.startAt).toLocaleString()}</p>
                        <p style="margin:0;"><strong>Return:</strong> <br>${new Date(b.endAt).toLocaleString()}</p>
                    </div>
                    <p style="margin-top:20px; font-size:1.2rem; background: #f8f9fa; padding:10px; border-radius:6px; display:inline-block;"><strong>Total Cost:</strong> <span style="color:#0056b3; font-weight:bold;">$${b.totalAmount}</span></p>
                    ${b.status === 'CONFIRMED' ? `<button class="btn" style="background:transparent; border: 2px solid #dc3545; color: #dc3545; margin-top:15px; width: 100%; display:block;" onclick="cancelBooking(${b.id})">Cancel Reservation</button>` : ''}
                </div>
            `;
        });
    } catch (e) {}
}

window.cancelBooking = async (bookingId) => {
    if(!confirm("Are you sure you want to cancel this reservation?")) return;
    try {
        const res = await secureFetch(`/api/bookings/${bookingId}/cancel`, {
            method: 'POST', body: JSON.stringify({ cancelReason: 'User requested cancellation' })
        });
        if (res.ok) { alert('Reservation cancelled.'); loadMyBookings(); }
        else alert('Failed to cancel reservation.');
    } catch (e) { console.error(e); }
}

// ==========================================
// AUTHENTICATION & SECURITY
// ==========================================
function setupEventListeners() {
    document.getElementById('openLogin')?.addEventListener('click', () => showModal('loginModal'));
    document.getElementById('closeLogin')?.addEventListener('click', () => hideModal('loginModal'));
    document.getElementById('openRegister')?.addEventListener('click', () => showModal('registerModal'));
    document.getElementById('closeRegister')?.addEventListener('click', () => hideModal('registerModal'));
    document.getElementById('loginForm')?.addEventListener('submit', handleLogin);
    document.getElementById('registerForm')?.addEventListener('submit', handleRegister);
}

function isAuthenticated() { return localStorage.getItem('accessToken') !== null; }
window.handleLogout = () => { localStorage.clear(); window.location.hash = '#home'; location.reload(); }

function setupNavbar() {
    const authContainer = document.getElementById('authButtons');
    if (!authContainer) return;

    if (isAuthenticated()) {
        const name = localStorage.getItem('userFullName') || 'Account';
        authContainer.innerHTML = `
            <span style="color:#0056b3; font-weight:bold; margin-right:15px;">Welcome, ${name}</span>
            <button class="btn login-btn" style="border:2px solid #dc3545 !important; color:#dc3545 !important;" onclick="handleLogout()">Logout</button>
        `;
    } else {
        authContainer.innerHTML = `
            <button class="btn login-btn" id="openLogin">Sign In</button>
            <button class="btn register-btn" id="openRegister">Register</button>
        `;
        // Re-attach event listeners after DOM injection
        document.getElementById('openLogin').addEventListener('click', () => showModal('loginModal'));
        document.getElementById('openRegister').addEventListener('click', () => showModal('registerModal'));
    }
}

async function secureFetch(url, options = {}) {
    const token = localStorage.getItem('accessToken');
    options.headers = { ...options.headers, 'Content-Type': 'application/json' };
    if (token) options.headers['Authorization'] = `Bearer ${token}`;

    const response = await fetch(url, options);
    // Auto-logout if the token expires (Now properly handling HTTP 401s from the backend)
    if (response.status === 401) handleLogout();
    return response;
}

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (res.ok) {
            const data = await res.json();
            localStorage.setItem('accessToken', data.data?.accessToken || data.accessToken);
            localStorage.setItem('userFullName', data.data?.user?.fullName || data.user?.fullName || 'User');
            hideModal('loginModal');
            document.getElementById('loginForm').reset();
            window.location.hash = '#dashboard';
            window.location.reload();
        } else {
            alert('Login failed. Please check your credentials.');
        }
    } catch(err) {
        console.error(err);
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const payload = {
        fullName: document.getElementById('registerName').value,
        email: document.getElementById('registerEmail').value,
        phone: document.getElementById('registerPhone').value,
        password: document.getElementById('registerPassword').value
    };

    try {
        const res = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            alert('Account created! Please log in.');
            hideModal('registerModal');
            showModal('loginModal');
        } else {
            alert('Registration failed. Email might already be in use.');
        }
    } catch(err) {
        console.error(err);
    }
}