

import java.util.Scanner;

import controller.CLIController;
import controller.Context;
import model.DatabaseInitializator;
import model.dao.BookDAO;
import model.dao.CabinetDAO;
import model.dao.RoomDAO;
import model.dao.ShelfDAO;
import model.pojo.Cabinet;
import model.pojo.Shelf;
import service.BookService;
import service.CabinetService;
import service.ShelfService;
import view.BookView;
import view.CabinetView;
import view.ShelfView;

public class Main {
    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        Context context = new Context();
        DatabaseInitializator.init();

        // DAO
        BookDAO bookDAO = new BookDAO();
        ShelfDAO shelfDAO = new ShelfDAO();
        CabinetDAO cabinetDAO = new CabinetDAO();
        RoomDAO roomDAO = new RoomDAO();

        // Services
        BookService bookService = new BookService(bookDAO, shelfDAO);
        ShelfService shelfService = new ShelfService(shelfDAO);
        CabinetService cabinetService = new CabinetService(cabinetDAO);

        // Controller
        CLIController controller = new CLIController(bookService, cabinetService, shelfService);

        try {
            while (true) {
                if (context.isInRoom()) {
                    var cabinets = cabinetService.getAllCabinets();
                    CabinetView.render(cabinets);
                } else if (context.isInCabinet()) {
                    var shelves = shelfService.getAllShelvesByCabinet(context.getCurrentCabinetId());
                    ShelfView.render(shelves);
                } else if (context.isInShelf()) {
                    var books = bookService.getAllBooksByShelf(context.getCurrentShelfId());
                    BookView.render(books);
                }

                System.out.println("\nEnter command (--help for help, exit for exit):");
                System.out.print("> ");
                String line = scanner.nextLine().trim();

                if (line.equalsIgnoreCase("exit")) break;
                if (line.equalsIgnoreCase("back")) {
                    context.back();
                    continue;
                }

                // "use cabinet someName"
                if (line.startsWith("use cabinet")) {
                    String name = line.split(" ")[2].trim();
                    Cabinet cb = cabinetService.findCabinetByName(name);
                    if (cb == null) {
                        System.out.println("Cabinet " + name + " not found");
                        continue;
                    }
                    context.setCabinet(cb.getId());
                    continue;
                }
                // "use shelf someName"
                if (line.startsWith("use shelf")) {
                    String name = line.split(" ")[2].trim();
                    Shelf sh = shelfService.findShelfByName(name);
                    if (sh == null) {
                        System.out.println("Shelf " + name + " not found");
                        continue;
                    }
                    context.setShelf(sh.getId());
                    continue;
                }

                String[] cmdArgs = line.split(" ");
                controller.run(cmdArgs, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
