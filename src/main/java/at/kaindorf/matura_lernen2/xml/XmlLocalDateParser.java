package at.kaindorf.matura_lernen2.xml;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.LogRecord;
import java.util.logging.XMLFormatter;

public class XmlLocalDateParser extends XmlAdapter<String, LocalDate> {

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate unmarshal(String s) throws Exception {
        return LocalDate.parse(s,dtf);
    }

    @Override
    public String marshal(LocalDate localDate) throws Exception {
        return dtf.format(localDate);
    }
}
