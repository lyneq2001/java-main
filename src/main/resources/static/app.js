let authHeader = null;
let currentUser = null;

function showAuth() {
    document.getElementById('auth').style.display = 'block';
}

function logout() {
    authHeader = null;
    currentUser = null;
    document.getElementById('dashboard').style.display = 'none';
    document.getElementById('auth').style.display = 'none';
    document.getElementById('home').style.display = 'block';
    document.getElementById('loginBtn').style.display = 'inline-block';
    document.getElementById('logoutBtn').style.display = 'none';
    document.getElementById('browseLink').style.display = 'none';
    loadPublicBooks();
}

function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    authHeader = null;
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, password})
    }).then(r => r.ok ? r.json() : Promise.reject()).then(data => {
        if (data && data.token) {
            authHeader = 'Bearer ' + data.token;
            fetchUser().then(() => {
                document.getElementById('home').style.display = 'none';
                document.getElementById('auth').style.display = 'none';
                document.getElementById('loginBtn').style.display = 'none';
                document.getElementById('logoutBtn').style.display = 'inline-block';
                document.getElementById('browseLink').style.display = 'inline-block';
                document.getElementById('dashboard').style.display = 'block';
                loadData();
            });
        } else {
            alert('Login failed');
        }
    }).catch(() => alert('Login failed'));
}

function register() {
    const username = document.getElementById('regUsername').value;
    const password = document.getElementById('regPassword').value;
    fetch('/api/auth/register', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, password})
    }).then(r => {
        if (r.ok) {
            alert('Registered');
        } else {
            alert('Registration failed');
        }
    });
}

function headers() {
    return authHeader ? { 'Authorization': authHeader, 'Content-Type': 'application/json' } : { 'Content-Type': 'application/json' };
}

function fetchUser() {
    return fetch('/api/users/me', { headers: { 'Authorization': authHeader } })
        .then(r => r.json())
        .then(u => {
            currentUser = u;
            document.getElementById('welcome').innerText = `Hello ${u.username} (${u.role})`;
            document.getElementById('adminPanel').style.display = u.role === 'ADMIN' ? 'block' : 'none';
        });
}

function loadAuthors() {
    fetch('/api/authors', { headers: { 'Authorization': authHeader } })
        .then(r => r.json())
        .then(data => {
            const list = document.getElementById('authorList');
            list.innerHTML = '';
            data.forEach(a => {
                const li = document.createElement('li');
                const span = document.createElement('span');
                span.innerHTML = `<a href="author.html?id=${a.id}">${a.id} - ${a.name}</a>`;
                li.appendChild(span);
                if (currentUser && currentUser.role === 'ADMIN') {
                    const edit = document.createElement('button');
                    edit.className = 'btn btn-sm btn-outline-primary ms-2';
                    edit.textContent = 'Edit';
                    edit.onclick = () => editAuthor(a.id, a.name);
                    li.appendChild(edit);
                    const del = document.createElement('button');
                    del.className = 'btn btn-sm btn-outline-danger ms-2';
                    del.textContent = 'Delete';
                    del.onclick = () => deleteAuthor(a.id);
                    li.appendChild(del);
                }
                list.appendChild(li);
            });
        });
}

function addAuthor() {
    const name = document.getElementById('authorName').value;
    fetch('/api/authors', {
        method: 'POST',
        headers: headers(),
        body: JSON.stringify({ name })
    }).then(r => {
        if (r.ok) {
            loadAuthors();
        } else {
            alert('Failed to add author');
        }
    });
}

function loadBooks() {
    fetch('/api/books', { headers: headers() })
        .then(r => r.json())
        .then(data => {
            const list = document.getElementById('bookList');
            list.innerHTML = '';
            data.forEach(b => {
                const li = document.createElement('li');
                const span = document.createElement('span');
                span.innerHTML = `<a href="book.html?id=${b.id}">${b.id} - ${b.title} (author ${b.author.id})</a>`;
                li.appendChild(span);
                if (currentUser && currentUser.role === 'ADMIN') {
                    const edit = document.createElement('button');
                    edit.className = 'btn btn-sm btn-outline-primary ms-2';
                    edit.textContent = 'Edit';
                    edit.onclick = () => editBook(b.id, b.title, b.author.id);
                    li.appendChild(edit);
                    const del = document.createElement('button');
                    del.className = 'btn btn-sm btn-outline-danger ms-2';
                    del.textContent = 'Delete';
                    del.onclick = () => deleteBook(b.id);
                    li.appendChild(del);
                }
                list.appendChild(li);
            });
        });
}

function loadPublicBooks() {
    fetch('/api/books')
        .then(r => r.json())
        .then(data => {
            const list = document.getElementById('publicBookList');
            if (!list) return;
            list.innerHTML = '';
            data.forEach(b => {
                const li = document.createElement('li');
                const link = document.createElement('a');
                link.href = `book.html?id=${b.id}`;
                link.textContent = `${b.title} by ${b.author.name}`;
                li.appendChild(link);
                list.appendChild(li);
            });
        });
}

function addBook() {
    const title = document.getElementById('bookTitle').value;
    const authorId = document.getElementById('bookAuthorId').value;
    fetch('/api/books', {
        method: 'POST',
        headers: headers(),
        body: JSON.stringify({ title, author: { id: authorId } })
    }).then(r => {
        if (r.ok) {
            loadBooks();
        } else {
            alert('Failed to add book');
        }
    });
}

