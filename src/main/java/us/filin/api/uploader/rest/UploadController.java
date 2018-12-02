package us.filin.api.uploader.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import us.filin.api.uploader.service.ContactService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

@RestController
@Slf4j
public class UploadController {
    final private ContactService contactService;
    ConfigReader configReader;

    @Autowired
    public UploadController(
            ContactService contactService,
            ConfigReader configReader
    ) {
        this.contactService = contactService;
        this.configReader = configReader;
    }

    @GetMapping
    @RequestMapping("/ping")
    public ApiResponse ping() {
        return new ApiResponse();
    }


    @GetMapping
    @RequestMapping("/ex")
    public ApiResponse demoException() {
        throw new CustomerConfigNotFoundException("demo exception");
    }

    @GetMapping
    @RequestMapping("/customer/{customer}/contact")
    public ApiResponse contacts() {
        return new ApiResponse();
    }

    @RequestMapping("/customer/{customer}/contact/upload")
    @PostMapping
    public ApiResponse doUpload(
            @PathVariable("customer") String customer,
            @RequestParam("file") MultipartFile file
    ) {
        log.info("doUpload() called for file {}, {}, {} bytes", file.getName(), file.getContentType(), file.getSize());

        CSVConfig csvConfig;
        try {
            csvConfig = configReader.readConfig(customer);
        } catch (IOException e) {
            throw new CustomerConfigNotFoundException("The configReader did not read csvConfig for " + customer, e);
        }
        if (csvConfig == null) {
            throw new CustomerConfigNotFoundException("The configReader did not return a CSVConfig");
        }
        CSVFormat csvFormat = CSVFormat.valueOf(csvConfig.getCsvFormat().name());

        try ( // all are closeable resources
              InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
              BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
              CSVParser csvParser = csvFormat.parse(bufferedReader); //BUG hardcode
        ) {
            contactService.upload(csvParser, csvConfig);
        } catch (ParseException | IllegalArgumentException | IOException e) {
            throw new BadInputException("Fail to parse using CSVConfig", e);
        }

        return new ApiResponse("your csv file uploaded");
    }


}