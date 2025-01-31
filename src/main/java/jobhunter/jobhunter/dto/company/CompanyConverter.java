package jobhunter.jobhunter.dto.company;

import jobhunter.jobhunter.domain.Company;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyConverter {
    @Autowired
    private ModelMapper modelMapper;
    public Company toCompany(CompanyRequest companyRequest) {
        Company company = modelMapper.map(companyRequest, Company.class);
        return company;
    }

    public CompanyResponse toCompanyResponse(Company company) {
        CompanyResponse companyResponse = modelMapper.map(company, CompanyResponse.class);
        return companyResponse;
    }
}
