package us.filin.api.uploader.rest;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.csv.CSVFormat;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Locale;

@Data
@ToString
public class CSVConfig {
    @JsonProperty(required = false)
    @JsonAlias("csv_format")
    private CSVFormat.Predefined csvFormat;
    @JsonAlias("has_header")
    @JsonProperty(required = true)
    private boolean hasHeader;
    @JsonProperty(required = true)
    private Fields fields;

    @JsonIgnore
    private String owner;

    @JsonIgnore
    public Field[] allKnownFields() {
        return new Field[]{
            fields.getFirstName(),
            fields.getLastName(),
            fields.getMiddleInitial(),
            fields.getEmail(),
            fields.getStreetAddress(),
            fields.getZipCode(),
            fields.getJoinDate(),
            fields.getUuid()
        };
    }

    @JsonIgnore
    public Field[] allRequired() {
        return Arrays.stream(this.allKnownFields())
                .filter(Field::isRequired)
                .toArray(Field[]::new);
    }

    @JsonIgnore
    public Field[] allPositioned() {
        return Arrays.stream(this.allKnownFields())
                .filter(Field::hasPosition)
                .toArray(Field[]::new);
    }

    @Getter
    public static class Field {
        @JsonProperty(required = false, defaultValue = "0")
        private boolean required;

        @JsonProperty(required = true)
        //index is 1 based
        private int position = 0;

        @JsonIgnore
        public boolean hasPosition() {
            return position > 0;
        }

        @JsonIgnore
        //index is 0 based
        public int getIndex() {
            return position - 1;
        }
    }

    @Getter
    public static class DateField extends Field {
        @JsonProperty(required = false)
        @JsonAlias("language_tag")
        private String languageTag = "en-US";

        @JsonProperty(required = false)
        private int style = 3; //default short style like MM/dd/yy

        @JsonIgnore
        private DateFormat dateFormat;

        public void setStyle(int style) {
            this.style = style;
            updateDateFormat();
        }

        public void setLanguageTag(String languageTag) {
            this.languageTag = languageTag;
            updateDateFormat();
        }

        private void updateDateFormat() {
            Locale locale = Locale.forLanguageTag(languageTag);
            dateFormat = DateFormat.getDateInstance(style, locale);
        }
    }

    @Getter
    public static class Fields {
        static Field PLACEHOLDER = new Field();
        static DateField DATE_PLACEHOLDER = new DateField();


        @JsonProperty(required = false)
        @JsonAlias("first_name")
        private Field firstName = PLACEHOLDER;

        @JsonProperty(required = false)
        @JsonAlias("last_name")
        private Field lastName = PLACEHOLDER;

        @JsonProperty(required = false)
        @JsonAlias("middle_initial")
        private Field middleInitial = PLACEHOLDER;

        @JsonProperty(required = false)
        @JsonAlias("email")
        private Field email = PLACEHOLDER;

        @JsonProperty(required = false)
        @JsonAlias("street_address")
        private Field streetAddress = PLACEHOLDER;

        @JsonProperty(required = false)
        @JsonAlias("zip_code")
        private Field zipCode = PLACEHOLDER;

        @JsonProperty(required = false)
        @JsonAlias("join_date")
        private DateField joinDate = DATE_PLACEHOLDER;


        @JsonProperty(required = false)
        @JsonAlias("uuid")
        private Field uuid = PLACEHOLDER;
    }
}
