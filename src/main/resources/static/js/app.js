document.addEventListener('DOMContentLoaded', () => {
    initApp();
    setupEventListeners();
    window.addEventListener('hashchange', handleRouting);
    handleRouting();
});

let globalCars = []; // Cache cars for instant city filtering

function initApp() {
    setupNavbar();
}

// ==========================================
// SPA ROUTER ENGINE
// ==========================================
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

    if (hash === '#cars') loadCars();
    else if (hash === '#bikes') loadBikes();
    else if (hash === '#dashboard') loadMyBookings();
}

// ==========================================
// MODALS & AUTHENTICATION
// ==========================================
function showModal(id) { document.getElementById(id).style.display = 'flex'; }
function hideModal(id) { document.getElementById(id).style.display = 'none'; }
window.closeBookingModal = () => hideModal('bookingModal');

function setupEventListeners() {
    document.getElementById('openLogin')?.addEventListener('click', () => showModal('loginModal'));
    document.getElementById('closeLogin')?.addEventListener('click', () => hideModal('loginModal'));
    document.getElementById('openRegister')?.addEventListener('click', () => showModal('registerModal'));
    document.getElementById('closeRegister')?.addEventListener('click', () => hideModal('registerModal'));
    document.getElementById('loginForm')?.addEventListener('submit', handleLogin);
    document.getElementById('registerForm')?.addEventListener('submit', handleRegister);
}

function isAuthenticated() { return localStorage.getItem('accessToken') !== null; }

window.handleLogout = () => {
    localStorage.clear();
    window.location.hash = '#home';
    location.reload();
}

function setupNavbar() {
    const authContainer = document.getElementById('authButtons');
    if (!authContainer) return;
    if (isAuthenticated()) {
        authContainer.innerHTML = `
            <a href="#dashboard" class="btn primary-btn" style="text-decoration:none; margin-right: 15px;">Dashboard</a>
            <button class="btn login-btn" onclick="handleLogout()">Logout</button>
        `;
    } else {
        authContainer.innerHTML = `
            <button class="btn login-btn" id="openLogin">Login</button>
            <button class="btn register-btn" id="openRegister">Register</button>
        `;
        document.getElementById('openLogin').addEventListener('click', () => showModal('loginModal'));
        document.getElementById('openRegister').addEventListener('click', () => showModal('registerModal'));
    }
}

async function secureFetch(url, options = {}) {
    const token = localStorage.getItem('accessToken');
    options.headers = { ...options.headers, 'Content-Type': 'application/json' };
    if (token) options.headers['Authorization'] = `Bearer ${token}`;
    const response = await fetch(url, options);
    if (response.status === 401) handleLogout();
    return response;
}

// ==========================================
// CARS & CITY FILTER LOGIC
// ==========================================
async function loadCars() {
    const grid = document.getElementById('carList');
    if (!grid) return;

    // Only fetch if we haven't already, to make filtering ultra-fast
    if (globalCars.length === 0) {
        grid.innerHTML = '<p style="text-align:center; grid-column: 1/-1;">Loading fleet...</p>';
        try {
            const res = await fetch('/api/cars/available');
            if (!res.ok) throw new Error("Backend block");
            const data = await res.json();
            const cars = data.data?.content || data.content || data.data || data;

            if (Array.isArray(cars)) {
                globalCars = cars;
                populateCityDropdown(cars);
            }
        } catch (e) {
            grid.innerHTML = '<p style="color:red; text-align:center; grid-column: 1/-1;">Network error fetching vehicles.</p>';
            return;
        }
    }
    renderCars(globalCars);
}

function populateCityDropdown(cars) {
    const select = document.getElementById('cityFilter');
    if (!select) return;

    // Extract unique cities
    const cities = [...new Set(cars.map(car => car.city))].sort();

    let options = '<option value="all">All Cities</option>';
    cities.forEach(city => {
        if(city) options += `<option value="${city}">${city}</option>`;
    });
    select.innerHTML = options;
}

window.filterCarsByCity = (selectedCity) => {
    if (selectedCity === 'all') {
        renderCars(globalCars);
    } else {
        const filtered = globalCars.filter(car => car.city === selectedCity);
        renderCars(filtered);
    }
}

function renderCars(carsToRender) {
    const grid = document.getElementById('carList');
    grid.innerHTML = '';

    if (carsToRender.length === 0) {
        grid.innerHTML = '<p style="text-align: center; grid-column: 1 / -1; font-size:1.2rem;">No vehicles available in this city.</p>';
        return;
    }

    carsToRender.forEach(car => {
        grid.innerHTML += `
            <div class="car-card">
                <img src="${car.imageUrl || '/images/bmw.jpg'}" alt="${car.brand}">
                <div class="car-info">
                    <h3>${car.brand} ${car.model}</h3>
                    <p style="font-size: 0.9em; color: #555;">📍 ${car.city} | 💺 ${car.seats} Seats | ⚙️ ${car.transmission}</p>
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-top:15px;">
                        <span style="font-weight:bold; font-size:1.2rem; color:#007bff;">$${car.dailyRate}<small style="color:#555; font-size:0.8rem;">/day</small></span>
                        <button class="btn book-btn" onclick="openBookingModal(${car.id}, '${car.brand} ${car.model}')">Book Now</button>
                    </div>
                </div>
            </div>
        `;
    });
}

