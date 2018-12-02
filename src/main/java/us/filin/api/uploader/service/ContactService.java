package us.filin.api.uploader.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import us.filin.api.uploader.rest.CSVConfig;
import us.filin.api.uploader.jpa.Contact;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@Slf4j
public class ContactService {
    private ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void upload(CSVParser csvParser, CSVConfig csvConfig) throws ParseException, IllegalArgumentException {
        boolean hasHeader = csvConfig.isHasHeader();
        List<Contact> contacts = new LinkedList<>();

        for (CSVRecord csvRecord : csvParser) {
            if (hasHeader) {
                hasHeader = false;
                continue;
            }
            checkRequired(csvRecord, csvConfig);
            Contact contact = csvRecordToContact(csvRecord, csvConfig);
            contacts.add(contact);
        }
        contactRepository.saveAll(contacts);
        contactRepository.flush();
    }


    private void checkRequired(CSVRecord csvRecord, CSVConfig csvConfig) throws IllegalArgumentException {
        for (CSVConfig.Field field : csvConfig.allPositioned()) {
            if (field.getPosition() > csvRecord.size()) {
                throw new IllegalArgumentException("to few columns in the CSV, refer with the CSVConfig: " + csvConfig);
            }

            String stringValue = csvRecord.get(field.getIndex());

            if (StringUtils.isEmpty(stringValue) && field.isRequired()) {
                throw new IllegalArgumentException("empty value for required field at column #" + field.getPosition());
            }
        }
    }

    public Contact csvRecordToContact(CSVRecord csvRecord, CSVConfig csvConfig) throws ParseException, IllegalArgumentException {
        final CSVConfig.Fields cf = csvConfig.getFields();

        Contact contact = new Contact();

        contact.setOwner(csvConfig.getOwner());

        int i;

        if (0 <= (i = cf.getFirstName().getIndex())) {
            contact.setFirstName(csvRecord.get(i));
        }

        if (0 <= (i = cf.getLastName().getIndex())) {
            contact.setLastName(csvRecord.get(i));
        }

        if (0 <= (i = cf.getEmail().getIndex())) {
            contact.setEmail(csvRecord.get(i));
        }

        if (0 <= (i = cf.getStreetAddress().getIndex())) {
            contact.setStreetAddress(csvRecord.get(i));
        }

        if (0 <= (i = cf.getZipCode().getIndex())) {
            contact.setZipCode(Short.valueOf(csvRecord.get(i)));
        }

        if (0 <= (i = cf.getJoinDate().getIndex())) {
            Date date = cf.getJoinDate().getDateFormat().parse(csvRecord.get(i));
            contact.setJoinedDate(date);
        }

        if (0 <= (i = cf.getUuid().getIndex())) {
            contact.setUuid(UUID.fromString(csvRecord.get(i)));
        }

        return contact;
    }
}
