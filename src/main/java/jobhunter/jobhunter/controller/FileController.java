package jobhunter.jobhunter.controller;

import jobhunter.jobhunter.dto.response.ApiResponse;
import jobhunter.jobhunter.dto.file.FileResponse;
import jobhunter.jobhunter.exception.AppException;
import jobhunter.jobhunter.exception.ErrorCode;
import jobhunter.jobhunter.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${base-uri}")
    private String baseUri;

    @Autowired
    private FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<ApiResponse<FileResponse>> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
    ) throws URISyntaxException, IOException {
        // validate data request
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new AppException(ErrorCode.FILE_INVALID);
        }

        // create folder if it's not exsit
        this.fileService.createDirectory(baseUri + folder);
        // save file
        String fileNameRes = this.fileService.store(file, folder, baseUri);
        FileResponse res = FileResponse.builder()
                .fileName(fileNameRes)
                .updatedAt(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<FileResponse>builder()
                        .data(res)
                        .error(null)
                        .message("File uploaded")
                        .statusCode("200")
                        .build());

    }
}
