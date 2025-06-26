package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.repository.BookRepository;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.PublisherRepository;
import com.example.library.repository.GenreRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.ReviewRepository;
import com.example.library.model.Author;
import com.example.library.model.Publisher;
import com.example.library.model.Genre;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final GenreRepository genreRepository;
    private final LoanRepository loanRepository;
    private final ReviewRepository reviewRepository;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       PublisherRepository publisherRepository,
                       GenreRepository genreRepository,
                       LoanRepository loanRepository,
                       ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.genreRepository = genreRepository;
        this.loanRepository = loanRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    public Book save(Book book) {
        if (book.getAuthor() != null) {
            if (book.getAuthor().getId() != null) {
                book.setAuthor(resolveAuthor(book.getAuthor().getId()));
            } else if (book.getAuthor().getFirstName() != null && book.getAuthor().getLastName() != null) {
                book.setAuthor(authorRepository.save(book.getAuthor()));
            }
        }
        if (book.getPublisher() != null && book.getPublisher().getId() != null) {
            book.setPublisher(resolvePublisher(book.getPublisher().getId()));
        }
        if (book.getGenres() != null) {
            book.setGenres(resolveGenres(book.getGenres()));
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, Book updated) {
        Book book = findById(id);
        Long newId = updated.getId();
        if (newId != null && !newId.equals(id)) {
            if (bookRepository.existsById(newId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "ID already exists");
            }
            Book newBook = new Book();
            newBook.setId(newId);
            newBook.setTitle(updated.getTitle() != null ? updated.getTitle() : book.getTitle());
            newBook.setAuthor(resolveAuthor(updated.getAuthor() != null ? updated.getAuthor().getId() : book.getAuthor().getId()));
            if (updated.getPublisher() != null) {
                Long pubId = updated.getPublisher().getId();
                newBook.setPublisher(resolvePublisher(pubId));
            } else {
                newBook.setPublisher(book.getPublisher());
            }
            newBook.setGenres(updated.getGenres() != null ? resolveGenres(updated.getGenres()) : book.getGenres());
            Book savedBook = bookRepository.save(newBook);

            loanRepository.findByBookId(id)
                    .forEach(l -> {
                        l.setBook(savedBook);
                        loanRepository.save(l);
                    });
            reviewRepository.findByBookId(id)
                    .forEach(r -> {
                        r.setBook(savedBook);
                        reviewRepository.save(r);
                    });

            bookRepository.deleteById(id);
            return savedBook;
        } else {
            book.setTitle(updated.getTitle());
            if (updated.getAuthor() != null) {
                if (updated.getAuthor().getId() != null) {
                    book.setAuthor(resolveAuthor(updated.getAuthor().getId()));
                } else if (updated.getAuthor().getFirstName() != null && updated.getAuthor().getLastName() != null) {
                    book.setAuthor(authorRepository.save(updated.getAuthor()));
                }
            }
            if (updated.getPublisher() != null) {
                Long pubId = updated.getPublisher().getId();
                book.setPublisher(resolvePublisher(pubId));
            }
            if (updated.getYearPublished() != null) {
                book.setYearPublished(updated.getYearPublished());
            }
            if (updated.getDescription() != null) {
                book.setDescription(updated.getDescription());
            }
            if (updated.getPages() != null) {
                book.setPages(updated.getPages());
            }
            if (updated.getIsbn() != null) {
                book.setIsbn(updated.getIsbn());
            }
            if (updated.getLanguage() != null) {
                book.setLanguage(updated.getLanguage());
            }
            if (updated.getEdition() != null) {
                book.setEdition(updated.getEdition());
            }
            if (updated.getSeries() != null) {
                book.setSeries(updated.getSeries());
            }
            if (updated.getVolume() != null) {
                book.setVolume(updated.getVolume());
            }
            if (updated.getGenres() != null) {
                book.setGenres(resolveGenres(updated.getGenres()));
            }
            return bookRepository.save(book);
        }
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public List<Book> findByGenreSorted(Long genreId) {
        return bookRepository.findByGenreSorted(genreId);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> advancedSearch(String title, Long authorId, Integer year,
                                     Long genreId, String keyword) {
        return bookRepository.advancedSearch(title, authorId, year, genreId, keyword);
    }

    public List<PublicBook> findPublicBooks() {
        return bookRepository.findAll().stream()
                .map(this::toPublicBook)
                .toList();
    }

    public PublicBook getPublicBook(Long id) {
        Book b = findById(id);
        return toPublicBook(b);
    }

    private PublicBook toPublicBook(Book b) {
        int active = loanRepository.findByBookIdAndReturnedDateIsNull(b.getId()).size();
        boolean available = b.getCopies() == null || active < b.getCopies();
        java.time.LocalDate next = loanRepository
                .findFirstByBookIdAndReturnedDateIsNullOrderByDueDateAsc(b.getId())
                .map(Loan::getDueDate).orElse(null);
        int availableCopies = (b.getCopies() == null ? 1 : b.getCopies()) - active;
        if (availableCopies < 0) availableCopies = 0;
        long ratingCount = reviewRepository.countByBookId(b.getId());
        Double avg = reviewRepository.averageRating(b.getId());
        double rating = avg != null ? avg : 0.0;
        return new PublicBook(b, available, availableCopies, next, ratingCount, rating);
    }

    private Author resolveAuthor(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }

    private Publisher resolvePublisher(Long pubId) {
        return publisherRepository.findById(pubId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found"));
    }

    private Set<Genre> resolveGenres(Set<Genre> genres) {
        Set<Genre> result = new java.util.HashSet<>();
        for (Genre g : genres) {
            if (g.getId() != null) {
                result.add(genreRepository.findById(g.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found")));
            }
        }
        return result;
    }

    public record PublicBook(
            Book book,
            boolean available,
            int availableCopies,
            java.time.LocalDate nextReturnDate,
            long ratingCount,
            double averageRating
    ) {}
}
