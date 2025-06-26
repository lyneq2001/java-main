document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) return;

    fetch(`/api/authors/${id}`)
        .then(r => r.json())
        .then(author => {
            const div = document.getElementById('author');
            div.innerHTML = `${author.photoUrl ? `<img src="${author.photoUrl}" class="img-fluid rounded mb-3" style="max-height:200px">` : ''}` +
                `<h2 class="h4 mb-2">${author.name}</h2>` +
                `${author.nationality ? `<p class="text-muted">${author.nationality}</p>` : ''}` +
                `${author.yearOfBirth ? `<p class="text-muted">Born ${author.yearOfBirth}</p>` : ''}` +
                `${author.yearOfDeath ? `<p class="text-muted">Died ${author.yearOfDeath}</p>` : ''}` +
                `${author.website ? `<p><a href="${author.website}" target="_blank">${author.website}</a></p>` : ''}` +
                `${author.description ? `<p>${author.description}</p>` : ''}`;
        });

    fetch(`/api/books/author/${id}`)
        .then(r => r.json())
        .then(data => {
            const list = document.getElementById('authorBooks');
            list.innerHTML = '';
            data.forEach(b => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.innerHTML = `<a href="book.html?id=${b.id}">${b.title}</a>`;
                list.appendChild(li);
            });
        });
});
