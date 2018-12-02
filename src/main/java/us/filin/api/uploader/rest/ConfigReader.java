package us.filin.api.uploader.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ConfigReader {

    @Autowired
    private ApplicationContext applicationContext;

    public ObjectMapper getMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    static Pattern BAD_CHARACTERS = Pattern.compile("\\W");
    public String getCsvFileByCustomer(String customer) throws CustomerConfigNotFoundException, IOException {
        if (BAD_CHARACTERS.matcher(customer).matches()) {
            throw new CustomerConfigNotFoundException("bad characters in customer name");
        }
        Resource resource = applicationContext.getResource("file:"+customer+".csvconfig.yaml");
        File file = resource.getFile();
        if (!file.exists() || !file.isFile()) {
            throw new CustomerConfigNotFoundException("csv file not found, or not a true file");
        }
        byte[] result = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        return new String(result); //for debug purposes I prefer to use String here
    }

    public CSVConfig readConfig(String customer) throws CustomerConfigNotFoundException, IOException {
        ObjectMapper mapper = getMapper();
        try {
            String fileContent = getCsvFileByCustomer(customer);
            CSVConfig csvConfig = mapper.readValue(fileContent, CSVConfig.class);
            csvConfig.setOwner(customer);
            return csvConfig;
        } catch (IOException ex) {
            log.error("problem to read", ex);
            throw ex;
        }
    }
}