function loadData() {
    loadAuthors();
    loadBooks();
    loadLoans();
    if (currentUser.role === 'ADMIN') {
        loadUsers();
        loadAllLoans();
    }
}

function loadLoans() {
    fetch('/api/loans', { headers: { 'Authorization': authHeader } })
        .then(r => r.json())
        .then(data => {
            const list = document.getElementById('loanList');
            list.innerHTML = '';
            data.forEach(l => {
                const li = document.createElement('li');
                li.textContent = `${l.id} - book ${l.book.id}`;
                const btn = document.createElement('button');
                btn.textContent = 'Return';
                btn.onclick = () => returnLoan(l.id);
                li.appendChild(btn);
                list.appendChild(li);
            });
        });
}

function borrowBook() {
    const bookId = document.getElementById('loanBookId').value;
    fetch('/api/loans', {
        method: 'POST',
        headers: headers(),
        body: JSON.stringify({ bookId })
    }).then(r => {
        if (r.ok) {
            loadLoans();
        } else {
            alert('Borrow failed');
        }
    });
}

function returnLoan(id) {
    fetch(`/api/loans/${id}/return`, { method: 'PUT', headers: headers() })
        .then(() => loadLoans());
}


function loadUsers() {
    fetch('/api/users', { headers: { 'Authorization': authHeader } })
        .then(r => r.json())
        .then(data => {
            const list = document.getElementById('userList');
            list.innerHTML = '';
            data.forEach(u => {
                const li = document.createElement('li');
                li.textContent = `${u.id} - ${u.username} (${u.role})`;
                const edit = document.createElement('button');
                edit.className = 'btn btn-sm btn-outline-primary ms-2';
                edit.textContent = 'Edit';
                edit.onclick = () => editUser(u.id, u.username, u.role);
                const del = document.createElement('button');
                del.className = 'btn btn-sm btn-outline-danger ms-2';
                del.textContent = 'Delete';
                del.onclick = () => deleteUser(u.id);
                li.appendChild(edit);
                li.appendChild(del);
                list.appendChild(li);
            });
        });
}

function editUser(id, username, role) {
    const newUsername = prompt('New username', username);
    if (newUsername === null) return;
    const newRole = prompt('Role', role);
    if (!newRole) return;
    fetch(`/api/users/${id}`, {
        method: 'PUT',
        headers: headers(),
        body: JSON.stringify({ username: newUsername, role: newRole })
    }).then(r => {
        if (r.ok) loadUsers();
        else alert('Edit failed');
    });
}

function deleteUser(id) {
    if (!confirm('Delete user?')) return;
    fetch(`/api/users/${id}`, { method: 'DELETE', headers: { 'Authorization': authHeader } })
        .then(() => loadUsers());
}

function loadAllLoans() {
    fetch('/api/loans', { headers: { 'Authorization': authHeader } })
        .then(r => r.json())
        .then(data => {
            const list = document.getElementById('allLoanList');
            list.innerHTML = '';
            data.forEach(l => {
                const li = document.createElement('li');
                li.textContent = `${l.id} - user ${l.user.id} book ${l.book.id}`;
                list.appendChild(li);
            });
        });
}

function showHome() {
    document.getElementById('dashboard').style.display = 'none';
    document.getElementById('home').style.display = 'block';
    loadPublicBooks();
}

function openAuthorModal() {
    loadAuthors();
    const modal = new bootstrap.Modal(document.getElementById('authorModal'));
    modal.show();
}

function openBookModal() {
    loadBooks();
    const modal = new bootstrap.Modal(document.getElementById('bookModal'));
    modal.show();
}

function editAuthor(id, currentName) {
    const newId = prompt('New id', id);
    if (newId === null) return;
    const name = prompt('New name', currentName);
    if (!name) return;
    fetch(`/api/authors/${id}`, {
        method: 'PUT',
        headers: headers(),
        body: JSON.stringify({ id: newId, name })
    }).then(r => {
        if (r.ok) loadAuthors();
        else if (r.status === 409) alert('ID already exists');
        else alert('Edit failed');
    });
}

function deleteAuthor(id) {
    fetch(`/api/authors/${id}`, { method: 'DELETE', headers: { 'Authorization': authHeader } })
        .then(() => loadAuthors());
}

function editBook(id, title, authorId) {
    const newId = prompt('New id', id);
    if (newId === null) return;
    const newTitle = prompt('New title', title);
    if (!newTitle) return;
    const newAuthor = prompt('Author id', authorId);
    if (!newAuthor) return;
    fetch(`/api/books/${id}`, {
        method: 'PUT',
        headers: headers(),
        body: JSON.stringify({ id: newId, title: newTitle, author: { id: newAuthor } })
    }).then(r => {
        if (r.ok) loadBooks();
        else if (r.status === 409) alert('ID already exists');
        else alert('Edit failed');
    });
}

function deleteBook(id) {
    fetch(`/api/books/${id}`, { method: 'DELETE', headers: { 'Authorization': authHeader } })
        .then(() => loadBooks());
}

document.addEventListener('DOMContentLoaded', loadPublicBooks);
document.getElementById('browseLink').addEventListener('click', (e) => { e.preventDefault(); showHome(); });

