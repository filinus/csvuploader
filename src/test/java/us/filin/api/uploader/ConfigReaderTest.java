package us.filin.api.uploader;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;
import us.filin.api.uploader.rest.CSVConfig;
import us.filin.api.uploader.rest.ConfigReader;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class ConfigReaderTest {

    @Spy
    ConfigReader configReader = new ConfigReader();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void readConfig() throws IOException {
        doReturn(
            "csv_format: Excel\n" +
            "has_header: yes\n" +
            "fields:\n" +
            "  first_name:\n" +
            "    required: yes\n" +
            "    position: 1\n" +
            "  last_name:\n" +
            "    required: no\n" +
            "    position: 2\n" +
            "  join_date:\n" +
            "    required: yes\n" +
            "    position: 3\n" +
            "    language_tag: en-US\n"+
            "    style: 3"
        ).when(configReader).getCsvFileByCustomer(anyString());
        CSVConfig csvConfig = configReader.readConfig("customer1");
        assertNotNull(csvConfig);
        assertEquals(CSVFormat.Predefined.Excel, csvConfig.getCsvFormat()  );
        assertNotNull(csvConfig.getFields());
        CSVConfig.DateField joinDateField = csvConfig.getFields().getJoinDate();
        assertNotNull(joinDateField);
        assertNotNull(joinDateField.getDateFormat());
    }
}