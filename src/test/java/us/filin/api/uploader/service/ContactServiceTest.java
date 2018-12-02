package us.filin.api.uploader.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import us.filin.api.uploader.rest.CSVConfig;
import us.filin.api.uploader.rest.ConfigReader;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class ContactServiceTest {

    @Mock
    ContactRepository contactRepository;

    @Test @Ignore
    public void upload() {
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void csvRecordToContact() throws IOException, ParseException {
        final CSVFormat csvFormat = CSVFormat.Predefined.Default.getFormat();
        final String csv =
                "header1,header2\r\nfirst1,last1\r\nfirst2,last2";
        CSVParser csvParser0 = CSVParser.parse(csv, csvFormat);
        assertNotNull(csvParser0.getRecords());
        assertEquals(3L, csvParser0.getRecordNumber());
        CSVParser csvParser = CSVParser.parse(csv, csvFormat);

        ConfigReader configReader = spy(new ConfigReader());

        doReturn(
                "csv_format: Default\n" +
                        "has_header: yes\n" +
                        "fields:\n" +
                        "  first_name:\n" +
                        "    required: yes\n" +
                        "    position: 1\n" +
                        "  last_name:\n" +
                        "    required: yes\n" +
                        "    position: 2"
        ).when(configReader).getCsvFileByCustomer(anyString());

        CSVConfig csvConfig = configReader.readConfig("any");

        ContactService contactService = new ContactService(contactRepository);
        contactService.upload(csvParser, csvConfig);
        //verify(contactService, times(2)).csvRecordToContact(any(), any());

    }
}