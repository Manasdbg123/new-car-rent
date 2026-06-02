document.addEventListener('DOMContentLoaded', () => {
    initApp();
    setupEventListeners();
    window.addEventListener('hashchange', handleRouting);
    handleRouting();
});

let globalVehicles = []; // Contains BOTH Cars and Bikes from DB

function initApp() {
    setupNavbar();
    fetchInventory(); // Fetch once on load
}

// Hero Search Widget Logic
window.executeHeroSearch = (forcedCity, forcedType) => {
    const loc = forcedCity || document.getElementById('heroLocation').value;
    const type = forcedType || document.getElementById('heroType').value;

    // Set the dropdowns in the target views
    const carCityFilter = document.getElementById('carCityFilter');
    const bikeCityFilter = document.getElementById('bikeCityFilter');

    if (type === 'cars' && carCityFilter) {
        carCityFilter.value = loc;
    } else if (type === 'bikes' && bikeCityFilter) {
        bikeCityFilter.value = loc;
    }

    // Route to the page
    window.location.hash = '#' + type;
    filterVehicles(); // Trigger filter
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

function showModal(id) { document.getElementById(id).style.display = 'flex'; }
function hideModal(id) { document.getElementById(id).style.display = 'none'; }
window.closeBookingModal = () => hideModal('bookingModal');

// DYNAMIC MODAL FOR ADDITIONAL CONDITIONS LOGIC
window.openInfoModal = (vehicleType, brandName) => {
    const title = document.getElementById('infoModalTitle');
    const body = document.getElementById('infoModalBody');
    if(!title || !body) return;

    title.innerText = `Rental Conditions: ${brandName}`;

    if(vehicleType === 'BIKE') {
        body.innerHTML = `
            <p style="margin-top:0;"><strong>🏍️ Two-Wheeler Deployment Protocols:</strong></p>
            <ul style="padding-left:20px; line-height:1.6;">
                <li><strong>Helmets Provided:</strong> Includes one complimentary safety helmet; secondary elements charged during physical collection.</li>
                <li><strong>Security Hold:</strong> A temporary holding credit authorization of $100 is handled at the logistics lot.</li>
                <li><strong>Speed Regulations:</strong> Electronic tracking limit governed at a maximum tier threshold of 100 km/h.</li>
                <li><strong>Insurance Scope:</strong> Full comprehensive insurance covers accidental damage exclusions except structural tires.</li>
            </ul>
        `;
    } else {
        body.innerHTML = `
            <p style="margin-top:0;"><strong>🚗 Four-Wheeler Fleet Regulations:</strong></p>
            <ul style="padding-left:20px; line-height:1.6;">
                <li><strong>Unlimited Mileage:</strong> Drive across boundaries with infinite transit operations inside federal lines.</li>
                <li><strong>Security Hold:</strong> A temporary holding credit authorization of $300 is processed at dispatch desks.</li>
                <li><strong>Roadside Coverage:</strong> Includes standard full platform breakdown dispatch and vehicle swaps.</li>
                <li><strong>Fuel Terms:</strong> Vehicle must be dropped with full tank status to bypass service refueling penalties.</li>
            </ul>
        `;
    }
    showModal('infoModal');
};
window.closeInfoModal = () => hideModal('infoModal');

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
// UNIFIED INVENTORY LOGIC
// ==========================================
async function fetchInventory() {
    try {
        const res = await fetch('/api/cars/available?size=200');
        if (!res.ok) throw new Error("Backend infrastructure validation error");
        const data = await res.json();

        const vehicles = data.data?.content || data.content || data.data || data;

        if (Array.isArray(vehicles)) {
            globalVehicles = vehicles;
            populateDropdowns(vehicles);
            filterVehicles();
        }
    } catch (e) {
        console.error("Failed to execute dynamic asset inventory population fetch", e);
    }
}

function populateDropdowns(vehicles) {
    const defaultCities = ['Bangalore', 'Chennai', 'Delhi', 'Hyderabad', 'Mumbai', 'Pune'];
    const dbCities = vehicles.map(v => v.city).filter(Boolean);
    const allCities = [...new Set([...defaultCities, ...dbCities])].sort();

    let options = '<option value="all">All Locations</option>';
    allCities.forEach(c => { options += `<option value="${c}">${c}</option>`; });

    const carCityFilter = document.getElementById('carCityFilter');
    const bikeCityFilter = document.getElementById('bikeCityFilter');

    if (carCityFilter) carCityFilter.innerHTML = options;
    if (bikeCityFilter) bikeCityFilter.innerHTML = options;
}

window.filterVehicles = () => {
    let hash = window.location.hash || '#home';
    if(hash !== '#cars' && hash !== '#bikes') return;

    let isCarPage = hash === '#cars';
    let type = isCarPage ? 'CAR' : 'BIKE';
    let citySelect = document.getElementById(isCarPage ? 'carCityFilter' : 'bikeCityFilter');
    let sortSelect = document.getElementById(isCarPage ? 'carSortFilter' : 'bikeSortFilter');

    let city = citySelect ? citySelect.value : 'all';
    let sort = sortSelect ? sortSelect.value : 'asc';

    let filtered = globalVehicles.filter(v => {
        let actualType = v.vehicleType;
        if (!actualType) {
            actualType = (v.seats <= 2) ? 'BIKE' : 'CAR';
        }
        return actualType === type;
    });

    if (city !== 'all') {
        filtered = filtered.filter(v => v.city === city);
    }

    filtered.sort((a, b) => {
        return sort === 'asc' ? a.dailyRate - b.dailyRate : b.dailyRate - a.dailyRate;
    });

    renderVehicles(filtered, isCarPage ? 'carList' : 'bikeList');
}

function renderVehicles(vehicles, containerId) {
    const grid = document.getElementById(containerId);
    if (!grid) return;
    grid.innerHTML = '';

    if (vehicles.length === 0) {
        grid.innerHTML = '<div style="grid-column: 1/-1; text-align:center; padding: 40px; background:#f8f9fa; border-radius:8px;"><h3>No vehicles found for this location.</h3><p>Try changing your search filters.</p></div>';
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
// MY BOOKINGS & DASHBOARD ACTIONS
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

window.openBookingModal = (carId, carName) => {
    if (!isAuthenticated()) { alert('Please login to continue.'); showModal('loginModal'); return; }
    document.getElementById('bookingCarId').value = carId;
    document.getElementById('bookingCarName').innerText = `Reserve: ${carName}`;
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
            alert('Your booking is confirmed!');
            hideModal('bookingModal');
            window.location.hash = '#dashboard';
        } else {
            let err; try { err = await res.json(); } catch(e){}
            alert('Booking failed: ' + (err?.message || 'Dates are unavailable. Please select different dates.'));
        }
    } catch (e) { console.error(e); }
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

// SECURITY LOGISTICS FORM HANDLERS
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
        } else alert('Login failed. Please check your credentials.');
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
            alert('Account created! Please log in.');
            hideModal('registerModal');
            showModal('loginModal');
        } else alert('Registration failed. Email might already be in use.');
    } catch(err) {}
}