// ==========================================
// BIKES LOGIC (Frontend Mock Data)
// ==========================================
// ==========================================
// BIKES LOGIC (Frontend Mock Data)
// ==========================================
function loadBikes() {
    const grid = document.getElementById('bikeList');
    if (!grid) return;

    // Professional Mock Data for 6 Bikes across 6 Cities
    const bikes = [
        { id: 101, brand: "Ducati", model: "Panigale V4", city: "Bangalore", dailyRate: 85, type: "Superbike", img: "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
        { id: 102, brand: "Harley-Davidson", model: "Iron 883", city: "Delhi", dailyRate: 65, type: "Cruiser", img: "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
        { id: 103, brand: "Kawasaki", model: "Ninja ZX-10R", city: "Hyderabad", dailyRate: 75, type: "Sports", img: "https://images.unsplash.com/photo-1600865809758-1d2a136bf558?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
        { id: 104, brand: "BMW", model: "S1000RR", city: "Mumbai", dailyRate: 90, type: "Superbike", img: "https://images.unsplash.com/photo-1599819811279-d50645840993?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
        { id: 105, brand: "Triumph", model: "Bonneville T120", city: "Chennai", dailyRate: 55, type: "Classic", img: "https://images.unsplash.com/photo-1572051662446-246eb57827e8?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" },
        { id: 106, brand: "Royal Enfield", model: "Interceptor 650", city: "Pune", dailyRate: 40, type: "Cruiser", img: "https://images.unsplash.com/photo-1621008678604-580004924194?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" }
    ];

    grid.innerHTML = '';
    bikes.forEach(bike => {
        grid.innerHTML += `
            <div class="car-card">
                <img src="${bike.img}" alt="${bike.brand}">
                <div class="car-info">
                    <span style="background:#ff4757; color:white; padding:3px 8px; border-radius:12px; font-size:0.8rem; font-weight:bold; margin-bottom:10px; display:inline-block;">${bike.type}</span>
                    <h3 style="margin-top:0;">${bike.brand} ${bike.model}</h3>
                    <p style="font-size: 0.9em; color: #555;">📍 ${bike.city}</p>
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-top:15px;">
                        <span style="font-weight:bold; font-size:1.2rem; color:#007bff;">$${bike.dailyRate}<small style="color:#555; font-size:0.8rem;">/day</small></span>
                        <button class="btn book-btn" onclick="alert('Bike booking backend coming soon! Please book a car instead.')" style="background:#ff4757;">Reserve</button>
                    </div>
                </div>
            </div>
        `;
    });
}

// ==========================================
// MY BOOKINGS & ACTIONS
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
            grid.innerHTML = '<p style="text-align:center; grid-column: 1/-1;">You have no active reservations. <a href="#cars">Rent a vehicle</a> to get started!</p>';
            return;
        }

        grid.innerHTML = '';
        bookings.forEach(b => {
            const statusColor = b.status === 'CONFIRMED' ? '#28a745' : (b.status === 'CANCELLED' ? '#dc3545' : '#6c757d');
            grid.innerHTML += `
                <div class="car-card" style="padding: 1.5rem; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05);">
                    <h3 style="border-bottom: 2px solid #007bff; padding-bottom: 10px; margin-bottom: 15px;">Booking #${b.id}</h3>
                    <p><strong>Vehicle:</strong> ${b.carBrand} ${b.carModel}</p>
                    <p><strong>Pickup:</strong> ${new Date(b.startAt).toLocaleString()}</p>
                    <p><strong>Return:</strong> ${new Date(b.endAt).toLocaleString()}</p>
                    <p><strong>Total Cost:</strong> <span style="color:#007bff; font-weight:bold;">$${b.totalAmount}</span></p>
                    <p><strong>Status:</strong> <span style="color: ${statusColor}; font-weight: bold;">${b.status}</span></p>
                    ${b.status === 'CONFIRMED' ? `<button class="btn book-btn" style="background:#dc3545; margin-top:15px; width: 100%;" onclick="cancelBooking(${b.id})">Cancel Booking</button>` : ''}
                </div>
            `;
        });
    } catch (e) {}
}

window.openBookingModal = (carId, carName) => {
    if (!isAuthenticated()) { alert('Please login to book.'); showModal('loginModal'); return; }
    document.getElementById('bookingCarId').value = carId;
    document.getElementById('bookingCarName').innerText = `Book: ${carName}`;
    document.getElementById('startDate').value = '';
    document.getElementById('endDate').value = '';
    showModal('bookingModal');
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
            alert('Booking Confirmed Successfully!');
            hideModal('bookingModal');
            window.location.hash = '#dashboard';
        } else {
            let err; try { err = await res.json(); } catch(e){}
            alert('Booking failed: ' + (err?.message || 'Dates overlap or are invalid.'));
        }
    } catch (e) { console.error(e); }
}

window.cancelBooking = async (bookingId) => {
    if(!confirm("Cancel this booking?")) return;
    try {
        const res = await secureFetch(`/api/bookings/${bookingId}/cancel`, {
            method: 'POST', body: JSON.stringify({ cancelReason: 'User requested cancellation' })
        });
        if (res.ok) { alert('Booking cancelled.'); loadMyBookings(); }
        else alert('Failed to cancel booking.');
    } catch (e) { console.error(e); }
}

// ==========================================
// FORM SUBMISSION HANDLERS
// ==========================================
async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const res = await fetch('/api/auth/login', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ email, password }) });
        if (res.ok) {
            const data = await res.json();
            localStorage.setItem('accessToken', data.data?.accessToken || data.accessToken);
            localStorage.setItem('userFullName', data.data?.user?.fullName || data.user?.fullName || 'User');
            hideModal('loginModal');
            document.getElementById('loginForm').reset();
            window.location.hash = '#dashboard';
            window.location.reload();
        } else alert('Login failed. Invalid credentials.');
    } catch(err) {}
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
        const res = await fetch('/api/auth/register', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
        if (res.ok) {
            alert('Registration successful! Please log in.');
            hideModal('registerModal');
            showModal('loginModal');
        } else alert('Registration failed. Email may be in use.');
    } catch(err) {}
}