package us.filin.api.uploader.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import us.filin.api.uploader.service.ContactService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UploadController.class)
public class UploadControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wContext;

    @MockBean
    ContactService contactServiceMockBean;

    @MockBean
    private UploadController uploadControllerMockBean;

    @MockBean
    private ConfigReader configReaderMock;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void doUploadTestMocked() throws Exception {
        String csv = "aa, bb, cc, dd";


        // Mock Request
        MockMultipartFile csvFile = new MockMultipartFile(
                "file.csv",
                "file.csv",
                "text/csv",
                csv.getBytes()
        );

        // Mock Response
        ApiResponse response = new ApiResponse();
        Mockito.when(uploadControllerMockBean.doUpload(
                Mockito.any(String.class),
                Mockito.any(MultipartFile.class))
        ).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/customer/customer123/contact/upload")
                .file("file", csvFile.getBytes())
                .contentType("text/csv")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void doUploadTest415() throws Exception {
        // Mock Request
        MockMultipartFile csvFile = new MockMultipartFile(
                "whatever",
                "whatever",
                "text/csv",
                "whatever".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/customer/customer234/contact/upload")
                .file("file", csvFile.getBytes())
                .contentType("text/plain"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void doUploadTestReal() throws Exception {
        // Mock Request
        MockMultipartFile csvFile = new MockMultipartFile(
                "whatever",
                "whatever",
                "text/csv",
                "header, header\r\nfirst, last\r\nfirst2, last2".getBytes()
        );

        ConfigReader configReader = spy(new ConfigReader());

        doReturn(
        "csv_format: Default\n" +
                "has_header: yes\n" +
                "fields:\n" +
                "  first_name:\n" +
                "    required: yes\n" +
                "    position: 1\n" +
                "  last_name:\n" +
                "    required: no\n" +
                "    position: 2"
        ).when(configReader).getCsvFileByCustomer(anyString());

        UploadController uploadController = new UploadController(contactServiceMockBean, configReader);
        uploadController.doUpload("customer", csvFile);
        verify(contactServiceMockBean, times(1)).upload(any(), any());
    }
}