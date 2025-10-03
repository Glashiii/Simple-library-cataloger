package controller;

import exceptions.EntityAlreadyExists;
import exceptions.EntityNotFoundException;
import exceptions.QuantityLimitException;
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
//        options.addOption(Option.builder().longOpt("cabinetId").hasArg().desc("Cabinet ID for shelf").build());

        // cabinet fields
//        options.addOption(Option.builder().longOpt("roomId").hasArg().desc("Room ID for cabinet").build());


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
                System.out.println("  Book can have args: title, newTitle, author");
                System.out.println("  Other entities can have args: name, newName");
                System.out.println("  Specify which entity you want to interact");
                return;
            }
            // Book commands

            // create book
            if (cmd.hasOption("c") && cmd.getOptionValue("c").equalsIgnoreCase("book")) {
                try {
                    if (context.isInShelf()) {
                        String title = getRequiredOptionValue(cmd, "title");
                        String author = cmd.getOptionValue("author");
                        bookService.addBookToShelf(context.getCurrentShelfId(), author, title);
                        System.out.println("Book added to shelf");
                    } else {
                        System.out.println("You are not in a shelf");
                    }
                } catch (EntityAlreadyExists e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
            // find book location
            if (cmd.hasOption("f") && cmd.getOptionValue("f").equalsIgnoreCase("book")) {
                String title = getRequiredOptionValue(cmd, "title");
                String author = cmd.getOptionValue("author");
                if (author != null && !author.isBlank()) {
                    try {
                        BookLocation book = bookService.findBookLocation(title, author);
                        System.out.println("Found book: " + book.getBookTitle() + " (" + book.getBookAuthor() + ") " +
                                "\nLocation: " + book.getCabinetName() + "/" + book.getShelfName());
                    } catch (EntityNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    try {
                        bookService.findBooksByTitle(title)
                                .forEach(b -> System.out.println(" --- " + b.getBookTitle() + " ("
                                        + b.getBookAuthor() + ")" +
                                        "\nLocation: " + b.getCabinetName() + "/" + b.getShelfName()));
                    } catch (EntityNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
                return;
            }
            // update book params
            if (cmd.hasOption("u") && cmd.getOptionValue("u").equalsIgnoreCase("book")) {
                String oldTitle = getRequiredOptionValue(cmd, "oldTitle");
                String author = getRequiredOptionValue(cmd, "author");
                String newTitle = getRequiredOptionValue(cmd, "newTitle");
                try {
                    bookService.updateBookTitle(oldTitle, author, newTitle);
                } catch (EntityNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
            // delete book
            if (cmd.hasOption("d") && cmd.getOptionValue("d").equalsIgnoreCase("book")) {
                String title = getRequiredOptionValue(cmd, "title");
                String author = getRequiredOptionValue(cmd, "author");
                try {
                    bookService.deleteBook(title, author);
                    System.out.println("Book with title " + title + " and author " + author + " deleted");
                } catch (EntityNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }

            // Cabinet commands

            // create cabinet
            if (cmd.hasOption("c") && cmd.getOptionValue("c").equalsIgnoreCase("cabinet")) {
                String name = getRequiredOptionValue(cmd, "name");
                try {
                    cabinetService.addCabinet(name, mainRoomId);
                    System.out.println("Added Cabinet " + name);
                } catch (EntityAlreadyExists | QuantityLimitException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }

            if (cmd.hasOption("d") && cmd.getOptionValue("d").equalsIgnoreCase("cabinet")) {
                String name = getRequiredOptionValue(cmd, "name");
                try {
                    cabinetService.deleteCabinet(name);
                    System.out.println("Cabinet " + name + " deleted");
                } catch (EntityNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }

            if (cmd.hasOption("u") && cmd.getOptionValue("u").equalsIgnoreCase("cabinet")) {
                String name = getRequiredOptionValue(cmd, "name");
                String newName = getRequiredOptionValue(cmd, "newName");
                try {
                    cabinetService.updateCabinet(name, newName);
                    System.out.println("Cabinet " + name + " updated");
                } catch (EntityNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }

            // shelf commands
            if (cmd.hasOption("c") && cmd.getOptionValue("c").equalsIgnoreCase("shelf")) {
                String name = getRequiredOptionValue(cmd, "name");
                if (context.isInCabinet()) {
                    try {
                        shelfService.addShelf(name, context.getCurrentCabinetId());
                        System.out.println("Added Shelf " + name);
                    } catch (EntityAlreadyExists e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    System.out.println("You are not in a cabinet");
                }
                return;
            }

            if (cmd.hasOption("d") && cmd.getOptionValue("d").equalsIgnoreCase("shelf")) {
                String name = getRequiredOptionValue(cmd, "name");
                try {
                    shelfService.deleteShelf(name);
                    System.out.println("Shelf " + name + " deleted");
                } catch (EntityNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }

            if (cmd.hasOption("u") && cmd.getOptionValue("u").equalsIgnoreCase("shelf")) {
                String name = getRequiredOptionValue(cmd, "name");
                String newName = getRequiredOptionValue(cmd, "newName");
                try {
                    shelfService.updateShelf(name, newName);
                    System.out.println("Shelf " + name + " updated");
                } catch (EntityNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
            System.out.println("Not known command. Please, use -h for help ");

        } catch (org.apache.commons.cli.ParseException e) {
            System.out.println("Please, enter right command. You can enter -h for help ");
            System.out.println(e.getMessage());
        }
    }
    private String getRequiredOptionValue(CommandLine cmd, String optionName) throws ParseException {
        if (!cmd.hasOption(optionName)) {
            throw new ParseException("Missing required option  --" + optionName);
        }
        String value = cmd.getOptionValue(optionName);
        if (value == null || value.isBlank()) {
            throw new ParseException("Value for option --" + optionName + " cannot be blank");
        }
        return value;
    }
}