document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    if (!id) return;
    Promise.all([
        fetch(`/api/books/${id}`).then(r=>r.json()),
        fetch(`/api/books/${id}/status`).then(r=>r.json())
    ]).then(([book,status])=>renderBook(book,status));
});

function renderBook(book,status) {
    const container = document.getElementById('book');
    const title = document.createElement('h2');
    title.className = 'h4 mb-3';
    title.innerText = book.title;
    container.appendChild(title);

    if(book.coverUrl){
        const img=document.createElement('img');
        img.src=book.coverUrl;
        img.className='img-fluid mb-3';
        img.style.maxHeight='300px';
        container.appendChild(img);
    }

    const author = document.createElement('p');
    author.innerHTML = `<strong>Author:</strong> <a href="author.html?id=${book.author.id}">${book.author.name}</a>`;
    container.appendChild(author);

    if(book.yearPublished){
        const y=document.createElement('p');
        y.innerHTML=`<strong>Year:</strong> ${book.yearPublished}`;
        container.appendChild(y);
    }

    if(book.isbn){
        const i=document.createElement('p');
        i.innerHTML=`<strong>ISBN:</strong> ${book.isbn}`;
        container.appendChild(i);
    }

    if(book.language){
        const l=document.createElement('p');
        l.innerHTML=`<strong>Language:</strong> ${book.language}`;
        container.appendChild(l);
    }

    if(book.edition){
        const e=document.createElement('p');
        e.innerHTML=`<strong>Edition:</strong> ${book.edition}`;
        container.appendChild(e);
    }

    if(book.series){
        const s=document.createElement('p');
        s.innerHTML=`<strong>Series:</strong> ${book.series}`;
        container.appendChild(s);
    }

    if(book.volume){
        const v=document.createElement('p');
        v.innerHTML=`<strong>Volume:</strong> ${book.volume}`;
        container.appendChild(v);
    }

    if(book.pages){
        const p=document.createElement('p');
        p.innerHTML=`<strong>Pages:</strong> ${book.pages}`;
        container.appendChild(p);
    }

    if(book.description){
        const d=document.createElement('p');
        d.textContent=book.description;
        container.appendChild(d);
    }

    if (book.publisher) {
        const pub = document.createElement('p');
        pub.innerHTML = `<strong>Publisher:</strong> ${book.publisher.name}`;
        container.appendChild(pub);
    }

    if (book.genres && book.genres.length > 0) {
        const g = document.createElement('p');
        g.innerHTML = `<strong>Genres:</strong> ${book.genres.map(gen => gen.name).join(', ')}`;
        container.appendChild(g);
    }

    if (book.reviews && book.reviews.length > 0) {
        const h3 = document.createElement('h3');
        h3.className = 'h5 mt-4';
        h3.innerText = 'Reviews';
        container.appendChild(h3);
        const ul = document.createElement('ul');
        ul.className = 'list-group';
        book.reviews.forEach(rev => {
            const li = document.createElement('li');
            li.className = 'list-group-item';
            li.innerText = `${rev.rating}/5 - ${rev.comment ? rev.comment : ''}`;
            ul.appendChild(li);
        });
        container.appendChild(ul);
    }

    if(status){
        if(status.ratingCount>0){
            const rating=document.createElement('p');
            rating.innerHTML=`<strong>Rating:</strong> ${status.averageRating.toFixed(1)}/5 (${status.ratingCount})`;
            container.appendChild(rating);
        }
        const info=document.createElement('p');
        let text=`Available copies: ${status.availableCopies}`;
        if(!status.available && status.nextReturnDate){
            text+=` (next return ${new Date(status.nextReturnDate).toLocaleDateString()})`;
        }
        info.innerHTML=`<strong>Status:</strong> ${text}`;
        container.appendChild(info);
    }
}
