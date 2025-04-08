# spring-boot-intro
# spring-boot-intro
# spring-boot-intro
add BookDto.class as a response DTO, and CreateBookRequestDto.class as a request DTO
Add Mapstruct and mappers
Return and accept DTOs on the service layer

Add BookController with methods:
public List getAll()
public BookDto getBookById(Long id)
public BookDto createBook(CreateBookRequestDto bookDto)

Add missing methods in the BookService and BookRepository

Create and use EntityNotFoundException class

Remove CommandLineRunner bean

