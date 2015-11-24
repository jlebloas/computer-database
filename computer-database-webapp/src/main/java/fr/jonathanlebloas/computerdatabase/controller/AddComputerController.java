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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;

@Controller
@RequestMapping("/computer/add")
public class AddComputerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddComputerController.class);

	private static final String PATH_ADD_VIEW = "addComputer";

	@Autowired
	private ComputerMapper computerMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private ComputerService computerService;

	@Autowired
	private CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET)
	public String doGet(ModelMap model) {
		LOGGER.info("Add Computer : GET");

		model.addAttribute("computerDTO", new ComputerDTO());
		model.addAttribute("companies", companyMapper.toDTO(companyService.listCompanies()));

		return PATH_ADD_VIEW;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String computerAddSubmit(@ModelAttribute @Valid final ComputerDTO computerDTO,
			final BindingResult bindingResult, final Model model) {
		LOGGER.info("Add Computer : POST : data received : {}", computerDTO);

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().stream().forEach(r -> LOGGER.warn("Invalid computer for add : {}", r));

			model.addAttribute("globalError", bindingResult.getGlobalError());
			model.addAttribute("computer", computerDTO);
			model.addAttribute("companies", companyMapper.toDTO(companyService.listCompanies()));

			return PATH_ADD_VIEW;
		}

		// We can now update the computer
		Computer temp = computerMapper.fromDTO(computerDTO);
		long companyId = temp.getCompany().getId();
		if (companyId <= 0) {
			temp.setCompany(null);
		} else {
			temp.setCompany(companyService.find(temp.getCompany().getId()));
		}
		computerService.create(temp);

		return "redirect:/";
	}

}
