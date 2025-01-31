package jobhunter.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jobhunter.jobhunter.dto.company.CompanyConverter;
import jobhunter.jobhunter.domain.Company;
import jobhunter.jobhunter.dto.company.CompanyRequest;
import jobhunter.jobhunter.dto.response.ApiResponse;

import jobhunter.jobhunter.dto.response.ResultPagination;
import jobhunter.jobhunter.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyConverter companyConverter;

    @PostMapping("/companies")
    public ResponseEntity<ApiResponse<Company>> createCompany(@RequestBody @Valid CompanyRequest companyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Company>builder()
                        .statusCode("201")
                        .message("Company created")
                        .data(companyService.handleCreateCompany(companyRequest))
                .build());
    }


    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<ResultPagination>> getCompany(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ResultPagination>builder()
                        .statusCode("200")
                        .message("All companies")
                        .data(companyService.handleGetCompanies(spec, pageable))
                        .build()
                );
    }


    @PutMapping("/companies")
    public ResponseEntity<ApiResponse<Company>> updateCompany(@RequestBody CompanyRequest companyRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Company>builder()
                        .statusCode("200")
                        .message("Company updated")
                        .data(companyService.handleUpdateCompany(companyRequest))
                        .build());
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable long id) {
        companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder()
                        .statusCode("")
                        .message("Company deleted")
                        .data(null)
                        .build());
    }
}
