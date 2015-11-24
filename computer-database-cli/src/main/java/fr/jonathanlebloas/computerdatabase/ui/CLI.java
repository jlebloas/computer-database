package fr.jonathanlebloas.computerdatabase.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ComputerNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

/**
 * Command Line Interface used to manage Computers (List, Create, Update ...)
 */
@Component
public final class CLI {

	private static final String CONSOLE_ARG_ID = "id";
	private static final String CONSOLE_ARG_NAME = "name";
	private static final String CONSOLE_ARG_INTRODUCED = "introduced";
	private static final String CONSOLE_ARG_DISCONTINUED = "discontinued";
	private static final String CONSOLE_ARG_MANUFACTURER = "manufacturer";
	private static final String CONSOLE_ARG_PAGE = "page";
	private static final String CONSOLE_ARG_SEARCH = "search";

	/**
	 * Usable commands enumerate
	 */
	private enum Command {
		LISTCOMPANIES("Display the list of companies", new Options()
				.addOption(new Option("p", CONSOLE_ARG_PAGE, true, "The optional page"))
				.addOption(new Option("s", CONSOLE_ARG_SEARCH, true, "The search of the page"))),
		LISTCOMPUTERS("Display the list of computers", new Options()
				.addOption(new Option("p", CONSOLE_ARG_PAGE, true, "The optional page"))
				.addOption(new Option("s", CONSOLE_ARG_SEARCH, true, "The search of the page"))),
		SHOW("Display the details of a computer", new Options()
				.addOption(new Option(CONSOLE_ARG_ID, CONSOLE_ARG_ID, true, "The id of the computer"))),
		CREATE("Create a computer", new Options()
				.addOption("n", CONSOLE_ARG_NAME, true, "The mendatory name of the computer")
				.addOption("i", CONSOLE_ARG_INTRODUCED, true, "The date it was introduced")
				.addOption("d", CONSOLE_ARG_DISCONTINUED, true, "The date it was discontinued")
				.addOption("m", CONSOLE_ARG_MANUFACTURER, true,	"The id of the manufacturer")),
		UPDATE( "Update a computer", new Options()
				.addOption(CONSOLE_ARG_ID, CONSOLE_ARG_ID, true, "The mandatory id of the upadted computer")
				.addOption("n", CONSOLE_ARG_NAME, true, "The new name of the computer")
				.addOption("i", CONSOLE_ARG_INTRODUCED, true, "The new introduced date")
				.addOption("d", CONSOLE_ARG_DISCONTINUED, true, "The new discontinued date")
				.addOption("m",	CONSOLE_ARG_MANUFACTURER, true, "The id of the new manufacturer")),
		DELETE("Delete a computer", new Options()
				.addOption(CONSOLE_ARG_ID, CONSOLE_ARG_ID, true, "The mandatory id of the computer to be deleted")),
		DELETECOMPANY("Delete a company", new Options()
				.addOption(CONSOLE_ARG_ID, CONSOLE_ARG_ID, true, "The mandatory id of the company to be deleted")),
		HELP("Display the commands", new Options());

		// TODO Look for optional args and naming args values

		private String description;

		private Options options;

		private Command(String description) {
			this.description = description;
		}

		private Command(String description, Options options) {
			this.description = description;
			this.options = options;
		}

		public String getDescription() {
			return this.description;
		}

		public Options getOptions() {
			return this.options;
		}
	}

	@Autowired
	private ComputerService computerService;

	@Autowired
	private CompanyService companyService;

	private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private static HelpFormatter formatter = new HelpFormatter();

	private static void help() {
		EnumSet<Command> enumSet = EnumSet.allOf(Command.class);
		System.out.println("Here is the command list :\n");

		for (Command command : enumSet) {
			formatter.printHelp(command.toString(), command.getDescription(), command.getOptions(), "\n", true);
		}
	}

