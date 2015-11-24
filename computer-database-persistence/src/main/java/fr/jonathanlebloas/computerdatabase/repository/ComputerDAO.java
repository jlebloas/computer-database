package fr.jonathanlebloas.computerdatabase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;

public interface ComputerDAO extends JpaRepository<Computer, Long> {

	Page<Computer> findByNameContainingOrCompany_NameContaining(Pageable pageable, String name, String companyName);

	void removeByCompany(Company company);
}
