package fr.jonathanlebloas.computerdatabase.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

/**
 * Command Line Interface used to manage Computers (List, Create, Update ...)
 */
public final class CLI {

	private static final String URI_COMPUTER = "http://localhost:8080/computer-database-rest/computer";
	private static final String URI_COMPANY = "http://localhost:8080/computer-database-rest/company";

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

	private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private static HelpFormatter formatter = new HelpFormatter();

	JerseyClient client;

	public CLI() {
		ClientConfig cc = new ClientConfig();
		// cc.register(new LoggingFilter()); // Enable REST client Logs
		client = JerseyClientBuilder.createClient(cc);
	}

	private static void help() {
		EnumSet<Command> enumSet = EnumSet.allOf(Command.class);
		System.out.println("Here is the command list :\n");

		for (Command command : enumSet) {
			formatter.printHelp(command.toString(), command.getDescription(), command.getOptions(), "\n", true);
		}
	}

	public static final void main(String[] args) {
		CLI cli = new CLI();
		Command command = null;

		try {
			// If there is at least a command
			if (args.length > 0 && !StringUtils.isEmpty(args[0])) {
				command = Command.valueOf(args[0].toUpperCase());

				// If there is additional options get and parse them
				String[] optionsArray;
				CommandLineParser optionsParser;
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

					List<CompanyDTO> companies = client.target(URI_COMPANY).path("/page").queryParam("page", index)
							.queryParam("search", search).queryParam("size", 10).request(MediaType.APPLICATION_JSON)
							.get(new GenericType<List<CompanyDTO>>() {
							});
					displayList(companies);
				}
			} else {
				List<CompanyDTO> companies = client.target(URI_COMPANY).path("/").request(MediaType.APPLICATION_JSON)
						.get(new GenericType<List<CompanyDTO>>() {
						});
				displayList(companies);
			}
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\t The page does not exist.");
		} catch (Exception e) {
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

					List<ComputerDTO> computers = client.target(URI_COMPUTER).path("/page").queryParam("page", index)
							.queryParam("search", search).queryParam("size", 10).request(MediaType.APPLICATION_JSON)
							.get(new GenericType<List<ComputerDTO>>() {
							});
					displayList(computers);
				}
			} else {
				List<ComputerDTO> computers = client.target(URI_COMPUTER).path("/").request(MediaType.APPLICATION_JSON)
						.get(new GenericType<List<ComputerDTO>>() {
						});
				displayList(computers);
			}
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("\t The page does not exist.");
		} catch (Exception e) {
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
			ComputerDTO computer = client.target(URI_COMPUTER).path("/" + id).request(MediaType.APPLICATION_JSON)
					.get(ComputerDTO.class);
			if (computer == null) {
				System.out.println("\t The computer does not exist.");
			} else {
				System.out.println("\t" + computer);
			}
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (Exception e) {
			System.out.println("\t" + e.getMessage());
		}
	}

	private void create(CommandLine options) {
		try {
			// Get the mandatory name asked
			if (options == null || !options.hasOption(CONSOLE_ARG_NAME)) {
				throw new IllegalArgumentException("The computer must have a name !");
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
			Long manufacturerId = 0L;
			if (options.hasOption(CONSOLE_ARG_MANUFACTURER)) {
				manufacturerId = Long.parseLong(options.getOptionValue(CONSOLE_ARG_MANUFACTURER));
			}

			// Create the computer
			ComputerDTO newComputer = new ComputerDTO();
			newComputer.setName(name);
			newComputer.setIntroduced(introduced == null ? "" : introduced.toString());
			newComputer.setDiscontinued(discontinued == null ? "" : discontinued.toString());
			newComputer.setCompanyId(manufacturerId.toString());

			Response response = client.target(URI_COMPUTER).path("/").request()
					.post(Entity.entity(newComputer, MediaType.APPLICATION_JSON));

			if (response.getStatus() == 200) {
				System.out.println("\t Your computer as been successfully created !");
			} else {
				System.out.println("\t an error occured : " + response.readEntity(String.class));
			}
		} catch (DateTimeParseException e) {
			System.out.println("\t Your date is not well formated. Format it like 2011-12-03");
		} catch (Exception e) {
			System.out.println("\t" + e.getMessage());
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

			ComputerDTO computer = client.target(URI_COMPUTER).path("/" + id).request(MediaType.APPLICATION_JSON)
					.get(ComputerDTO.class);
			if (computer == null) {
				throw new IllegalArgumentException("The computer requested with this id does not exist.");
			}

			if (options.hasOption(CONSOLE_ARG_NAME)) {
				computer.setName(options.getOptionValue(CONSOLE_ARG_NAME));
			}

			// Parse the given dates
			if (options.hasOption(CONSOLE_ARG_INTRODUCED)) {
				if (StringUtils.isEmpty(options.getOptionValue(CONSOLE_ARG_INTRODUCED))) {
					computer.setIntroduced(null);
				} else {
					LocalDate introduced = LocalDate.parse(options.getOptionValue(CONSOLE_ARG_INTRODUCED), df);
					computer.setIntroduced(introduced.toString());
				}
			}
			if (options.hasOption(CONSOLE_ARG_DISCONTINUED)) {
				if (StringUtils.isEmpty(options.getOptionValue(CONSOLE_ARG_DISCONTINUED))) {
					computer.setDiscontinued(null);
				} else {
					LocalDate discontinued = LocalDate.parse(options.getOptionValue(CONSOLE_ARG_DISCONTINUED), df);
					computer.setDiscontinued(discontinued.toString());
				}
			}

			// Get the optional manufacturer
			if (options.hasOption(CONSOLE_ARG_MANUFACTURER)) {
				Long manufacturerId = Long.parseLong(options.getOptionValue(CONSOLE_ARG_MANUFACTURER));
				computer.setCompanyId(manufacturerId.toString());
			}

			// Update the computer with the service
			Response response = client.target(URI_COMPUTER).path("/" + id).request()
					.post(Entity.entity(computer, MediaType.APPLICATION_JSON));

			if (response.getStatus() == 200) {
				System.out.println("\t Your computer as been successfully updated !");
			} else {
				System.out.println("\t an error occured : " + response.readEntity(String.class));
			}
		} catch (DateTimeParseException e) {
			System.out.println("\t Your date is not well formated.");
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format. Format it like 2011-12-03");
		} catch (IllegalArgumentException e) {
			System.out.println("\t " + e.getMessage());
		} catch (Exception e) {
			System.out.println("\t " + e.getMessage());
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

			// Check if the computer exist
			ComputerDTO computer = client.target(URI_COMPUTER).path("/" + id).request(MediaType.APPLICATION_JSON)
					.get(ComputerDTO.class);
			if (computer == null) {
				throw new IllegalArgumentException("The computer does not exist.");
			}

			// Delete the computer
			Response response = client.target(URI_COMPUTER).path("/" + id).request().delete();

			if (response.getStatus() == 200) {
				System.out.println("\t Your computer as been successfully deleted !");
			} else {
				System.out.println("\t an error occured : " + response.readEntity(String.class));
			}
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (Exception e) {
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

			// Check if the company exist
			CompanyDTO company = client.target(URI_COMPANY).path("/" + id).request(MediaType.APPLICATION_JSON)
					.get(CompanyDTO.class);
			if (company == null) {
				throw new IllegalArgumentException("The company does not exist.");
			}

			// Delete the computer
			Response response = client.target(URI_COMPANY).path("/" + id).request().delete();

			if (response.getStatus() == 200) {
				System.out.println("\t Your company as been successfully deleted !");
			} else {
				System.out.println("\t an error occured : " + response.readEntity(String.class));
			}
		} catch (NumberFormatException e) {
			System.out.println("\t Your id has a wrong format.");
		} catch (Exception e) {
			System.out.println("\t" + e.getMessage());
		}
	}
}
