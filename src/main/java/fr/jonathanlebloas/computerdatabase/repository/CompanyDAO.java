package fr.jonathanlebloas.computerdatabase.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.jonathanlebloas.computerdatabase.model.Company;

public interface CompanyDAO extends JpaRepository<Company, Long> {

	Page<Company> findByNameContaining(Pageable pageable, String search);

}
