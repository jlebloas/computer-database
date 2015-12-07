package fr.jonathanlebloas.computerdatabase.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;

@Controller
@RequestMapping(path = "/computer/edit")
public class EditComputerController {

	private static final String ATTR_COMPANIES = "companies";

	private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerController.class);

	private static final String PATH_UPDATE_VIEW = "editComputer";

	@Autowired
	private ComputerMapper computerMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private ComputerService computerService;

	@Autowired
	private CompanyService companyService;

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public String doGet(@PathVariable(value = "id") final int computerId,
			ModelMap model) {
		LOGGER.info("Edit Computer : GET with id {}", computerId);

		Computer computer = computerService.find(computerId);

		model.addAttribute("computerDTO", computerMapper.toDTO(computer));
		model.addAttribute(ATTR_COMPANIES, companyMapper.toDTO(companyService.listCompanies()));

		return PATH_UPDATE_VIEW;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String computerEditSubmit(@ModelAttribute @Valid final ComputerDTO computerDTO,
			final BindingResult bindingResult, final Model model) {
		LOGGER.info("Edit Computer : POST : data received : {}", computerDTO);

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().stream().forEach(r -> LOGGER.warn("Invalid edited computer : {}", r));

			model.addAttribute("globalError", bindingResult.getGlobalError());
			model.addAttribute("computer", computerDTO);
			model.addAttribute(ATTR_COMPANIES, companyMapper.toDTO(companyService.listCompanies()));

			return PATH_UPDATE_VIEW;
		}

		// We can now update the computer
		Computer temp = computerMapper.fromDTO(computerDTO);
		if (temp.getCompany() != null) {
			temp.setCompany(companyService.find(temp.getCompany().getId()));
		}
		computerService.update(temp);

		return "redirect:/";
	}
}
