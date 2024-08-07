package com.embarkx.companyms.company.impl;

import com.embarkx.companyms.company.Company;
import com.embarkx.companyms.company.CompanyRepository;
import com.embarkx.companyms.company.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public boolean updateCompany(Long id, Company updatedCompany) {
        Optional<Company> companyOptional=companyRepository.findById(id) ;
        if (companyOptional.isPresent()){
            Company temp=companyOptional.get() ;

            temp.setDescription(updatedCompany.getDescription());
            temp.setName(updatedCompany.getName());
            companyRepository.save(temp) ;
            return true ;
        }
        return false;
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company) ;
    }

    @Override
    public boolean deleteCompanyById(Long id) {
        if(companyRepository.findById(id).isEmpty())return false ;
        companyRepository.deleteById(id);
        return true ;
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null) ;
    }
}
