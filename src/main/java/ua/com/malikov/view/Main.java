package ua.com.malikov.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;
import ua.com.malikov.Profiles;
import ua.com.malikov.model.*;
import ua.com.malikov.service.*;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private boolean reInit;

    private CompanyService companyController;
    private CustomerService customerController;
    private DeveloperService developerController;
    private ProjectService projectController;
    private SkillService skillController;

    private DataSource dataSource;

    public static void main(String[] args) throws Exception {
        try (GenericXmlApplicationContext appCtx = new GenericXmlApplicationContext()) {
            appCtx.getEnvironment().setActiveProfiles(Profiles.ACTIVE_DB_IMPLEMENTATION);
            appCtx.load("spring/spring-app.xml", "spring/spring-db.xml");
            appCtx.refresh();
            Main main = appCtx.getBean(Main.class);
            main.start();
        }
    }

    private void start() throws Exception {

        String choice = "";

        while (!choice.equalsIgnoreCase("0")) {
            System.out.print(
                    "\n---------Main menu---------\n" +
                            "1. CRUD operations with companies.\n" +
                            "2. CRUD operations with customers.\n" +
                            "3. CRUD operations with developers.\n" +
                            "4. CRUD operations with projects.\n" +
                            "5. CRUD operations with skills.\n" +
                            "6. Add skills to developer.\n" +
                            "7. Add developer to project.\n" +
                            "0. Quit\n\n" +
                            "Please make your choice: ");
            choice = br.readLine();

            switch (choice) {
                case "1": // table companies
                case "2": // table customers
                case "3": // table developers
                case "4": //table projects
                case "5": { //table skills
                    getIntoSubMenu(choice);
                    break;
                }
                case "6": //add skills to developer
                {
                    System.out.print("Start of adding skills to existing developer..\nPlease input developer information. ");
                    Developer developer = developerController.get(getIdFromConsole());
                    if (developer != null) {
                        System.out.println("Developer for adding skills:\n" + developer);
                        System.out.println("Now you need to input all skills of developer.");
                        Set<Skill> updatedSkills = getSkillsFromConsole();
                        if (updatedSkills != null) {
                            updatedSkills.addAll(developer.getSkills());
                        } else {
                            updatedSkills = developer.getSkills();
                        }
                        developerController.addSkillsToDeveloper(updatedSkills, developer);
                        System.out.println("Successful operation. ");
                    } else {
                        System.out.println("Sorry, there is no developer with such id.");
                    }
                    System.out.println("End of adding skill to existing developer.");
                    break;
                }
                case "7": //add developer to project
                {
                    System.out.println("Start of adding developer to existing project.." +
                            "\nPlease input id of developer you want to add to project.");
                    Integer developerId = getIdFromConsole();
                    System.out.println("Please input id of project where you want to add the developer.");
                    Integer projectId = getIdFromConsole();
                    Developer developer = developerController.get(developerId);
                    Project project = projectController.get(projectId);
                    if (project != null && developer != null) {
                        projectController.addDeveloperToProject(developer, project);
                        System.out.print("Successful operation. ");
                    } else {
                        System.out.print("Sorry, bad id. ");
                    }
                    System.out.println("End of adding developer to existing project.");
                    break;
                }
                case "8": //add developer to company
                {
                    System.out.println("Start of adding existing developer to existing company.." +
                            "\nPlease input id of developer you want to add to project.");
                    Integer developerId = getIdFromConsole();
                    System.out.println("Please input id of company where you want to add the developer.");
                    Integer companyId = getIdFromConsole();
                    Developer developer = developerController.get(developerId);
                    Company company = companyController.get(companyId);
                    if (company != null && developer != null) {
                        companyController.addDeveloperToCompany(developer, company);
                        System.out.print("Successful operation. ");
                    } else {
                        System.out.print("Sorry, bad id. ");
                    }
                    System.out.println("End of adding developer to existing project.");
                    break;
                }
                case "0": //exit
                {
                    br.close();
                    System.out.println("Good Bye!");
                    System.exit(0);
                }
                default:
                    System.out.print("\nPlease, choose 0-7 : ");
            }
        }
    }

    private void getIntoSubMenu(String choice) throws Exception {
        String tableName = table(choice);
        String subMenuChoice = "";
        while (!"0".equalsIgnoreCase(subMenuChoice)) {
            System.out.print(
                    "\n---------Menu for " + tableName + "---------\n" +
                            "1. Create (add 1 new entity).\n" +
                            "2. Read by id.\n" +
                            "3. Read all.\n" +
                            "4. Update by id (update 1 entity).\n" +
                            "5. Delete by id.\n" +
                            "6. Delete all.\n" +
                            "0. Back to main menu.\n\n" +
                            "Please make your choice: ");
            subMenuChoice = br.readLine();

            switch (subMenuChoice) {
                case "1": // adding new entity
                {
                    System.out.print("Start of creating new row in table " + tableName +
                            "..\nPlease enter name of new entity: ");
                    String name = br.readLine();
                    switch (choice) {
                        case "1": // adding new company
                        {
                            companyController.add(new Company(name));
                            break;
                        }
                        case "2": // adding new customer
                        {
                            customerController.add(new Customer(name));
                            break;
                        }
                        case "3": // adding new developer
                        {
                            System.out.print("Please enter last name of new developer: ");
                            String lastName = br.readLine();
                            System.out.print("Please set company for new developer. ");
                            companyController.getAll().forEach(System.out::println);
                            Company company = companyController.get(getIdFromConsole());
                            if (company != null) {
                                developerController.add(
                                        new Developer(name, lastName, company, getSkillsFromConsole()));
                                break;
                            } else System.out.println("Sorry there is no such row in database: "
                                    + "companyId - " + company);
                            continue;

                        }
                        case "4": //  adding new project
                        {
                            System.out.print("Please set company of new project. ");
                            Company company = companyController.get(getIdFromConsole());
                            System.out.print("Please set customer of new project. ");
                            Customer customer = customerController.get(getIdFromConsole());
                            Float cost = getCostOfProject();
                            if (company != null && customer != null) {
                                projectController.add(new Project(name, company, customer, getDevelopersFromConsole(), cost));
                                break;
                            } else System.out.println("Sorry there is no such rows in database: "
                                    + "companyId - " + company + ", "
                                    + "customerId - " + customer);
                            continue;
                        }
                        case "5": //  adding new skill
                        {
                            skillController.add(new Skill(name));
                            break;
                        }
                    }
                    System.out.println("New entity was successfully added in table " + tableName + ".");
                    System.out.println("End of creating new row in table " + tableName + '.');
                    continue;
                }
                case "2": // reading by id
                {
                    System.out.println("Start of reading by id from table " + tableName + "..");
                    Integer id = getIdFromConsole();

                    switch (choice) {
                        case "1": // reading company by id
                        {
                            printEntityById(id, companyController);
                            break;
                        }
                        case "2": // reading customer by id
                        {
                            printEntityById(id, customerController);
                            break;
                        }
                        case "3": // reading developer by id
                        {
                            printEntityById(id, developerController);
                            break;
                        }
                        case "4": //  reading project by id
                        {
                            printEntityById(id, projectController);
                            break;
                        }
                        case "5": //  reading skill by id
                        {
                            printEntityById(id, skillController);
                            break;
                        }
                    }
                    System.out.println("End of reading by id from table " + tableName + ".");
                    continue;
                }
                case "3": //reading all
                {
                    System.out.println("Start of reading all entities from table " + tableName + "..");
                    switch (choice) {
                        case "1": // reading all companies
                        {
                            readAllRowsFromTable(companyController);
                            break;
                        }
                        case "2": // reading all customers
                        {
                            readAllRowsFromTable(customerController);
                            break;
                        }
                        case "3": // reading all developers
                        {
                            readAllRowsFromTable(developerController);
                            break;
                        }
                        case "4": //  reading all projects
                        {
                            readAllRowsFromTable(projectController);
                            break;
                        }
                        case "5": //  reading all skills
                        {
                            readAllRowsFromTable(skillController);
                            break;
                        }
                    }
                    System.out.println("End of reading all entities from table " + tableName + ".");
                    continue;
                }
                case "4": // updating by id
                {
                    System.out.println("Start of updating by id in table " + tableName + "..");
                    Integer id = getIdFromConsole();

                    System.out.print("Starting of data input for entity update." +
                            " ! Notion: Press twice \'Enter\' if you don\'t want to change this param (to keep this param as it is now).");
                    switch (choice) {
                        case "1": // updating company
                        {
                            updateAndPrintEntityRetrievedByIdInputedFromConsole(id, companyController);
                            break;
                        }
                        case "2": // updating customer
                        {
                            updateAndPrintEntityRetrievedByIdInputedFromConsole(id, customerController);
                            break;
                        }
                        case "3": // updating developer
                        {
                            Developer developer = developerController.get(id);
                            if (developer != null) {
                                System.out.println("Developer for update:\n" + developer);
                                System.out.print("Enter new name of developer or press 'Enter' " +
                                        "if you dont want to change name: ");
                                String newName = br.readLine();
                                if ("".equals(newName)) {
                                    newName = developer.getName();
                                }
                                System.out.print("Please enter new last name of updated developer or press \'Enter\' " +
                                        "if you dont want to change last name: ");
                                String newLastName = br.readLine();
                                if ("".equals(newLastName)) {
                                    newLastName = developer.getLastName();
                                }
                                System.out.print("Please enter new company id of updated developer: ");
                                Company company = companyController.get(getIdFromConsole());
                                System.out.println("Now you need to input all skills of updated developer.");
                                if (company != null) {
                                    developerController.update(new Developer(id, newName, newLastName, company, getSkillsFromConsole()));
                                } else {
                                    System.out.println("Company for developer to update wasn't found in database.\n" +
                                            "Developer was not updated.");
                                }
                            } else {
                                System.out.println("Sorry. Bad id.");
                            }
                            break;
                        }
                        case "4": //  updating project
                        {
                            Project project = projectController.get(id);
                            if (project != null) {
                                System.out.println("Project for update:\n" + project);
                                System.out.print("Enter new name of project:");
                                String newName = br.readLine();

                                System.out.print("Please enter new company id of updated project: ");
                                Company company = companyController.get(getIdFromConsole());
                                System.out.print("Please enter new customer id of updated project: ");
                                Customer customer = customerController.get(getIdFromConsole());

                                System.out.println("Please update developers to new project. Type id's of developer. Press \'Enter\' after each id of developer. Press twice \'Enter\' to end input.");

                                Set<Developer> newDevelopers = getDevelopersFromConsole();
                                Float newProjectCost = getCostOfProject();
                                if (company != null && customer != null) {
                                    projectController.update(
                                            new Project(id, newName, company, customer, newDevelopers, newProjectCost));
                                } else {
                                    System.out.println("Some inputs are null: " + "customerID - "
                                            + customer + ", companyID - "
                                            + company + ". Nothing were updated");
                                }
                            } else {
                                System.out.println("Sorry. Bad id.");
                            }
                            break;
                        }
                        case "5": //  updating skill
                        {
                            updateAndPrintEntityRetrievedByIdInputedFromConsole(id, skillController);
                            break;
                        }
                    }
                    System.out.println("End of updating by id in table " + tableName + ".");

                    continue;
                }
                case "5": // deleting by id
                {
                    System.out.println("Start of deleting by id in table " + tableName + "..");
                    Integer id = getIdFromConsole();
                    String hardChoice = getAnswerForDeletingConfirmation();
                    if (("5".equals(subMenuChoice) && !"".equals(hardChoice) && hardChoice.equalsIgnoreCase("y"))) {
                        switch (choice) {
                            case "1": // deleting company by id
                            {
                                deleteEntityById(tableName, id, companyController);
                                break;
                            }
                            case "2": // deleting customer by id
                            {
                                deleteEntityById(tableName, id, customerController);
                                break;
                            }
                            case "3": // deleting developer by id
                            {
                                deleteEntityById(tableName, id, developerController);
                                break;
                            }
                            case "4": //  deleting project by id
                            {
                                deleteEntityById(tableName, id, projectController);
                                break;
                            }
                            case "5": //  deleting skill by id
                            {
                                deleteEntityById(tableName, id, skillController);
                                break;
                            }
                        }
                    } else {
                        System.out.println("Deleting was canceled.");
                    }
                    System.out.println("End of deleting by id in table " + tableName + ".");
                    continue;
                }
                case "6": // deleting all
                {
                    System.out.println("Start of deleting all rows in table " + tableName + "..");
                    String hardChoice = getAnswerForDeletingConfirmation();
                    if (("6".equals(subMenuChoice) && !"".equals(hardChoice) && hardChoice.equalsIgnoreCase("y"))) {
                        switch (choice) {
                            case "1": // deleting all companies
                            {
                                companyController.deleteAll();
                                break;
                            }
                            case "2": // deleting all customers
                            {
                                customerController.deleteAll();
                                break;
                            }
                            case "3": // deleting all developers
                            {
                                developerController.deleteAll();
                                break;
                            }
                            case "4": //  deleting all projects
                            {
                                projectController.deleteAll();
                                break;
                            }
                            case "5": //  deleting all skills
                            {
                                skillController.deleteAll();
                                break;
                            }
                        }
                        System.out.println("All entities from table " + tableName + " were successfully deleted:\n");
                    } else {
                        System.out.println("Deleting was canceled.");
                    }
                    System.out.println("End of deleting all rows in table " + tableName + ".");
                    continue;
                }

                case "0": //
                {
                    continue;
                }
                default:
                    System.out.print("\nPlease, choose 0-6 : ");
            }
        }
    }

    private Float getCostOfProject() throws IOException {
        Float cost;
        while (true) {
            try {
                System.out.print("Please set cost of project: ");
                cost = Float.valueOf(br.readLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Wrong input. Please try again to input float number.");
            }
        }
        return cost;
    }

    //  that is temporary method, we can do that work in one query to database
    private <T> void deleteEntityById(String tableName, Integer id, AbstractService<T> controller) throws SQLException {
        T t = controller.get(id);
        if (t != null) {
            controller.delete(id);
            System.out.println("Entity from table " + tableName + " with id=" + id + " was successfully deleted.\n");
        } else {
            System.out.println("Sorry. Bad id.");
        }
    }

    private <T extends NamedEntity> void updateAndPrintEntityRetrievedByIdInputedFromConsole(Integer id, AbstractService<T> controller) throws IOException, SQLException {
        T t = controller.get(id);
        if (t != null) {
            System.out.println("Entity for update:\n" + t);
            System.out.print("Enter new name: ");
            String newName = br.readLine();
            t.setName(newName);
            controller.update(t);
        } else {
            System.out.println("Sorry. Bad id. Couldn't retrieve entity with that id from database.");
        }
    }

    private String getAnswerForDeletingConfirmation() throws IOException {
        System.out.println("Press \'y\\Y\' to confirm or \'n\\N\' to cancel deleting.");
        return br.readLine();
    }

    private <T> void readAllRowsFromTable(AbstractService<T> controller) throws SQLException {
        List<T> entities = controller.getAll();
        if (entities.size() > 0) {
            entities.forEach(System.out::println);
        } else {
            System.out.println("Sorry. There is no entity in table.");
        }
    }

    private <T> void printEntityById(Integer id, AbstractService<T> controller) throws SQLException {
        T entity = controller.get(id);
        System.out.println(entity != null ? "Successful reading of entity by id: " + entity : "Sorry, bad id.");
    }

    private Set<Developer> getDevelopersFromConsole() throws IOException, SQLException {
        System.out.println("Please add developers to new project. Type id's of developer. Press \'Enter\' after each id of developer. Press \'Enter\' twice to end input.");
        Set<Developer> developers = new HashSet<>();
        Integer developerIdFromConsole = getIdFromConsoleOrPressEnterToStopInput();
        while (developerIdFromConsole != -1) {
            Developer developer = developerController.get(developerIdFromConsole);
            if (developer != null) {
                developers.add(developer);
            } else {
                System.out.println("Sorry, bad id of developer. Couldn't retrieve from database.");
            }
            developerIdFromConsole = getIdFromConsoleOrPressEnterToStopInput();
        }
        return developers;
    }

    //doesn't allow to input not integer or integer < 1
    private Integer getIdFromConsole() throws IOException {
        while (true) {
            try {
                System.out.print("Enter id: ");
                String input = br.readLine();
                Integer result = Integer.valueOf(input);
                if (result > 0) {
                    return result;
                }
            } catch (NumberFormatException e) {
                LOG.error("Number NumberFormatException occurred while parsing input.");
            }
            System.out.println("Incorrect input. Please input integer which is > 0.");
        }
    }

    //return -1 Integer to terminate input id double Enter pressed
    //doesn't allow to input not integer or integer < 1
    private Integer getIdFromConsoleOrPressEnterToStopInput() throws IOException {
        while (true) {
            try {
                System.out.print("Enter id: ");
                String input = br.readLine();
                if ("".equals(input)) {
                    return -1;
                }
                Integer result = Integer.valueOf(input);
                if (result > 0) {
                    return result;
                }
            } catch (NumberFormatException e) {
                LOG.error("Number NumberFormatException occurred while parsing input.");
            }
            System.out.println("Incorrect input. Please enter input integer > 0 " +
                    "or press \'Enter\' twice to terminate input of id's.");
        }
    }

    private Set<Skill> getSkillsFromConsole() throws IOException {
        System.out.println("Type name of skill of developer, press \'Enter\' after each skill name. To stop input press \'Enter\' twice.");
        Set<Skill> skills = new HashSet<>();
        skillController.getAll().forEach(System.out::println);
        String skillName = br.readLine();
        while (!"".equals(skillName)) {
            Skill skill = skillController.get(skillName);
            if (skill != null) {
                skills.add(skill);
            } else {
                skills.add(skillController.add(new Skill(skillName)));
            }
            skillName = br.readLine();
        }
        return skills;
    }

    private String table(String choice) {
        switch (choice) {
            case "1":
                return "companies";
            case "2":
                return "customers";
            case "3":
                return "developers";
            case "4":
                return "projects";
            case "5":
                return "skills";
        }
        return null;
    }

    public void setCompanyController(CompanyService companyController) {
        this.companyController = companyController;
    }

    public void setCustomerController(CustomerService customerController) {
        this.customerController = customerController;
    }

    public void setDeveloperController(DeveloperService developerController) {
        this.developerController = developerController;
    }

    public void setProjectController(ProjectService projectController) {
        this.projectController = projectController;
    }

    public void setSkillController(SkillService skillController) {
        this.skillController = skillController;
    }

    public void setReInit(boolean reInit) {
        this.reInit = reInit;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