	public static final void main(String[] args) {
		CLI cli;
		Command command = null;

		// Load context spring with a try-with-resource that will automatically
		// close the context at the end
		try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml")) {
			cli = context.getBean(CLI.class);

			// If there is at least a command
			if (args.length > 0 && !StringUtils.isEmpty(args[0])) {
				command = Command.valueOf(args[0].toUpperCase());

				// If there is additional options get and parse them
				String[] optionsArray = null;
				CommandLineParser optionsParser = null;
				CommandLine options = null;
				if (args.length > 1) {
					optionsArray = Arrays.copyOfRange(args, 1, args.length);

					optionsParser = new DefaultParser();
					options = optionsParser.parse(command.getOptions(), optionsArray);
				}

				// Process the command
				switch (command) {
				case LISTCOMPANIES:
					cli.listCompanies(options);
					break;

				case LISTCOMPUTERS:
					cli.listComputers(options);
					break;

				case CREATE:
					cli.create(options);
					break;

				case UPDATE:
					cli.update(options);
					break;

				case DELETE:
					cli.delete(options);
					break;

				case DELETECOMPANY:
					cli.deleteCompany(options);
					break;

				case SHOW:
					cli.show(options);
					break;

				case HELP:
					help();
					break;

				default:
					System.out.println("\t This command does exist !");
					help();
				}

			} else {
				System.out.println("\t You should at least enter a command !\n");
				help();
			}
		} catch (IllegalArgumentException e) {
			System.out.println("\t This command does exist !");
			help();
		} catch (ParseException exp) {
			System.out.println("\t Parsing options failed.  Reason: " + exp.getMessage());
			formatter.printHelp(command.toString(), command.getDescription(), command.getOptions(), "\n", true);
		}
	}

	private static void displayList(List<? extends Object> list) {
		for (Object object : list) {
			System.out.println("\t" + object.toString());
		}
	}

	private void listCompanies(CommandLine options) {
		try {
			// Get if a page is wanted
			if (options != null) {
				if (!options.hasOption(CONSOLE_ARG_PAGE)) {
					System.out.println("\t Unknown parameters");
					help();
					return;
				} else {
					int index = Integer.parseInt(options.getOptionValue(CONSOLE_ARG_PAGE));

					String search;
					if (options.hasOption(CONSOLE_ARG_SEARCH)) {
						search = options.getOptionValue(CONSOLE_ARG_SEARCH);
					} else {
						search = "";
					}

					PageRequest request = new PageRequest(index, 10);

					displayList(companyService.getPage(request, search).getContent());
				}
			} else {
				displayList(companyService.listCompanies());
			}
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\t The page does not exist.");
		} catch (ServiceException e) {
			System.out.println("\t" + e.getMessage());
		}
	}

	private void listComputers(CommandLine options) {
		try {
			// Get if a page is wanted
			if (options != null) {
				if (!options.hasOption(CONSOLE_ARG_PAGE)) {
					System.out.println("\t Unknown parameters");
					help();
					return;
				} else {
					int index = Integer.parseInt(options.getOptionValue(CONSOLE_ARG_PAGE));

					String search;
					if (options.hasOption(CONSOLE_ARG_SEARCH)) {
						search = options.getOptionValue(CONSOLE_ARG_SEARCH);
					} else {
						search = "";
					}

					PageRequest request = new PageRequest(index, 10);

					displayList(computerService.getPage(request, search).getContent());
				}
			} else {
				displayList(computerService.listComputers());
			}
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\t The page does not exist.");
		} catch (ServiceException e) {
			System.out.println("\t" + e.getMessage());
		}
	}

	private void show(CommandLine options) {
		try {
			// Get the mandatory id
			if (options == null || !options.hasOption(CONSOLE_ARG_ID)) {
				System.out.println("\t You must specify the id of the computer");
				return;
			}
			// Check id type
			Long id = Long.parseLong(options.getOptionValue(CONSOLE_ARG_ID));

			Computer c = computerService.find(id);
			String computerDetails = computerService.getComputerDetails(c);
			System.out.println("\t" + computerDetails);

		} catch (ComputerNotFoundException e) {
			System.out.println("\t " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (ServiceException e) {
			System.out.println("\t" + e.getMessage());
		}
	}

	private void create(CommandLine options) {
		try {
			// Get the mandatory name asked
			if (options == null || !options.hasOption(CONSOLE_ARG_NAME)) {
				throw new EmptyNameException();
			}
			String name = options.getOptionValue(CONSOLE_ARG_NAME);

			// Parse the given dates
			LocalDate introduced = null;
			LocalDate discontinued = null;
			if (options.hasOption(CONSOLE_ARG_INTRODUCED)) {
				introduced = LocalDate.parse(options.getOptionValue(CONSOLE_ARG_INTRODUCED), df);
			}
			if (options.hasOption(CONSOLE_ARG_DISCONTINUED)) {
				discontinued = LocalDate.parse(options.getOptionValue(CONSOLE_ARG_DISCONTINUED), df);
			}

			// Get the optional manufacturer
			Company company = null;
			if (options.hasOption(CONSOLE_ARG_MANUFACTURER)) {
				Long manufacturerId = Long.parseLong(options.getOptionValue(CONSOLE_ARG_MANUFACTURER));

				company = companyService.find(manufacturerId);
			}

			// Create the computer
			Computer newComputer = Computer.builder().name(name).introduced(introduced).discontinued(discontinued)
					.company(company).build();
			computerService.create(newComputer);

			System.out.println("\t Your computer as been successfully created ! : " + newComputer.toString());

		} catch (ServiceException e) {
			System.out.println("\t" + e.getMessage());
		} catch (DateTimeParseException e) {
			System.out.println("\t Your date is not well formated. Format it like 2011-12-03");
		}
	}

	private void update(CommandLine options) {
		try {
			// Get the mandatory id
			if (options == null || !options.hasOption(CONSOLE_ARG_ID)) {
				System.out.println("\t You must specify the id of the computer");
				return;
			}
			// Check id type
			Long id = Long.parseLong(options.getOptionValue(CONSOLE_ARG_ID));

			Computer computer = computerService.find(id);

			if (options.hasOption(CONSOLE_ARG_NAME)) {
				computer.setName(options.getOptionValue(CONSOLE_ARG_NAME));
			}

			// Parse the given dates
			if (options.hasOption(CONSOLE_ARG_INTRODUCED)) {
				LocalDate introduced = LocalDate.parse(options.getOptionValue(CONSOLE_ARG_INTRODUCED), df);
				computer.setIntroduced(introduced);
			}
			if (options.hasOption(CONSOLE_ARG_DISCONTINUED)) {
				LocalDate discontinued = LocalDate.parse(options.getOptionValue(CONSOLE_ARG_DISCONTINUED), df);
				computer.setDiscontinued(discontinued);
			}

			// Get the optional manufacturer
			if (options.hasOption(CONSOLE_ARG_MANUFACTURER)) {
				Long manufacturerId = Long.parseLong(options.getOptionValue(CONSOLE_ARG_MANUFACTURER));

				Company manufacturer = companyService.find(manufacturerId);
				computer.setCompany(manufacturer);
			}

			// Update the computer with the service
			computerService.update(computer);
			System.out.println("\t Your computer as been successfully updated ! : " + computer.toString());

		} catch (DateTimeParseException e) {
			System.out.println("\t Your date is not well formated.");
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format. Format it like 2011-12-03");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	private void delete(CommandLine options) {
		try {
			// Get the mandatory id
			if (options == null || !options.hasOption(CONSOLE_ARG_ID)) {
				System.out.println("\t You must specify the id of the computer");
				return;
			}
			// Check id type
			Long id = Long.parseLong(options.getOptionValue(CONSOLE_ARG_ID));

			Computer computer = computerService.find(id);

			// Update the computer with the service
			computerService.delete(computer);
			System.out.println("\t Your computer as been successfully deleted ! : " + computer.toString());

		} catch (ComputerNotFoundException e) {
			System.out.println("\t The computer requested with this id does not exist.");
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (ServiceException e) {
			System.out.println("\t" + e.getMessage());
		}
	}

	private void deleteCompany(CommandLine options) {
		try {
			// Get the mandatory id
			if (options == null || !options.hasOption(CONSOLE_ARG_ID)) {
				System.out.println("\t You must specify the id of the company");
				return;
			}
			// Check id type
			Long id = Long.parseLong(options.getOptionValue(CONSOLE_ARG_ID));

			Company company = companyService.find(id);

			// Update the computer with the service
			companyService.delete(company);
			System.out.println("\t Your company as been successfully deleted ! : " + company.toString());

		} catch (ComputerNotFoundException e) {
			System.out.println("\t The computer requested with this id does not exist.");
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (ServiceException e) {
			System.out.println("\t" + e.getMessage());
		}
	}

}