package controller;

import exceptions.EntityNotFoundException;
import model.dto.BookLocation;
import org.apache.commons.cli.*;
import service.BookService;
import service.CabinetService;
import service.ShelfService;

public class CLIController {
    private final BookService bookService;
    private final CabinetService cabinetService;
    private final ShelfService shelfService;
    private final int mainRoomId = 0;

    private final Options options;

    public CLIController(BookService bookService, CabinetService cabinetService, ShelfService shelfService) {
        this.bookService = bookService;
        this.cabinetService = cabinetService;
        this.shelfService = shelfService;

        this.options = new Options();

        options.addOption("c", "create", true, "Create entity (book|cabinet|shelf)");
        options.addOption("f", "find", true, "Find entity (book|cabinet|shelf)");
        options.addOption("u", "update", true, "Update entity (book|cabinet|shelf)");
        options.addOption("d", "delete", true, "Delete entity (book|cabinet|shelf)");
        options.addOption("h", "help", false, "Show help");

        // book fields
        options.addOption(Option.builder().longOpt("title").hasArg().desc("Book title").build());
        options.addOption(Option.builder().longOpt("author").hasArg().desc("Book author").build());
        options.addOption(Option.builder().longOpt("newTitle").hasArg().desc("New title of book").build());

        // shelves fields
        options.addOption(Option.builder().longOpt("cabinetId").hasArg().desc("Cabinet ID for shelf").build());

        // cabinet fields
        options.addOption(Option.builder().longOpt("roomId").hasArg().desc("Room ID for cabinet").build());

        // id
        options.addOption(Option.builder().longOpt("id").hasArg().desc("Entity ID").build());

        options.addOption(Option.builder().longOpt("name").hasArg().desc("Name of entity").build());
        options.addOption(Option.builder().longOpt("newName").hasArg().desc("New name of entity").build());
    }

    public void run(String[] args, Context context) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("library", options);
                System.out.println("  use cabinet <name>   - goto inside cabinet");
                System.out.println("  use shelf <name>     - goto inside shelf");
                System.out.println("  back - turn back");
                System.out.println("  exit - exit program");
                return;
            }
            // Book commands
            if (cmd.hasOption("c") && cmd.getOptionValue("c").equalsIgnoreCase("book")) {
                if (context.isInShelf()) {
                    String title = cmd.getOptionValue("title");
                    String author = cmd.getOptionValue("author");
                    bookService.addBookToShelf(context.getCurrentShelfId(), author, title);
                } else {
                    System.out.println("You are not in a shelf");
                }
                return;
            }

            if (cmd.hasOption("f") && cmd.getOptionValue("f").equalsIgnoreCase("book")) {
                String title = cmd.getOptionValue("title");
                String author = cmd.getOptionValue("author");

                if (author != null) {
                    BookLocation book = bookService.findBookLocation(title, author);
                    if (book == null) {
                        System.out.println("Book not found");
                    }
                    else {
                        System.out.println("Found book: " + book.getBookTitle() + " (" + book.getBookAuthor() + ") " +
                                "\nLocation: " + book.getCabinetName() + "/" + book.getShelfName());
                    }
                } else {
                    bookService.findBooksByTitle(title)
                            .forEach(b -> System.out.println(" --- " + b.getBookTitle() + " ("
                                    + b.getBookAuthor() + ")" +
                                    "\nLocation: " + b.getCabinetName() + "/" + b.getShelfName()));
                }
                return;
            }

            if (cmd.hasOption("u") && cmd.getOptionValue("u").equalsIgnoreCase("book")) {
                String oldTitle = cmd.getOptionValue("title");
                String author = cmd.getOptionValue("author");
                String newTitle = cmd.getOptionValue("newTitle");
                bookService.updateBookTitle(oldTitle, author, newTitle);
                return;
            }

            if (cmd.hasOption("d") && cmd.getOptionValue("d").equalsIgnoreCase("book")) {
                try {
                    bookService.deleteBook(cmd.getOptionValue("title"), cmd.getOptionValue("author"));
                }
                catch (EntityNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }

            // Cabinet commands
            if (cmd.hasOption("c") && cmd.getOptionValue("c").equalsIgnoreCase("cabinet")) {
                String name = cmd.getOptionValue("name");
                cabinetService.addCabinet(name, mainRoomId);
                return;
            }

            if (cmd.hasOption("d") && cmd.getOptionValue("d").equalsIgnoreCase("cabinet")) {
                String name = cmd.getOptionValue("name");
                cabinetService.deleteCabinet(name);
                System.out.println("Cabinet " + name + " deleted");
                return;
            }

            if (cmd.hasOption("u") && cmd.getOptionValue("u").equalsIgnoreCase("cabinet")) {
                String name = cmd.getOptionValue("name");
                String newName = cmd.getOptionValue("newName");
                cabinetService.updateCabinet(name, newName);
                System.out.println("Cabinet " + name + " updated");
                return;
            }

            // shelf commands
            if (cmd.hasOption("c") && cmd.getOptionValue("c").equalsIgnoreCase("shelf")) {
                if (context.isInCabinet()) {
                    shelfService.addShelf(cmd.getOptionValue("name"), context.getCurrentCabinetId());
                } else {
                    System.out.println("You are not in a cabinet");
                }
                return;
            }

            if (cmd.hasOption("d") && cmd.getOptionValue("d").equalsIgnoreCase("shelf")) {
                String name = cmd.getOptionValue("name");
                shelfService.deleteShelf(name);
                System.out.println("Shelf " + name + " deleted");
                return;
            }

            if (cmd.hasOption("u") && cmd.getOptionValue("u").equalsIgnoreCase("shelf")) {
                String name = cmd.getOptionValue("name");
                String newName = cmd.getOptionValue("newName");
                shelfService.updateShelf(name, newName);
                System.out.println("Shelf " + name + " updated");
                return;
            }
            System.out.println("Not known command. Please, use -h for help ");

        } catch (org.apache.commons.cli.ParseException e) {
            throw new RuntimeException(e);
        }
    }
}