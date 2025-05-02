package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.ApiResponse;
import com.example.CollegeBackend.dto.BookRequest;
import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.dto.Role;
import com.example.CollegeBackend.model.Book;
import com.example.CollegeBackend.repository.BookRepository;
import com.example.CollegeBackend.utils.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book")

public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse> newBook(HttpServletRequest request, @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to add new book");
        }

        return new ResponseEntity<>(
                new ApiResponse(bookRepository
                        .save(new Book(book.getTitle(), book.getAuthor(), book.getIsbn(), book.isAvailable()))),
                HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllBooks(HttpServletRequest request) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to view all books");
        }

        return new ResponseEntity<>(new ApiResponse(bookRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse> getAvailableBooks(HttpServletRequest request) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to view available books");
        }

        return new ResponseEntity<>(new ApiResponse(bookRepository.findByAvailable(true)), HttpStatus.OK);
    }

    @GetMapping("/unavailable")
    public ResponseEntity<ApiResponse> getUnavailableBooks(HttpServletRequest request) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to view unavailable books");
        }

        return new ResponseEntity<>(new ApiResponse(bookRepository.findByAvailable(false)), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchBooks(HttpServletRequest request, @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search books");
        }

        return new ResponseEntity<>(new ApiResponse(bookRepository.findByTitleContainingIgnoreCase(book.getTitle())),
                HttpStatus.OK);
    }

    @GetMapping("/search/author")
    public ResponseEntity<ApiResponse> searchBooksByAuthor(HttpServletRequest request, @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search books by author");
        }

        return new ResponseEntity<>(new ApiResponse(bookRepository.findByAuthorContainingIgnoreCase(book.getAuthor())),
                HttpStatus.OK);
    }

    @GetMapping("/search/isbn")
    public ResponseEntity<ApiResponse> searchBooksByIsbn(HttpServletRequest request, @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search books by isbn");
        }

        return new ResponseEntity<>(new ApiResponse(bookRepository.findByIsbnContainingIgnoreCase(book.getIsbn())),
                HttpStatus.OK);
    }

    @GetMapping("/search/available")
    public ResponseEntity<ApiResponse> searchAvailableBooks(HttpServletRequest request, @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search available books");
        }

        return new ResponseEntity<>(
                new ApiResponse(bookRepository.findByAvailableAndTitleContainingIgnoreCase(true, book.getTitle())),
                HttpStatus.OK);
    }

    @GetMapping("/search/unavailable")
    public ResponseEntity<ApiResponse> searchUnavailableBooks(HttpServletRequest request,
            @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search unavailable books");
        }

        return new ResponseEntity<>(
                new ApiResponse(bookRepository.findByAvailableAndTitleContainingIgnoreCase(false, book.getTitle())),
                HttpStatus.OK);
    }

    @GetMapping("/search/available/author")
    public ResponseEntity<ApiResponse> searchAvailableBooksByAuthor(HttpServletRequest request,
            @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search available books by author");
        }

        return new ResponseEntity<>(
                new ApiResponse(bookRepository.findByAvailableAndAuthorContainingIgnoreCase(true, book.getAuthor())),
                HttpStatus.OK);
    }

    @GetMapping("/search/unavailable/author")
    public ResponseEntity<ApiResponse> searchUnavailableBooksByAuthor(HttpServletRequest request,
            @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search unavailable books by author");
        }

        return new ResponseEntity<>(
                new ApiResponse(bookRepository.findByAvailableAndAuthorContainingIgnoreCase(false, book.getAuthor())),
                HttpStatus.OK);
    }

    @GetMapping("/search/available/isbn")
    public ResponseEntity<ApiResponse> searchAvailableBooksByIsbn(HttpServletRequest request,
            @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search available books by isbn");
        }

        return new ResponseEntity<>(
                new ApiResponse(bookRepository.findByAvailableAndIsbnContainingIgnoreCase(true, book.getIsbn())),
                HttpStatus.OK);
    }

    @GetMapping("/search/unavailable/isbn")
    public ResponseEntity<ApiResponse> searchUnavailableBooksByIsbn(HttpServletRequest request,
            @RequestBody BookRequest book) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to search unavailable books by isbn");
        }

        return new ResponseEntity<>(
                new ApiResponse(bookRepository.findByAvailableAndIsbnContainingIgnoreCase(false, book.getIsbn())),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteBookById(HttpServletRequest request, @PathVariable Long id) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "JWT token is empty");
        }
        if (!jwtPayload.getRole().equals(Role.ADMIN)) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "You are not authorized to delete books");
        }

        if (!bookRepository.existsById((id))) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Book not found");
        }

        bookRepository.deleteById((id));
        return new ResponseEntity<>(new ApiResponse("Book deleted successfully"), HttpStatus.OK);
    }

}
