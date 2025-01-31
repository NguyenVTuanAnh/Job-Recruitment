package jobhunter.jobhunter.service;

import jobhunter.jobhunter.dto.company.CompanyConverter;
import jobhunter.jobhunter.domain.Company;
import jobhunter.jobhunter.domain.User;
import jobhunter.jobhunter.dto.company.CompanyRequest;
import jobhunter.jobhunter.dto.response.Meta;
import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.repository.CompanyRepository;
import jobhunter.jobhunter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private UserRepository userRepository;
    public Company handleCreateCompany(CompanyRequest companyRequest) {
        Company company = companyConverter.toCompany(companyRequest);
        companyRepository.save(company);
        return company;
    }

    public ResultPagination handleGetCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(spec,pageable);
        return ResultPagination.builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber())              // curent page
                        .pages(companies.getTotalPages())                 // size of a page
                        .pageSize(pageable.getPageSize())        // amount page
                        .total(companies.getTotalElements())        // total elements
                        .build())
                .result(companies.getContent())
                .build();
    }

    public Company handleUpdateCompany(CompanyRequest companyRequest) {
        Company company = companyRepository.findById(companyRequest.getId()).orElse(null);
        if (company != null) {
            if (companyRequest.getName() != null) company.setName(companyRequest.getName());
            if (companyRequest.getAddress() != null) company.setAddress(companyRequest.getAddress());
            if(companyRequest.getDescription() != null) company.setDescription(companyRequest.getDescription());
            if (companyRequest.getLogo() != null) company.setLogo(companyRequest.getLogo());
            companyRepository.save(company);
            return company;
        }
        return null;

    }

    public void handleDeleteCompany(long id) {
        Optional<Company> company = companyRepository.findCompanyById(id);
        if (company.isPresent()) {
            Company com = company.get();
            // fetch all user belong to this company
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }
        companyRepository.deleteById(id);
    }

    public Optional<Company> handleGetCompanyById(long id) {
        return companyRepository.findCompanyById(id);
    }

}
